package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.selections.ArraySelect;
import org.firstinspires.ftc.teamcode.utility.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryObservable;

public class ControlClaw implements Subsystem, TelemetryObservable {
    private final Servo pivot, claw;
    private final Servo leftElbow, rightElbow;

    public enum ClawState { // Accepts values between 0.5 and 1.0
        CLOSED(0.3),
        OPEN(0.69);

        public final double position;

        ClawState(double position) {
            this.position = position;
        }
    }

    public enum PivotState {
        THIRTY(0),
        SIXTY(0.1),
        NINETY(0.2),
        ONETWENTY(0.3),
        ONEFIFTY(0.4),
        ONEEIGHTY(0.5),
        TWOTEN(0.6),
        TWOFORTY(0.7),
        TWOSEVENTY(0.8),
        THREEHUNDRED(1);

        public final double position;

        PivotState(double position) {
            this.position = position;
        }
    }

    public enum ElbowState {
        PASSTHROUGH(0.12),
        SCORE(0.6),
        CLIP(0.7),
        BUCKET(0.8),
        ACTIVE(0.4);

        public final double position;

        ElbowState(double position) {
            this.position = position;
        }
    }

    private final ArraySelect<PivotState> pivotStates = new ArraySelect<>(PivotState.values());
    private boolean isClawOpen = false;


    public ControlClaw(HardwareMap hw){
        // Pinching Claw
        claw = hw.get(Servo.class, "top_claw");

        // Rotational Pivot
        pivot = hw.get(Servo.class, "top_pivot");

        // Rotational Elbow
        leftElbow = hw.get(Servo.class, "left_top_elbow");
        rightElbow = hw.get(Servo.class, "right_top_elbow");

        leftElbow.setDirection(Servo.Direction.REVERSE);
        rightElbow.setDirection(Servo.Direction.FORWARD);

        setElbowPositions(ElbowState.PASSTHROUGH.position);
        claw.setPosition(ClawState.CLOSED.position);
        isClawOpen = false;
        pivot.setPosition(PivotState.ONEEIGHTY.position);
        pivotStates.setSelected(5); // Set the selected pivot state to the one at the fifth index
    }

    public Action setPivotPosition(PivotState state){
        return (TelemetryPacket packet) -> {
            pivot.setPosition(state.position);
            return false;
        };
    }

    public Action nextPivot(){
        return (TelemetryPacket packet) -> {
            pivotStates.next(); // Advance the pivot to the next rotation
            pivot.setPosition(pivotStates.getSelected().position); // Set the position of the servo
            return false;
        };
    }

    public Action prevPivot(){
        return (TelemetryPacket packet) -> {
            pivotStates.previous(); // Advance the pivot to the next rotation
            pivot.setPosition(pivotStates.getSelected().position); // Set the position of the servo
            return false;
        };
    }

    public Action setClawPosition(ClawState state){
        return (TelemetryPacket packet) -> {
            if(state == ClawState.OPEN) isClawOpen = true;
            else if(state == ClawState.CLOSED) isClawOpen = false;
            claw.setPosition(state.position);
            return false;
        };
    }

    public Action setClawPosition(double position){
        return (TelemetryPacket packet) -> {
            claw.setPosition(position);
            return false;
        };
    }

    public Action toggleClaw(){
        return (TelemetryPacket packet) -> {
            if(isClawOpen) {
                claw.setPosition(ClawState.CLOSED.position);
                isClawOpen = false;
            }else {
                claw.setPosition(ClawState.OPEN.position);
                isClawOpen = true;
            }
            return false;
        };
    }

    public Action setElbowPosition(double position) {
        return (TelemetryPacket packet) -> {
            setElbowPositions(position);
            return false;
        };
    }

    public Action setElbowPosition(ElbowState state) {
        return (TelemetryPacket packet) -> {
            setElbowPositions(state.position);
            return false;
        };
    }

    public boolean isCollecting(){
        return leftElbow.getPosition() == ElbowState.ACTIVE.position;
    }

    public void setElbowPositions(double position){
        leftElbow.setPosition(position);
        rightElbow.setPosition(position);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("Claw Position", claw.getPosition());
        telemetry.addData("Claw Is Open: ", isClawOpen);
        telemetry.addData("Pivot Position", pivot.getPosition());
        telemetry.addData("Selected Position", pivotStates.getSelected());
        telemetry.addData("Elbow Position", leftElbow.getPosition());
    }

    @Override
    public String getName() {
        return "Control Claw";
    }
}
