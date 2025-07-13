package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
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

        Pose2d initialPose = new Pose2d(8, -62, Math.toRadians(90));
        robot.driveTrain.getDrive().localizer.setPose(initialPose);
        waitForStart();

        if (isStopRequested()) return; //

        // Drive forwards 10 inches, extend collection, pass block through
        Actions.runBlocking(
                robot.driveTrain.getDrive().actionBuilder(initialPose)
                        .splineToConstantHeading(new Vector2d(5, -32), 90)
                        .stopAndAdd(robot.moveToScore()) // Score preloaded specimen
                        .waitSeconds(0.2)
                        .stopAndAdd(robot.scoreSpecimen())
                        .strafeTo(new Vector2d(5, -40))
                        .stopAndAdd(robot.collectFromWall())

                        .splineToConstantHeading(new Vector2d(48, -40), 90)
                        .stopAndAdd(robot.extendCollection()) // Grab first specimen
//                        .stopAndAdd(robot.passthrough())
////                        .splineToConstantHeading(new Vector2d(48, -60), 90)
////                        .stopAndAdd(robot.moveToScore()) // Deposit and grab from observation zone
////                        .splineToConstantHeading(new Vector2d(7, -32), 90)
////                        .stopAndAdd(robot.collectFromWall()) // Score first specimen
                        .build()
        );
    }
}
