package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.RobotCentricDriveTrain;
import org.firstinspires.ftc.teamcode.utility.subsystems.DriveTrain;

@TeleOp(name = "Drive Only", group = "!")
public class DriveOnly extends OpMode {
    private DriveTrain driveTrain;

    @Override
    public void init() {
        driveTrain = new RobotCentricDriveTrain(hardwareMap,
                new Pose2d(0, 0, Math.toRadians(0)));
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x;
        double rx = -gamepad1.right_stick_x;

        driveTrain.drive(x, y, rx);
    }
}
