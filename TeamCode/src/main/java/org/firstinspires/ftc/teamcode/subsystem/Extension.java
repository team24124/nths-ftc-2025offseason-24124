package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryObservable;

@Config
public class Extension implements Subsystem, TelemetryObservable {
    private final Servo leftExtension, rightExtension;

    public enum State {
        RETRACTED(0.48),
        EXTENDED(0.8);

        public final double position;

        State(double position){
            this.position = position;
        }
    }

    private boolean isExtended = false;

    public Extension(HardwareMap hw){
        leftExtension = hw.get(Servo.class, "left_extension");
        rightExtension = hw.get(Servo.class, "right_extension");

        leftExtension.setDirection(Servo.Direction.REVERSE);
        rightExtension.setDirection(Servo.Direction.FORWARD);

        setExtension(State.RETRACTED);
    }

    public Action toggleExtension(){
        return (TelemetryPacket packet) -> {
            if(isExtended){
                setExtension(State.RETRACTED);
                isExtended = false;
            }
            else {
                setExtension(State.EXTENDED);
                isExtended = true;
            }
            return false;
        };
    }

    public Action extendTo(double position){
        return (TelemetryPacket packet) -> {
            setExtension(position);
            return false;
        };
    }

    public void setExtension(State state){
        leftExtension.setPosition(state.position);
        rightExtension.setPosition(state.position);
    }

    public void setExtension(double position){
        leftExtension.setPosition(position);
        rightExtension.setPosition(position);
    }


    @Override
    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("Current Position", leftExtension.getPosition());
    }

    @Override
    public String getName() {
        return "Lift Extension";
    }
}
