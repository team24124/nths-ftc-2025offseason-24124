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
                        .splineToConstantHeading(new Vector2d(-5, -36), 270)
                        .splineToLinearHeading(new Pose2d(new Vector2d(42, -49), Math.toRadians(93)), 0)
                        .stopAndAdd(robot.collectFromWall())
                        .waitSeconds(0.1)
                        .stopAndAdd(robot.extendCollection()) // Grab first specimen
                        .waitSeconds(1)
                        .stopAndAdd(robot.passthroughNoRotate())
                        .strafeTo(new Vector2d(50, -47))
                        .stopAndAdd(robot.controlClaw.setClawPosition(ControlClaw.ClawState.OPEN))
                        .stopAndAdd(robot.collectFromWall())
                        .waitSeconds(0.1)
                        .stopAndAdd(robot.extendCollection()) // Grab first specimen
                        .waitSeconds(1)
                        .stopAndAdd(robot.passthroughNoRotate())
                        .stopAndAdd(robot.controlClaw.setClawPosition(ControlClaw.ClawState.OPEN))
                        .strafeToConstantHeading(new Vector2d(50, -52))
                        .stopAndAdd(robot.slides.setStateTo(Slides.State.CLIPPER))
                        .stopAndAdd(robot.collectFromWall())
                        .waitSeconds(3)
                        .stopAndAdd(robot.moveToScore())

////                        .stopAndAdd(robot.moveToScore()) // Deposit and grab from observation zone
////                        .splineToConstantHeading(new Vector2d(7, -32), 90)
////                        .stopAndAdd(robot.collectFromWall()) // Score first specimen
                        .build()
        );
    }
}
