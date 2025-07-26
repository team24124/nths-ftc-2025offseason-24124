package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(-32, -62, Math.toRadians(90)))
                        .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(45)), Math.toRadians(225))
                        .waitSeconds(4)

                        // Grab Sample
                        .splineToLinearHeading(new Pose2d(-48, -44, Math.toRadians(90)), Math.toRadians(90))
                        .waitSeconds(2)

                        // Deposit in High Bucket
                        .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(45)), Math.toRadians(225))
                        .waitSeconds(4)

                        // Grab Sample
                        .splineToLinearHeading(new Pose2d(-52, -44, Math.toRadians(90)), Math.toRadians(90))
                        .waitSeconds(2)

                        // Deposit in High Bucket
                        .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(45)), Math.toRadians(225))
                        .waitSeconds(4)
                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}