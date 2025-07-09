package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystem.ControlClaw;
import org.firstinspires.ftc.teamcode.subsystem.Robot;
import org.firstinspires.ftc.teamcode.subsystem.Slides;

@Autonomous(name = "Specimen Auto")
public class SpecimenAuto extends LinearOpMode {
    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry);

        Pose2d initialPose = new Pose2d(-8, 62, Math.toRadians(90));
        robot.driveTrain.getDrive().localizer.setPose(initialPose);
        waitForStart();

        if (isStopRequested()) return; //

        // Drive forwards 10 inches, extend collection, pass block through
        Actions.runBlocking(
                robot.driveTrain.getDrive().actionBuilder(initialPose)
                        .splineToConstantHeading(new Vector2d(5, -32), 90)
                        .stopAndAdd(new InstantAction(()->{})) // Score preloaded specimen
                        .strafeTo(new Vector2d(5, -40))

                        .splineToConstantHeading(new Vector2d(48, -40), 90)
                        .stopAndAdd(new InstantAction(()->{})) // Grab first specimen
                        .splineToConstantHeading(new Vector2d(48, -60), 90)
                        .stopAndAdd(new InstantAction(()->{})) // Deposit and grab from observation zone
                        .splineToConstantHeading(new Vector2d(7, -32), 90)
                        .stopAndAdd(new InstantAction(()->{})) // Score first specimen
                        .build()
        );
    }
}
