package org.firstinspires.ftc.teamcode.opmodes;

import static java.lang.Thread.sleep;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystem.Arm;
import org.firstinspires.ftc.teamcode.subsystem.CollectionClaw;
import org.firstinspires.ftc.teamcode.subsystem.Extension;
import org.firstinspires.ftc.teamcode.subsystem.FieldCentricDriveTrain;
import org.firstinspires.ftc.teamcode.subsystem.RobotCentricDriveTrain;
import org.firstinspires.ftc.teamcode.subsystem.Slides;
import org.firstinspires.ftc.teamcode.utility.subsystems.DriveTrain;

@TeleOp(name = "Simple Main", group = "!")
public class SimpleMain extends OpMode {

    private DriveTrain driveTrain;
    private CollectionClaw collectionClaw;
    private Extension extension;
    private Slides slides;
    private Arm arm;
    private Limelight3A limelight;

    private boolean isClawOpen;

    double kP = 0.0025; //0.0022; //add to teleop
    boolean toggleSearch = false; //add to teleop

    @Override
    public void init() {
        driveTrain = new FieldCentricDriveTrain(hardwareMap,
                new Pose2d(0, 0, Math.toRadians(0)));
        extension = new Extension(hardwareMap);
        collectionClaw = new CollectionClaw(hardwareMap);
        slides = new Slides(hardwareMap);
        arm = new Arm(hardwareMap);

        isClawOpen = true;
        collectionClaw.setClaw(CollectionClaw.ClawState.OPEN.position);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        if (limelight.getStatus().getPipelineIndex() != 2) {
            limelight.pipelineSwitch(2); // Switch to pipeline number 0
        }
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
    }

    @Override
    public void start() {
        limelight.start();
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x;
        double rx = -gamepad1.right_stick_x;

        driveTrain.drive(x, y, rx);

        // GAMEPAD 1
        if(gamepad1.leftBumperWasPressed()){
            driveTrain.getSpeeds().previous();
        }

        if(gamepad1.rightBumperWasPressed()){
            driveTrain.getSpeeds().next();
        }

        if(gamepad1.startWasPressed()){
            driveTrain.getDrive().localizer.setPose(new Pose2d(0, 0, Math.toRadians(0)));
        }

        if(gamepad1.dpad_left){ limelight.pipelineSwitch(0); }
        if(gamepad1.dpad_up){ limelight.pipelineSwitch(1); }
        if(gamepad1.dpad_right){ limelight.pipelineSwitch(2); }

        // GAMEPAD 2
        if(gamepad2.backWasPressed()){
            arm.setPosition(Arm.State.HOME.position);
        }

        if(gamepad2.startWasPressed()){
            arm.setPosition(Arm.State.WALL.position);
        }

        if(gamepad2.dpadLeftWasPressed()){
            slides.stopAndResetEncoders();
        }

        if(gamepad2.dpadUpWasPressed()){ slides.positions.next(); }
        if(gamepad2.dpadDownWasPressed()){ slides.positions.previous(); }

        if(gamepad2.rightBumperWasPressed()){
            collectionClaw.setPivot(
                    collectionClaw.pivotStates.next().getSelected().position
            );
        }

        if(gamepad2.leftBumperWasPressed()){
            collectionClaw.setPivot(
                    collectionClaw.pivotStates.previous().getSelected().position
            );
        }

        if(gamepad2.aWasPressed()){
            extension.setExtension(Extension.State.EXTENDED);
            collectionClaw.setElbowPositions(CollectionClaw.ElbowState.HOVER.position);
            collectionClaw.setClaw(CollectionClaw.ClawState.OPEN.position);
            collectionClaw.setPivot(CollectionClaw.PivotState.ONEEIGHTY.position);
        }

        if(gamepad2.xWasPressed()){
            collectionClaw.setElbowPositions(CollectionClaw.ElbowState.PASSTHROUGH.position);
            extension.setExtension(0.2);
        }

        if(gamepad2.bWasPressed()){
            collectionClaw.setElbowPositions(CollectionClaw.ElbowState.ACTIVE.position);
            collectionClaw.setClaw(CollectionClaw.ClawState.CLOSED.position);
        }

        if(gamepad2.yWasPressed()){
            if(isClawOpen){
                collectionClaw.setClaw(CollectionClaw.ClawState.CLOSED.position);
                isClawOpen = false;
            }else{
                collectionClaw.setClaw(CollectionClaw.ClawState.OPEN.position);
                isClawOpen = true;
            }
        }

        // Move the arm back and forth using triggers
        if (gamepad2.right_trigger > 0 || gamepad2.left_trigger > 0) {
            double rightTrigger = gamepad2.right_trigger;
            double leftTrigger = gamepad2.left_trigger;
            int fudgedArmPosition = (int) (arm.getFudgeFactor() * (rightTrigger + -leftTrigger));
            arm.moveTo((arm.armMotor.getCurrentPosition() - fudgedArmPosition));
        }


        slides.periodic();

        // Limelight
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
                    driveTrain.setDrivePowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower);

                    if (gamepad1.xWasPressed()) {
                        toggleSearch = false;
                        driveTrain.setDrivePowers(0, 0, 0, 0);
                        break;
                    }

                    if (targetX > 466 && targetX < 476 && targetY > 265 && targetY < 275) { //change servo movements here to what you want
                        driveTrain.setDrivePowers(0, 0, 0, 0); //keep motor powers

                        extension.setExtension(0.8);
                        collectionClaw.setElbowPositions(CollectionClaw.ElbowState.ACTIVE.position);
                        //right tilted block is 90 to 180:  //
                        //left tilted block is 0 to 90:  \\
                        collectionClaw.setPivot(0.82 - (((double) 0.67 / 180) * angle));  //if ll is attached static
                        //bottom pivot positions are 0.15 0.5 0.82 from left to right  | _ |
                        try {
                            sleep(1800);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        collectionClaw.setClawPosition(0.6);
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        collectionClaw.setElbowPosition(0.65);

                        toggleSearch = !toggleSearch;
                        break;
                    }
                }
            }
        }
    }
}
