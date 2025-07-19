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
                        .afterTime(0, robot.controlClaw.setElbowPosition(ControlClaw.ElbowState.SCORE))
                        .afterTime(0.45, robot.moveToScore()) // Score preloaded specimen
                        .splineToConstantHeading(new Vector2d(-5, -30), 90)
                        .stopAndAdd(robot.scoreSpecimen())
                        .splineToConstantHeading(new Vector2d(-5, -40), 270)
                        .splineToLinearHeading(new Pose2d(new Vector2d(42, -50), Math.toRadians(93)), 0)
                        .stopAndAdd(robot.extendCollection()) // Grab first specimen
                        .waitSeconds(0.5)
                        .stopAndAdd(robot.passthroughNoRotate())
                        .strafeTo(new Vector2d(52, -49))
                        .stopAndAdd(robot.controlClaw.setClawPosition(ControlClaw.ClawState.OPEN))
                        .stopAndAdd(robot.collectFromWall())
                        .waitSeconds(0.1)
                        .stopAndAdd(robot.extendCollection()) // Grab second specimen
                        .waitSeconds(0.5)
                        .stopAndAdd(robot.passthroughNoRotate())
                        .strafeToConstantHeading(new Vector2d(52, -52))
                        .stopAndAdd(robot.controlClaw.setClawPosition(ControlClaw.ClawState.OPEN))
                        .strafeToConstantHeading(new Vector2d(52, -50))
                        .stopAndAdd(robot.collectFromWall())
                        .waitSeconds(0.5)
                        .stopAndAdd(robot.moveToScore())
                        .splineToConstantHeading(new Vector2d(0, -30), 90)
                        .afterTime(0, robot.scoreSpecimen())
                        .strafeToConstantHeading(new Vector2d(40, -50))
                        .stopAndAdd(robot.collectFromWall())
                        .waitSeconds(0.5)
                        .stopAndAdd(robot.moveToScore())
                        .splineToConstantHeading(new Vector2d(4, -30), 90)
                        .stopAndAdd(robot.scoreSpecimen())
                        .waitSeconds(3)

////                        .stopAndAdd(robot.moveToScore()) // Deposit and grab from observation zone
////                        .splineToConstantHeading(new Vector2d(7, -32), 90)
////                        .stopAndAdd(robot.collectFromWall()) // Score first specimen
                        .build()
        );
    }
}
