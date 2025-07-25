package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystem.ControlClaw;
import org.firstinspires.ftc.teamcode.subsystem.Robot;

@Autonomous(name = "Sample Auto")
public class SampleAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(-32, -62, Math.toRadians(90));

        Robot robot = new Robot(hardwareMap, telemetry);

        waitForStart();

        Actions.runBlocking(
                robot.driveTrain.getDrive().actionBuilder(beginPose)
                        // Deposit preloaded Sample
                        .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(45)), Math.toRadians(225))
                        .waitSeconds(4)

                        // Grab Sample
                        .splineToLinearHeading(new Pose2d(-48, -44, Math.toRadians(90)), Math.toRadians(90))
                        .waitSeconds(2)

                        // Deposit in High Bucket
                        .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(45)), Math.toRadians(225))
                        .waitSeconds(4)

                        // Grab Sample
                        .splineToLinearHeading(new Pose2d(-52, -44, Math.toRadians(90)), Math.toRadians(90))
                        .waitSeconds(2)

                        // Deposit in High Bucket
                        .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(45)), Math.toRadians(225))
                        .waitSeconds(4)
                        .build());
    }
}
