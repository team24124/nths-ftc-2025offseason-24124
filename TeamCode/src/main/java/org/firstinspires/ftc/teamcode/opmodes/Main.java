package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.Extension;
import org.firstinspires.ftc.teamcode.subsystem.FieldCentricDriveTrain;
import org.firstinspires.ftc.teamcode.subsystem.Robot;
import org.firstinspires.ftc.teamcode.subsystem.RobotCentricDriveTrain;
import org.firstinspires.ftc.teamcode.utility.ActionScheduler;
import org.firstinspires.ftc.teamcode.utility.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryMaster;

@TeleOp(name = "Main", group = "!")
public class Main extends OpMode {
    private Robot robot;
    private ActionScheduler actions;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        actions = ActionScheduler.INSTANCE;
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        if(gamepad1.a) actions.schedule(robot.extension.toggleExtension());
        if(gamepad1.b) actions.schedule(robot.collectionClaw.setElbowPosition(0));
        if(gamepad1.x) actions.schedule(robot.collectionClaw.setElbowPosition(0.5));
        if(gamepad1.y) actions.schedule(robot.collectionClaw.setElbowPosition(1.0));

        robot.driveTrain.drive(x, y, rx);
        robot.telemetryMaster.update(); // Update telemetry
        ActionScheduler.INSTANCE.run(); // Call this in order for actions to work
    }
}
