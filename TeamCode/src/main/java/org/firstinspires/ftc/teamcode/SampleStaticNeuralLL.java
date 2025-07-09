package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

@TeleOp(name = "SampleStaticNeuralLL", group="Linear OpMode")
public class SampleStaticNeuralLL extends LinearOpMode {
    DcMotor front_left_motor;
    DcMotor front_right_motor;
    DcMotor back_left_motor;
    DcMotor back_right_motor;
    private DcMotor arm;
    Limelight3A limelight;
    private Servo left_extension;
    private Servo right_extension;
    private Servo left_bottom_elbow;
    private Servo right_bottom_elbow;
    private Servo bottom_pivot;
    private Servo bottom_claw;
    private Servo top_pivot;
    private Servo top_claw;
    double kP = 0.004; // Tune this proportional constant
    @Override
    public void runOpMode() {
        left_extension = hardwareMap.get(Servo.class, "left_extension");
        right_extension = hardwareMap.get(Servo.class, "right_extension");
        left_bottom_elbow = hardwareMap.get(Servo.class, "left_bottom_elbow");
        right_bottom_elbow = hardwareMap.get(Servo.class, "right_bottom_elbow");
        bottom_pivot = hardwareMap.get(Servo.class, "bottom_pivot");
        bottom_claw = hardwareMap.get(Servo.class, "bottom_claw");
        top_pivot = hardwareMap.get(Servo.class, "top_pivot");
        top_claw = hardwareMap.get(Servo.class, "top_claw");

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        if (limelight.getStatus().getPipelineIndex() != 3) {
            limelight.pipelineSwitch(3); // Switch to pipeline number 0
        }
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        front_left_motor = hardwareMap.get(DcMotor.class, "left_front");
        front_right_motor = hardwareMap.get(DcMotor.class, "right_front");
        back_left_motor = hardwareMap.get(DcMotor.class, "left_back");
        back_right_motor = hardwareMap.get(DcMotor.class, "right_back");
        arm = hardwareMap.get(DcMotor.class, "arm");

        // Set the motors to run without encoders for free movement
        front_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right_motor.setDirection(DcMotorSimple.Direction.REVERSE);
        front_right_motor.setDirection(DcMotorSimple.Direction.REVERSE);
        left_extension.setDirection(Servo.Direction.REVERSE);

        // Wait for game to start
        waitForStart();
        limelight.start();
        // Main loops
        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();
            if (limelight.isRunning()) {
                if (limelight.getLatestResult() != null) {
                    List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();
                    String className = detections.get(0).getClassName();
                    double x = detections.get(0).getTargetXDegrees();
                    double y = detections.get(0).getTargetYDegrees();
                    List<List<Double>> corners = detections.get(0).getTargetCorners();

                    double zeroX = corners.get(0).get(0); //x of 0
                    double zeroY = corners.get(0).get(1); //y of 0
                    double oneX = corners.get(1).get(0); //x of 1
                    double threeY = corners.get(3).get(1); //y of 3

                    double width = zeroX - oneX;

                    double length = zeroY - threeY;

                    double ratio = width / length;

                    telemetry.addData(String.valueOf(ratio), " width: " + width + " length: " + length);
                    telemetry.update();

                    if (gamepad1.b) {
                        if (ratio < 1) {
                            bottom_claw.setPosition(0.2);
                        } else {
                            bottom_claw.setPosition(0.8);
                        }
                    }



//                    for (LLResultTypes.DetectorResult detection : detections) {
//                        className = detection.getClassName(); // What was detected
//                        x = detection.getTargetXDegrees(); // Where it is (left-right)
//                        y = detection.getTargetYDegrees(); // Where it is (up-down)
//                        List<List<Double>> corners = detection.getTargetCorners();
//                        telemetry.addData(className, "at (" + x + ", " + y + ") degrees");
//                        telemetry.addData(className, corners);
//                        telemetry.update();
//                    }

                    //block ratio is 9 to 3.6 cm
                }
            }
        }
    }
}
