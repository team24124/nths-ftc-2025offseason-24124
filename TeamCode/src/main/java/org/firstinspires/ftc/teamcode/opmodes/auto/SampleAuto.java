package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystem.Robot;

public class SampleAuto extends LinearOpMode {
    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry);

        waitForStart();

        if (isStopRequested()) return; //

        // Drive forwards 10 inches, extend collection, pass block through
        Actions.runBlocking(
                new SequentialAction(
                        robot.driveTrain.getDrive().actionBuilder(new Pose2d(0, 0, Math.toRadians(0)))
                                .splineTo(new Vector2d(10, 0), 0)
                                .afterTime(1, robot.extendCollection())
                                .afterTime(1, robot.passthrough())
                                .build()
                )
        );
    }
}
