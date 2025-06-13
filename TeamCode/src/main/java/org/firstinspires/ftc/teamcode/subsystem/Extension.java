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
    public static double leftLimit = 0.5, rightLimit = 0.8;
    private double currentPosition = 0.5;

    public Extension(HardwareMap hw){
        leftExtension = hw.get(Servo.class, "left_extension");
        rightExtension = hw.get(Servo.class, "right_extension");

        leftExtension.setDirection(Servo.Direction.REVERSE);
        rightExtension.setDirection(Servo.Direction.FORWARD);

        leftExtension.setPosition(leftLimit);
        rightExtension.setPosition(leftLimit);
    }

    public Action toggleExtension(){

        return (TelemetryPacket packet) -> {
            if(currentPosition == leftLimit){
                leftExtension.setPosition(rightLimit);
                rightExtension.setPosition(rightLimit);
                currentPosition = rightLimit;
            }else{
                leftExtension.setPosition(leftLimit);
                rightExtension.setPosition(leftLimit);
                currentPosition = leftLimit;
            }

            return false;
        };
    }

    public void setExtension(double position){
        currentPosition = position;
        leftExtension.setPosition(position);
        rightExtension.setPosition(position);
    }


    @Override
    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("Current Position", currentPosition);
    }

    @Override
    public String getName() {
        return "Lift Extension";
    }
}
