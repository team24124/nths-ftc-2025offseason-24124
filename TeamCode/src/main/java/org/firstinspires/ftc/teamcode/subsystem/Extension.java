package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.dashboard.config.Config;
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
    public static double leftLimit = 0, rightLimit = 1;
    private double currentPosition;

    public Extension(HardwareMap hw){
        leftExtension = hw.get(Servo.class, "left_extension");
        rightExtension = hw.get(Servo.class, "right_extension");

        leftExtension.setDirection(Servo.Direction.REVERSE);
        rightExtension.setDirection(Servo.Direction.FORWARD);

        //leftExtension.scaleRange(leftLimit, rightLimit);
        //rightExtension.scaleRange(leftLimit, rightLimit);
    }

    public Action toggleExtension(){
        return new InstantAction(() -> {
            if(currentPosition != leftLimit){
                setExtension(rightLimit);
            }else{
                setExtension(leftLimit);
            }
        });
    }

    public void setExtension(double position){
        currentPosition = position;
        leftExtension.setPosition(position);
        rightExtension.setPosition(position);
    }


    @Override
    public void updateTelemetry(Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "Lift Extension";
    }
}
