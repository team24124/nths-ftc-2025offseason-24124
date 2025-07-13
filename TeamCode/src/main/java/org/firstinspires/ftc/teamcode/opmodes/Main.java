package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Button;
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Trigger;
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

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        actions = ActionScheduler.INSTANCE;
        actions.init();

        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        // Change speeds of drive train
        if(driver.wasJustPressed(Button.LEFT_BUMPER)) { actions.schedule(new InstantAction(robot.driveTrain.getSpeeds()::previous)); }
        if(driver.wasJustPressed(Button.RIGHT_BUMPER)) { actions.schedule(new InstantAction(robot.driveTrain.getSpeeds()::next)); }

        // Reset the orientation for the the field-centric drive
        if(driver.wasJustPressed(Button.START)) {
            Vector2d current = robot.driveTrain.getDrive().localizer.getPose().position;
            robot.driveTrain.getDrive().localizer.setPose(new Pose2d(current, 0));
        }

        // Toggle between extending and completely the rest of the passthrough
        if(operator.wasJustPressed(Button.A)) {
            if(!robot.isExtended()){
                actions.schedule(robot.extendCollection());
            }else{
                actions.schedule(robot.passthrough());
            }
        }

        // Toggle the top claw between scoring and collecting
        if(operator.wasJustPressed(Button.X)){
            if(robot.isInScoringMode()){
                actions.schedule(robot.scoreSpecimen());
            }else {
                actions.schedule(robot.moveToScore());
            }
        }

        // Open/Close Top Claw
        if (driver.wasJustReleased(Button.Y)) { actions.schedule(robot.controlClaw.toggleClaw()); }

        // Reset Top Arm to starting positions
        if(operator.wasJustPressed(Button.BACK)) { actions.schedule(robot.resetControlArm()); }

        // Control Viper Slides
        if(operator.wasJustPressed(Button.DPAD_UP)) { actions.schedule(robot.slides.nextPos()); }
        if(operator.wasJustPressed(Button.DPAD_DOWN)) { actions.schedule(robot.slides.prevPos()); }

        if(operator.wasJustPressed(Button.DPAD_LEFT)) { 
            actions.schedule(new SequentialAction(
                    robot.slides.setStateTo(Slides.State.HOME),
                    robot.collectFromWall()
            ));
        }

        // Collection Claw Pivots
        if(operator.wasJustPressed(Button.LEFT_BUMPER)) { actions.schedule(robot.collectionClaw.prevPivot()); }
        if(operator.wasJustPressed(Button.RIGHT_BUMPER)) { actions.schedule(robot.collectionClaw.nextPivot()); }

        // Move the arm back and forth using triggers
        if(operator.getTrigger(Trigger.RIGHT_TRIGGER) > 0 || operator.getTrigger(Trigger.LEFT_TRIGGER) > 0){
            double rightTrigger = operator.getTrigger(Trigger.RIGHT_TRIGGER);
            double leftTrigger = operator.getTrigger(Trigger.LEFT_TRIGGER);
            int fudgedArmPosition = (int)(robot.arm.getFudgeFactor() * (rightTrigger + -leftTrigger));
            actions.schedule(
                    robot.arm.moveTo((robot.arm.getCurrentState().position - fudgedArmPosition))
            );
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
