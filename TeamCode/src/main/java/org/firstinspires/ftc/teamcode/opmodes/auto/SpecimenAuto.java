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
                        .afterTime(0.15, robot.moveToScore()) // Score preloaded specimen
                        .splineToConstantHeading(new Vector2d(-6, -30), 90)
                        .stopAndAdd(robot.scoreSpecimen())
                        .splineToConstantHeading(new Vector2d(-6, -36), 270)
                        .splineToConstantHeading(new Vector2d(42, -49), 0)
                        .stopAndAdd(robot.collectFromWall())
                        .waitSeconds(0.1)
                        .stopAndAdd(robot.extendCollection()) // Grab first specimen
                        .waitSeconds(2)
                        .stopAndAdd(robot.passthroughNoRotate())
                        .waitSeconds(2)
                        .stopAndAdd(robot.controlClaw.setClawPosition(ControlClaw.ClawState.OPEN))
                        .splineToConstantHeading(new Vector2d(42, -49), 90)
////                        .stopAndAdd(robot.moveToScore()) // Deposit and grab from observation zone
////                        .splineToConstantHeading(new Vector2d(7, -32), 90)
////                        .stopAndAdd(robot.collectFromWall()) // Score first specimen
                        .build()
        );
    }
}
