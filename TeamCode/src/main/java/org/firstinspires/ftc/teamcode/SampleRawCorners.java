package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "SampleRawCorners", group="Linear OpMode")
public class SampleRawCorners extends LinearOpMode {
    private Servo intake;
    private Servo wrist;
    Limelight3A limelight;
    @Override
    public void runOpMode() {
        //initialize hardware map
        wrist = hardwareMap.get(Servo.class, "wrist");
        intake = hardwareMap.get(Servo.class, "intake");

        //limelight initiation
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(3); // Switch to pipeline number 0

        // Wait for game to start
        waitForStart();
        // Main loops
        while (opModeIsActive()) { // opmode is a demo
            if(limelight.getLatestResult() != null && limelight.getLatestResult().isValid()) {
                LLResultTypes.ColorResult c = limelight.getLatestResult().getColorResults().get(0);
                if (!limelight.getLatestResult().getColorResults().isEmpty()) {
                    telemetry.addData("corner 0", c.getTargetCorners().get(0));
                    telemetry.addData("corner 1", c.getTargetCorners().get(1));
                    telemetry.update();
                    wrist.setPosition(c.getTargetCorners().get(0).get(0) / 1000);
                }
                //if on right larger x
                //if on bottom larger y
            }
        }
    }
}
