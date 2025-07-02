package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Button;
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Trigger;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.subsystem.Robot;
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

        if(driver.wasJustPressed(Button.A)) {
            if(!robot.isExtended()){
                actions.schedule(robot.extendCollection());
            }else{
                actions.schedule(robot.passthrough());
            }
        }

        if(driver.wasJustPressed(Button.X)){
            if(robot.isInScoringMode()){
                actions.schedule(robot.collectFromWall());
            }else {
                actions.schedule(robot.moveToScore());
            }
        }

        if (driver.wasJustPressed(Button.Y)) {
            actions.schedule(robot.controlClaw.toggleClaw());
        }

        if(driver.wasJustPressed(Button.BACK)) { actions.schedule(robot.resetControlArm()); }

        // Control Viper Slides
        if(driver.wasJustPressed(Button.DPAD_UP)) { actions.schedule(robot.slides.nextPos()); }
        if(driver.wasJustPressed(Button.DPAD_DOWN)) { actions.schedule(robot.slides.prevPos()); }

        // Collection Claw Pivots
        if(driver.wasJustPressed(Button.LEFT_BUMPER)) { actions.schedule(robot.collectionClaw.prevPivot()); }
        if(driver.wasJustPressed(Button.RIGHT_BUMPER)) { actions.schedule(robot.collectionClaw.nextPivot()); }

        // Move the arm back and forth using triggers
        if(driver.getTrigger(Trigger.RIGHT_TRIGGER) > 0 || driver.getTrigger(Trigger.LEFT_TRIGGER) > 0){
            double rightTrigger = driver.getTrigger(Trigger.RIGHT_TRIGGER);
            double leftTrigger = driver.getTrigger(Trigger.LEFT_TRIGGER);
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
        telemetry.addData("IsExtended", robot.isExtended());
        ActionScheduler.INSTANCE.run(); // Call this in order for actions to work
    }

    @Override
    public void stop() {
        actions.stop();
    }
}
