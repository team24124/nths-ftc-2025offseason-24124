package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.Robot;

@TeleOp(name = "Reset Encoders", group = "!")
public class ResetEncoders extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        waitForStart();

        robot.slides.stopAndResetEncoders();
        robot.arm.stopAndResetEncoders();
        telemetry.addLine("Encoders successfully reset to zero.");
        telemetry.update();
    }
}
