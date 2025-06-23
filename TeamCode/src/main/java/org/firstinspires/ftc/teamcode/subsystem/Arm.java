package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryObservable;

public class Arm implements Subsystem, TelemetryObservable {
    private final DcMotorEx armMotor;

    // TODO: Tune these positions
    public enum State {
        HOME(0),
        PASSTHROUGH(300),
        ACTIVE(600);

        public final int position;

        State(int position) {
            this.position = position;
        }
    }

    private State current;

    public Arm(HardwareMap hw){
        armMotor = hw.get(DcMotorEx.class, "arm");
        armMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        current = State.HOME;
    }

    /**
     * Move the arm to a given target position as a RoadRunner action.
     *
     * @param target Target position in encoder ticks for the slides to travel to
     * @return A RoadRunner Action
     */
    public Action moveTo(int target) {
        return (TelemetryPacket packet) -> {
            armMotor.setTargetPosition(target);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(0.5);

            return false;
        };
    }

    /**
     * Move the arm to a given target position as a RoadRunner action.
     *
     * @param state Target state to travel to
     * @return A RoadRunner Action
     */
    public Action moveTo(State state) {
        return (TelemetryPacket packet) -> {
            int target = state.position;
            armMotor.setTargetPosition(target);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(0.5);
            current = state;

            return false;
        };
    }

    /**
     * Stop and reset all used motors. Sets motor's RunMode to RUN_USING_ENCODER after.
     */
    public void stopAndResetEncoders() {
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public State getCurrentState(){
        return current;
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("Current State", getCurrentState());
        telemetry.addData("Est. Motor Position", armMotor.getCurrentPosition());
    }

    @Override
    public String getName() {
        return "Arm";
    }
}
