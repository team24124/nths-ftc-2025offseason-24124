package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryMaster;

public class Robot {
    public Arm arm;
    public CollectionClaw collectionClaw;
    public ControlClaw controlClaw;
    public Extension extension;
    public Slides slides;
    public RobotCentricDriveTrain driveTrain;

    public TelemetryMaster telemetryMaster;

    public Robot(HardwareMap hw, Telemetry telemetry) {
        arm = new Arm(hw);
        collectionClaw = new CollectionClaw(hw);
        controlClaw = new ControlClaw(hw);
        extension = new Extension(hw);
        slides = new Slides(hw);

        Pose2d startPose = new Pose2d(new Vector2d(0, 0), Math.toRadians(0));
        driveTrain = new RobotCentricDriveTrain(hw, startPose);

        telemetryMaster = new TelemetryMaster(telemetry);
        telemetryMaster
                .subscribe(arm)
                .subscribe(collectionClaw)
                .subscribe(controlClaw)
                .subscribe(extension)
                .subscribe(slides)
                .subscribe(driveTrain);
    }

    public Action extendCollection(){
        return new SequentialAction(
                collectionClaw.setPivotPosition(CollectionClaw.PivotState.ONEEIGHTY),
                collectionClaw.setClawPosition(CollectionClaw.ClawState.OPEN),
                new ParallelAction(
                        extension.extendTo(Extension.State.EXTENDED.position),
                        collectionClaw.setElbowPosition(CollectionClaw.ElbowState.ACTIVE)

                )
        );
    }

    public Action retractCollection(){
        return new SequentialAction(
                collectionClaw.setClawPosition(CollectionClaw.ClawState.CLOSED),
                new SleepAction(0.1),
                new ParallelAction(
                        extension.extendTo(Extension.State.RETRACTED.position),
                        collectionClaw.setElbowPosition(CollectionClaw.ElbowState.PASSTHROUGH),
                        collectionClaw.setPivotPosition(CollectionClaw.PivotState.NINETY)
                )
        );
    }

    public Action collectControl(){
        return new SequentialAction(
                controlClaw.setPivotPosition(ControlClaw.PivotState.TWOSEVENTY),
                controlClaw.setClawPosition(ControlClaw.ClawState.OPEN),
                slides.moveTo(Slides.State.PASSTHROUGH.position),
                arm.moveTo(Arm.State.PASSTHROUGH),
                controlClaw.setElbowPosition(ControlClaw.ElbowState.PASSTHROUGH),
                new SleepAction(0.3),
                controlClaw.setClawPosition(ControlClaw.ClawState.CLOSED)

        );
    }

    public Action activeControl(){
        return new SequentialAction(
                collectionClaw.setClawPosition(CollectionClaw.ClawState.OPEN),
                new SleepAction(0.2),
                controlClaw.setElbowPosition(ControlClaw.ElbowState.ACTIVE),
                arm.moveTo(Arm.State.ACTIVE),
                new SleepAction(0.1),
                controlClaw.setClawPosition(ControlClaw.ClawState.CLOSED)
        );
    }
}
