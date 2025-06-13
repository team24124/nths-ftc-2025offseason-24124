package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                //following code is prototype 4 cycle & park autonomous. 5 cycles is possible.
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-19, 62, Math.toRadians(90)))
                        .strafeTo(new Vector2d(-18, 31.5))
                        .strafeTo(new Vector2d(-19, 35))
                        //clipped 1
                        .lineToSplineHeading(new Pose2d(-39, 36, Math.toRadians(220)))
                        .waitSeconds(0.06)
                        .lineToSplineHeading(new Pose2d(-39, 49, Math.toRadians(130)))
                        .lineToSplineHeading(new Pose2d(-49.2, 37, Math.toRadians(220)))
                        .waitSeconds(0.06)
                        .lineToSplineHeading(new Pose2d(-48, 49, Math.toRadians(130)))
                        //pushed back 2
                        .lineToSplineHeading(new Pose2d(-43, 55, Math.toRadians(270)))
                        .lineToSplineHeading(new Pose2d(-43, 63.3, Math.toRadians(270)))
                        //took clip from wall
                        .lineToSplineHeading(new Pose2d(-12, 31.3, Math.toRadians(90)))
                        //clipped # 2
                        .setTangent(Math.toRadians(90))
                        .lineToSplineHeading(new Pose2d(-27, 43, Math.toRadians(270)))
                        .splineToConstantHeading(new Vector2d(-43, 63), Math.toRadians(90)) //changed from 270
                        .lineToSplineHeading(new Pose2d(-27, 48, Math.toRadians(90)))
                        .splineToConstantHeading(new Vector2d(-16.5, 29.5), Math.toRadians(270)) //changed from 90 // these are just the flick the robot does at the end. if its 270 itll go down and up, 90 it will go just down
                        //clipped # 3
                        .setTangent(Math.toRadians(90))
                        .lineToSplineHeading(new Pose2d(-27, 43, Math.toRadians(270)))
                        .splineToConstantHeading(new Vector2d(-43, 62), Math.toRadians(90))
                        .lineToSplineHeading(new Pose2d(-27, 48, Math.toRadians(90)))
                        .splineToConstantHeading(new Vector2d(-22.5, 28), Math.toRadians(270))
                        //clipped # 4
                        .lineToSplineHeading(new Pose2d(-45, 60, Math.toRadians(270)))
                        .waitSeconds(20)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}