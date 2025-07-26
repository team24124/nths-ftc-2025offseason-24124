package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Button;
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Trigger;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.subsystem.Robot;
import org.firstinspires.ftc.teamcode.subsystem.Slides;
import org.firstinspires.ftc.teamcode.utility.ActionScheduler;

@TeleOp(name = "Main", group = "!")
public class Main extends OpMode {
    private Robot robot;
    private ActionScheduler actions;
    private GamepadEx driver, operator;


    private final double kP = 0.0022; //add to teleop
    boolean toggleSearch = false; //add to teleop
    private Limelight3A limelight;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        actions = ActionScheduler.INSTANCE;

        limelight.pipelineSwitch(2);
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)

        actions.init();
        limelight.start();


        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x;
        double rx = -gamepad1.right_stick_x;

        if(driver.wasJustPressed(Button.DPAD_LEFT)) {
            limelight.pipelineSwitch(0);
        }

        if(driver.wasJustPressed(Button.DPAD_UP)){
            limelight.pipelineSwitch(1);
        }

        if(driver.wasJustPressed(Button.DPAD_RIGHT)){
            limelight.pipelineSwitch(2);
        }

        // Change speeds of drive train
        if (driver.wasJustPressed(Button.LEFT_BUMPER)) {
            actions.schedule(new InstantAction(robot.driveTrain.getSpeeds()::previous));
        }
        if (driver.wasJustPressed(Button.RIGHT_BUMPER)) {
            actions.schedule(new InstantAction(robot.driveTrain.getSpeeds()::next));
        }

        // Reset the orientation for the the field-centric drive
        if (driver.wasJustPressed(Button.START)) {
            Vector2d current = robot.driveTrain.getDrive().localizer.getPose().position;
            robot.driveTrain.getDrive().localizer.setPose(new Pose2d(current, 0));
        }

        // Toggle between extending and completely the rest of the passthrough
        if (operator.wasJustPressed(Button.A)) {
            if (!robot.isExtended() && !robot.controlClaw.isCollecting()) {
                actions.schedule(robot.extendCollection());
            } else {
                actions.schedule(robot.passthrough());
            }
        }

        // Toggle the top claw between scoring and collecting
        if (operator.wasJustPressed(Button.X)) {
            if (robot.isInScoringMode() && !robot.isExtended()) {
                actions.schedule(robot.scoreSpecimen());
            } else {
                actions.schedule(robot.moveToScore());
            }
        }

        if (operator.wasJustPressed(Button.B)) {
            actions.schedule(robot.moveToBucket());
        }

        // Open/Close Top Claw
        if (driver.wasJustPressed(Button.Y) || operator.wasJustPressed(Button.Y)) {
            actions.schedule(robot.controlClaw.toggleClaw());
        }

        // Reset Top Arm to starting positions
        if (operator.wasJustPressed(Button.BACK)) {
            actions.schedule(robot.resetControlArm());
        }

        if (operator.wasJustPressed(Button.START)) {
            robot.slides.positions.setSelected(Slides.State.HOME);
            robot.slides.stopAndResetEncoders();

        }

        // Control Viper Slides
        if (operator.wasJustPressed(Button.DPAD_UP)) {
            if(!robot.slides.isMoving){
                actions.schedule(robot.slides.nextPos());
            }
        }
        if (operator.wasJustPressed(Button.DPAD_DOWN)) {
            if(!robot.slides.isMoving){
                actions.schedule(new SequentialAction(
                        robot.slides.prevPos(),
                        new InstantAction(
                                () -> {
                                    if (robot.slides.positions.getSelected() == Slides.State.HOME)
                                        robot.slides.stopAndResetEncoders();
                                }
                        )
                ));
            }
        }

        if (operator.wasJustPressed(Button.DPAD_LEFT)) {
            if(!robot.slides.isMoving && !robot.isExtended()){
                actions.schedule(robot.collectFromWall());
            }
        }

        // Collection Claw Pivots
        if (operator.wasJustPressed(Button.LEFT_BUMPER)) {
            actions.schedule(robot.collectionClaw.prevPivot());
        }
        if (operator.wasJustPressed(Button.RIGHT_BUMPER)) {
            actions.schedule(robot.collectionClaw.nextPivot());
        }

        // Move the arm back and forth using triggers
        if (operator.getTrigger(Trigger.RIGHT_TRIGGER) > 0 || operator.getTrigger(Trigger.LEFT_TRIGGER) > 0) {
            double rightTrigger = operator.getTrigger(Trigger.RIGHT_TRIGGER);
            double leftTrigger = operator.getTrigger(Trigger.LEFT_TRIGGER);
            int fudgedArmPosition = (int) (robot.arm.getFudgeFactor() * (rightTrigger + -leftTrigger));
            actions.schedule(
                    robot.arm.moveTo((robot.arm.getCurrentState().position - fudgedArmPosition))
            );
        }

        if (limelight.isRunning() && limelight.getLatestResult() != null) { //everything in this if statement goes into teleop
            if (limelight.getLatestResult().getPythonOutput() != null) {
                LLResult result = limelight.getLatestResult();
                double[] pythonOutputs = result.getPythonOutput();
                if (pythonOutputs != null && pythonOutputs.length > 0) {
                    telemetry.addData("valid target", pythonOutputs[0]);
                    telemetry.addData("targetX", pythonOutputs[1]);
                    telemetry.addData("targetY", pythonOutputs[2]);
                    telemetry.addData("angle", pythonOutputs[3]);
                    telemetry.addData("area %", pythonOutputs[4]);
                    telemetry.addData("searching ", toggleSearch);
                    telemetry.update();
                }
            }

            if (gamepad1.aWasPressed()) toggleSearch = !toggleSearch;

            if (limelight.getLatestResult().getPythonOutput() != null && limelight.getLatestResult().getPythonOutput()[0] == 1 && toggleSearch) {
                double[] array = limelight.getLatestResult().getPythonOutput();
                double targetX = array[1];
                double targetY = array[2];
                double angle = array[3];
                double strafeX = (targetX - 471) * kP;
                double strafeY = (targetY - 270) * kP;
                while (targetX < 466 || targetX > 476 || targetY < 265 || targetY > 275) {
                    array = limelight.getLatestResult().getPythonOutput();
                    targetX = array[1];
                    targetY = array[2];
                    angle = array[3];
                    strafeX = (targetX - 471) * kP;
                    strafeY = (targetY - 270) * kP;

                    if (array[0] == 0) break;

                    double baseX = 0;
                    double baseY = 0;

                    if (strafeX < 0) baseX = -0.1;
                    else if (strafeX > 0) baseX = 0.1;

                    if (strafeY < 0) baseY = -0.1;
                    else if (strafeY > 0) baseY = 0.1;

                    // Calculate the power for each wheel
                    double frontLeftPower = -strafeY + strafeX + baseX - baseY;
                    double frontRightPower = strafeY - strafeX - baseX - baseY;
                    double backLeftPower = -strafeY - strafeX - baseX - baseY;
                    double backRightPower = -strafeY + strafeX + baseX - baseY;
                    // Set the motor powers
                    robot.driveTrain.setDrivePowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower);



                    if (gamepad1.x) {
                        toggleSearch = false;
                        robot.driveTrain.setDrivePowers(0, 0, 0, 0);

                        break;
                    }

                    if (targetX > 466 && targetX < 476 && targetY > 265 && targetY < 275) { //change servo movements here to what you want
                        robot.driveTrain.setDrivePowers(0, 0, 0, 0);

                        actions.schedule(new SequentialAction(
                                robot.extendCollection(),
                                robot.collectionClaw.setPivotPosition(0.82 - (((double) 0.67/180) * angle))
                        ));

                        toggleSearch = !toggleSearch;
                        break;
                    }
                }
            }
        }


        robot.driveTrain.drive(x, y, rx);
        robot.telemetryMaster.update(); // Update telemetry

        driver.readButtons();
        operator.readButtons();

        robot.slides.periodic();
        robot.driveTrain.periodic();

        ActionScheduler.INSTANCE.run(); // Call this in order for actions to work
    }

    @Override
    public void stop() {
        actions.stop();
    }
}
