package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.Arm;
import org.firstinspires.ftc.teamcode.subsystem.ControlClaw;
import org.firstinspires.ftc.teamcode.subsystem.Extension;
import org.firstinspires.ftc.teamcode.subsystem.FieldCentricDriveTrain;
import org.firstinspires.ftc.teamcode.subsystem.Robot;
import org.firstinspires.ftc.teamcode.subsystem.RobotCentricDriveTrain;
import org.firstinspires.ftc.teamcode.subsystem.Slides;
import org.firstinspires.ftc.teamcode.utility.ActionScheduler;
import org.firstinspires.ftc.teamcode.utility.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryMaster;

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

        if(driver.wasJustPressed(GamepadKeys.Button.START)) {
            robot.slides.triggerStartFlag();
        }

        if(driver.wasJustPressed(GamepadKeys.Button.A)) actions.schedule(robot.extendCollection());
        if(driver.wasJustPressed(GamepadKeys.Button.B)) actions.schedule(robot.retractCollection());
        if(driver.wasJustPressed(GamepadKeys.Button.X)) actions.schedule(robot.collectControl());
        if(driver.wasJustPressed(GamepadKeys.Button.Y)) actions.schedule(robot.activeControl());
        if(driver.wasJustPressed(GamepadKeys.Button.DPAD_UP)){
            actions.schedule(
                    new SequentialAction(
                            robot.retractCollection(),
                            new SleepAction(0.8),
                            robot.collectControl(),
                            new SleepAction(0.5),
                            robot.activeControl()
                    )
            );
        }
        if(driver.wasJustPressed(GamepadKeys.Button.BACK)) {
            actions.schedule(
                    new SequentialAction(
                            robot.arm.moveTo(Arm.State.HOME),
                            robot.slides.moveTo(Slides.State.PASSTHROUGH.position),
                            robot.controlClaw.setClawPosition(ControlClaw.ClawState.OPEN),
                            robot.controlClaw.setElbowPosition(ControlClaw.ElbowState.PASSTHROUGH),
                            robot.controlClaw.setPivotPosition(ControlClaw.PivotState.TWOSEVENTY)
                    )
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
