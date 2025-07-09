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
        Pose2d beginPose = new Pose2d(-8, 62, 90);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(beginPose)
                        .splineToConstantHeading(new Vector2d(5, -32), 90)
                        .stopAndAdd(new InstantAction(()->{})) // Score preloaded specimen
                        .strafeTo(new Vector2d(5, -40))

                        .splineToConstantHeading(new Vector2d(48, -40), 90)
                        .stopAndAdd(new InstantAction(()->{})) // Grab first specimen
                        .splineToConstantHeading(new Vector2d(48, -60), 90)
                        .stopAndAdd(new InstantAction(()->{})) // Deposit and grab from observation zone
                        .splineToConstantHeading(new Vector2d(7, -32), 90)
                        .stopAndAdd(new InstantAction(()->{})) // Score first specimen
                        .build());
    }
}
