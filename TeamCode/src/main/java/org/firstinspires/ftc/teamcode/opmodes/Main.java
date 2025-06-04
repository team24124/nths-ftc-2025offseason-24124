package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.Extension;
import org.firstinspires.ftc.teamcode.subsystem.FieldCentricDriveTrain;
import org.firstinspires.ftc.teamcode.subsystem.RobotCentricDriveTrain;
import org.firstinspires.ftc.teamcode.utility.ActionScheduler;
import org.firstinspires.ftc.teamcode.utility.subsystems.DriveTrain;

@TeleOp(name = "Main", group = "!")
public class Main extends OpMode {
    private DriveTrain driveTrain;
    private Extension extension;

    @Override
    public void init() {
        driveTrain = new RobotCentricDriveTrain(hardwareMap, new Pose2d(
                new Vector2d(0, 0), Math.toRadians(0)));
        extension = new Extension(hardwareMap);
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        if(gamepad1.a) ActionScheduler.INSTANCE.schedule(extension.toggleExtension());
        if(gamepad1.b) extension.setExtension(Extension.leftLimit);
        if(gamepad1.x) extension.setExtension(Extension.rightLimit);

        driveTrain.drive(x, y, rx);
        ActionScheduler.INSTANCE.run();
    }
}
