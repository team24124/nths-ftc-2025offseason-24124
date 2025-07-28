package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystem.Arm;
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
                        .afterTime(0, robot.slides.moveTo(8500))
                        .waitSeconds(1)
                        .afterTime(0.5, robot.arm.moveTo(Arm.State.WALL))
                        .splineToLinearHeading(new Pose2d(-4, -58, Math.toRadians(270)), Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(-4, -32), Math.toRadians(90))
                        .stopAndAdd(robot.slides.moveTo(4000))
                        .waitSeconds(3)
                        .splineToConstantHeading(new Vector2d(-4, -38), Math.toRadians(270))
                        .afterTime(0, robot.slides.moveTo(Slides.State.HOME.position))
                        .splineToConstantHeading(new Vector2d(64, -60), Math.toRadians(0))
                        .build()
        );
    }
}
