package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryMaster;

public class Robot {
    public Arm arm;
    public CollectionClaw collectionClaw;
    public ControlClaw controlClaw;
    public Extension extension;
    public Slides slides;
    public DriveTrain driveTrain;

    public TelemetryMaster telemetryMaster;

    private boolean isExtended = false;
    private boolean isInScoringMode = false;

    public Robot(HardwareMap hw, Telemetry telemetry) {
        arm = new Arm(hw);
        collectionClaw = new CollectionClaw(hw);
        controlClaw = new ControlClaw(hw);
        extension = new Extension(hw);
        slides = new Slides(hw);

        Pose2d startPose = new Pose2d(new Vector2d(0, 0), Math.toRadians(0));
        driveTrain = new FieldCentricDriveTrain(hw, startPose);

        telemetryMaster = new TelemetryMaster(telemetry);
        telemetryMaster
                .subscribe(arm)
                .subscribe(collectionClaw)
                .subscribe(controlClaw)
                .subscribe(extension)
                .subscribe(slides)
                .subscribe(driveTrain);
    }

    public boolean isExtended(){
        return isExtended;
    }

    public boolean isInScoringMode(){
        return isInScoringMode;
    }

    public Action extendCollection(){
        isExtended = true;
        return new SequentialAction(
                collectionClaw.setClawPosition(CollectionClaw.ClawState.OPEN),
                new ParallelAction(
                        extension.extendTo(Extension.State.EXTENDED.position),
                        collectionClaw.setElbowPosition(CollectionClaw.ElbowState.ACTIVE)

                ),
                collectionClaw.setPivotPosition(CollectionClaw.PivotState.ONEEIGHTY)
        );
    }

    public Action retractCollection(){
        isExtended = false;
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

    public Action retractCollectionNoRotate(){
        isExtended = false;
        return new SequentialAction(
                collectionClaw.setClawPosition(CollectionClaw.ClawState.CLOSED),
                new SleepAction(0.1),
                new ParallelAction(
                        extension.extendTo(Extension.State.RETRACTED.position),
                        collectionClaw.setElbowPosition(CollectionClaw.ElbowState.PASSTHROUGH),
                        collectionClaw.setPivotPosition(CollectionClaw.PivotState.ONEEIGHTY)
                )
        );
    }

    public Action collectFromPassthrough(){
        isInScoringMode = false;
        return new SequentialAction(
                controlClaw.setPivotPosition(ControlClaw.PivotState.TWOSEVENTY),
                controlClaw.setClawPosition(ControlClaw.ClawState.OPEN),
                slides.setStateTo(Slides.State.PASSTHROUGH),
                arm.moveTo(Arm.State.PASSTHROUGH),
                controlClaw.setElbowPosition(ControlClaw.ElbowState.PASSTHROUGH),
                new SleepAction(0.3),
                controlClaw.setClawPosition(ControlClaw.ClawState.CLOSED)

        );
    }

    public Action collectFromPassthroughNoRotate(){
        isInScoringMode = false;
        return new SequentialAction(
                controlClaw.setPivotPosition(ControlClaw.PivotState.ONEEIGHTY),
                controlClaw.setClawPosition(ControlClaw.ClawState.OPEN),
                slides.setStateTo(Slides.State.PASSTHROUGH),
                arm.moveTo(Arm.State.PASSTHROUGH),
                controlClaw.setElbowPosition(ControlClaw.ElbowState.PASSTHROUGH),
                new SleepAction(0.3),
                controlClaw.setClawPosition(ControlClaw.ClawState.CLOSED)

        );
    }

    public Action collectFromWall(){
        isInScoringMode = false;
        return new SequentialAction(
                collectionClaw.setClawPosition(CollectionClaw.ClawState.OPEN),
                new SleepAction(0.2),
                controlClaw.setElbowPosition(ControlClaw.ElbowState.ACTIVE),
                arm.moveTo(Arm.State.ACTIVE),
                new SleepAction(0.1),
                controlClaw.setClawPosition(ControlClaw.ClawState.CLOSED),
                controlClaw.setPivotPosition(ControlClaw.PivotState.ONEEIGHTY)
        );
    }

    public Action passthrough(){
        return new SequentialAction(
                new ParallelAction(
                        retractCollection(),
                        resetControlArm()
                ),
                new SleepAction(0.8),
                collectFromPassthrough(),
                new SleepAction(0.4),
                collectFromWall()
        );
    }
    public Action passthroughNoRotate(){
        return new SequentialAction(
                new ParallelAction(
                        retractCollectionNoRotate(),
                        resetControlArm()
                ),
                controlClaw.setPivotPosition(ControlClaw.PivotState.ONEEIGHTY),
                slides.moveTo(Slides.State.PASSTHROUGH.position+500),
                new SleepAction(0.8),
                collectFromPassthroughNoRotate(),
                new SleepAction(0.4),
                collectFromWall()
        );
    }

    public Action moveToScore(){
        isInScoringMode = true;
        return new SequentialAction(
                controlClaw.setPivotPosition(ControlClaw.PivotState.ONEEIGHTY),
                controlClaw.setClawPosition(ControlClaw.ClawState.CLOSED),
                controlClaw.setElbowPosition(ControlClaw.ElbowState.SCORE),
                arm.moveTo(Arm.State.SCORE),
                slides.setStateTo(Slides.State.HIGH_RUNG)
        );
    }

    public Action scoreSpecimen(){
        isInScoringMode = false;
        return new SequentialAction(
                controlClaw.setPivotPosition(ControlClaw.PivotState.ONEEIGHTY),
                slides.setStateTo(Slides.State.CLIP_HIGH_CHAMBER),
                arm.moveTo(Arm.State.ACTIVE),
                controlClaw.setElbowPosition(ControlClaw.ElbowState.ACTIVE),
                new SleepAction(1),
                controlClaw.setClawPosition(ControlClaw.ClawState.OPEN)
        );
    }

    public Action resetControlArm(){
        return new SequentialAction(
                arm.moveTo(Arm.State.HOME),
                slides.setStateTo(Slides.State.PASSTHROUGH),
                controlClaw.setClawPosition(ControlClaw.ClawState.OPEN),
                controlClaw.setElbowPosition(ControlClaw.ElbowState.PASSTHROUGH),
                controlClaw.setPivotPosition(ControlClaw.PivotState.TWOSEVENTY)
        );
    }
}
