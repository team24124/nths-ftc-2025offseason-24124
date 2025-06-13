package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "SampleDynamicLL", group="Linear OpMode")
public class SampleDynamicLL extends LinearOpMode {
    Limelight3A limelight;
    private Servo wrist;
    private Servo intake;
    @Override
    public void runOpMode() {
        //limelight initiation & wrist/intake
        wrist = hardwareMap.get(Servo.class, "wrist");
        intake = hardwareMap.get(Servo.class, "intake");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        if (limelight.getStatus().getPipelineIndex() != 4) {
            limelight.pipelineSwitch(4); // Switch to pipeline number 0
        }
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)

        // Wait for game to start
        waitForStart();
        limelight.start();
        // Main loops
        while (opModeIsActive()) { // opmode for dynamic ll
            if (limelight.isRunning()) {
                if (limelight.getLatestResult() != null) {
                    if (limelight.getLatestResult().getPythonOutput() != null) {
                        LLResult result = limelight.getLatestResult();
                        double[] pythonOutputs = result.getPythonOutput();
                        if (pythonOutputs != null && pythonOutputs.length > 0) {
                            telemetry.addData("target", pythonOutputs[0]);
                            telemetry.addData("targetX", pythonOutputs[1]);
                            telemetry.addData("targetY", pythonOutputs[2]);
                            telemetry.addData("Angle", pythonOutputs[3]);
                            telemetry.addData("Area %", pythonOutputs[4]);
                            telemetry.update();
                        }
                    }
                    double[] array = limelight.getLatestResult().getPythonOutput();
                    double angle = array[3];
                    if (angle < 0) {
                        angle = -angle;
                    }
                    telemetry.addData("angle", angle);
                    telemetry.addData("wristpos", wrist.getPosition());
                    telemetry.update();
                    //right tilt is 90 to 180
                    //left tilt is 0 to 90
                    if (array[0] == 1) {
                        if (angle < 87 && wrist.getPosition() < 0.9) {
                            wrist.setPosition(wrist.getPosition() + 0.0005);
                        } else if (angle > 93 && wrist.getPosition() > 0.1){
                            wrist.setPosition(wrist.getPosition() - 0.0005);
                        }
                    } else if (wrist.getPosition() > 0.9 && angle > 90) {
                        wrist.setPosition(0.5);
                    } else if (wrist.getPosition() < 0.1 && angle < 90) {
                        wrist.setPosition(0.5);
                    }
                    //wrist.setPosition(((double) 1 /180) * angle);  //if ll is attached static
                }
            }
        }
    }
}
