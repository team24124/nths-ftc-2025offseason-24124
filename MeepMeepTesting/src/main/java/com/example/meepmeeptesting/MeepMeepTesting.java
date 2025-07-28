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
                myBot.getDrive().actionBuilder(new Pose2d(8, -62, Math.toRadians(90)))
//                        .splineToConstantHeading(new Vector2d(-32, -48), Math.toRadians(90))
//                        .splineToConstantHeading(new Vector2d(-32, 0), Math.toRadians(0))
                        .splineToConstantHeading(new Vector2d( 0, -55), Math.toRadians(0))
                        .splineToLinearHeading(new Pose2d(-4, -50, Math.toRadians(270)), Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(-4, -32), Math.toRadians(90))
                        .waitSeconds(2)
                        .splineToConstantHeading(new Vector2d(-4, -38), Math.toRadians(270))
                        .splineToLinearHeading(new Pose2d(64, -60, Math.toRadians(0)), Math.toRadians(0))
                        //.splineToLinearHeading(new Pose2d(-4, -32, Math.toRadians(90)), Math.toRadians(90))
                        //.waitSeconds(2)
                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}