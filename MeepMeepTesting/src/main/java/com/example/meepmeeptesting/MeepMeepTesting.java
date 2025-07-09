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
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-12, 62, Math.toRadians(270)))
                        .strafeTo(new Vector2d(-12, 31.5))
                        .strafeTo(new Vector2d(-12, 35))
                        //clipped 1
                        .lineToSplineHeading(new Pose2d(-36, 39, Math.toRadians(220)))
                        .waitSeconds(0.06)
                        .lineToSplineHeading(new Pose2d(-36, 50, Math.toRadians(130)))
                        .lineToSplineHeading(new Pose2d(-46, 39, Math.toRadians(220)))
                        .waitSeconds(0.06)
                        .lineToSplineHeading(new Pose2d(-45, 50, Math.toRadians(130)))
                        .lineToSplineHeading(new Pose2d(-56, 39, Math.toRadians(220)))
                        .waitSeconds(0.06)
                        .lineToSplineHeading(new Pose2d(-52, 49, Math.toRadians(130)))
                        //pushed back 2
                        .lineToSplineHeading(new Pose2d(-43, 63.3, Math.toRadians(90)))
                        //took clip from wall
                        .lineToSplineHeading(new Pose2d(-11, 38, Math.toRadians(90)))
                        //clipped # 2
                        .setTangent(Math.toRadians(90))
                        .lineToSplineHeading(new Pose2d(-27, 43, Math.toRadians(90)))
                        .splineToConstantHeading(new Vector2d(-40, 63), Math.toRadians(90))
                        .lineToSplineHeading(new Pose2d(-27, 48, Math.toRadians(90)))
                        .splineToConstantHeading(new Vector2d(-10, 38), Math.toRadians(270)) //changed from 90 // these are just the flick the robot does at the end. if its 270 itll go down and up, 90 it will go just down
                        //clipped # 3
                        .setTangent(Math.toRadians(90))
                        .lineToSplineHeading(new Pose2d(-27, 43, Math.toRadians(90)))
                        .splineToConstantHeading(new Vector2d(-40, 62), Math.toRadians(90))
                        .lineToSplineHeading(new Pose2d(-27, 48, Math.toRadians(90)))
                        .splineToConstantHeading(new Vector2d(-9, 38), Math.toRadians(270))
                        //clipped # 4
                        .lineToSplineHeading(new Pose2d(-45, 60, Math.toRadians(90)))
                        .waitSeconds(20)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}