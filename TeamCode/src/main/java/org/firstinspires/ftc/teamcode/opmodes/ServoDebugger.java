package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

@TeleOp(name = "Servo Debugger", group = "Utility")
@Config
public class ServoDebugger extends OpMode {
    public static String servoStr = "";
    public static double position = 0.5;
    private final ArrayList<Servo> servos = new ArrayList<>();

    @Override
    public void init() {
        telemetry.addLine("Enter comma-seperated servo names into the servoStr field.");
        telemetry.addLine("Start names with ! to reverse the servo's direction.");
        telemetry.addLine("Eg. \"servo_one,!servo_two\"");
        telemetry.addLine(); // Line break

        String[] servoNames = servoStr.replaceAll("\\s+", "").split(",");

        try {
            for(String servoName : servoNames){
                telemetry.addLine("Adding " + servoName);
                if(servoName.charAt(0) == '!'){
                    servoName = servoName.substring(1);
                    Servo servo = hardwareMap.get(Servo.class, servoName);
                    telemetry.addLine("Reversing direction of " + servoName);
                    servo.setDirection(Servo.Direction.REVERSE);
                    servos.add(servo);
                }else {
                    servos.add(hardwareMap.get(Servo.class, servoName));
                }
            }
        } catch (IllegalArgumentException e) {
            telemetry.addLine("One or more servo names could not be found in the hardware configuration.");
        }

        telemetry.addLine();
        telemetry.addLine(servos.size() + " servos detected.");
    }

    @Override
    public void loop() {
        for (Servo servo : servos) {
            servo.setPosition(position);

            telemetry.addLine(servo.getDeviceName() + " on port " + servo.getPortNumber());
            telemetry.addLine(servo.getConnectionInfo());
            telemetry.addData("Position", servo.getPosition());
            telemetry.addData("Direction", servo.getDirection());
            telemetry.addLine();
        }
    }
}
