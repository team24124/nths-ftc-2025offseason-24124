package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class ExtensionTest extends OpMode {

    private Servo left_extension;
    private Servo right_extension;

    @Override
    public void init() {
        left_extension = hardwareMap.get(Servo.class, "left_extension");
        right_extension = hardwareMap.get(Servo.class, "right_extension");

        left_extension.setDirection(Servo.Direction.REVERSE);
        right_extension.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void loop() {
        if(gamepad1.aWasPressed()){
            left_extension.setPosition(1);
            right_extension.setPosition(1);
        }
    }
}
