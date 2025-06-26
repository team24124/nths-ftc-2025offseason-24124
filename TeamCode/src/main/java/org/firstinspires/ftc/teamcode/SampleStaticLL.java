package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "SampleStaticLL", group="Linear OpMode")
public class SampleStaticLL extends LinearOpMode {
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
        if (limelight.getStatus().getPipelineIndex() != 4) {
            limelight.pipelineSwitch(4); // Switch to pipeline number 0
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
            telemetry.addData("elbowpos", left_bottom_elbow.getPosition());
            telemetry.update();
            if (limelight.isRunning()) {
                if (limelight.getLatestResult() != null) {

                    if (limelight.getLatestResult().getPythonOutput() != null) {
                        LLResult result = limelight.getLatestResult();
                        double[] pythonOutputs = result.getPythonOutput();
                        if (pythonOutputs != null && pythonOutputs.length > 0) {
                            telemetry.addData("target", pythonOutputs[0]);
                            telemetry.addData("targetX", pythonOutputs[1]);
                            telemetry.addData("targetY", pythonOutputs[2]);
                            telemetry.addData("angle", pythonOutputs[3]);
                            telemetry.addData("area %", pythonOutputs[4]);
                            telemetry.update();
                        }
                    }


                    if (gamepad1.a) {
                        double[] array = limelight.getLatestResult().getPythonOutput();
                        double targetX = array[1];
                        double targetY = array[2];
                        double angle = array[3];
                        double strafeX = (targetX - 83) * kP;
                        double strafeY = (targetY - 265) * kP;
                        telemetry.update();

                        while (targetX < 78 || targetX > 88 || targetY < 260 || targetY > 270) {
                            telemetry.update();

                            array = limelight.getLatestResult().getPythonOutput();
                            targetX = array[1];
                            targetY = array[2];
                            angle = array[3];
                            strafeX = (targetX - 83) * kP;
                            strafeY = (targetY - 265) * kP;

                            if (array == null) {
                                break;
                            }

                            double baseX = 0;
                            double baseY = 0;
                            if (strafeX < 0) {
                                baseX = -0.052;
                            } else if (strafeX > 0){
                                baseX = 0.052;
                            }
                            if (strafeY < 0) {
                                baseY = -0.052;
                            } else if (strafeY > 0){
                                baseY = 0.052;
                            }

                            // Calculate the power for each wheel
                            double frontLeftPower = strafeY - strafeX - baseX + baseY;
                            double frontRightPower = strafeY + strafeX + baseX + baseY;
                            double backLeftPower = strafeY + strafeX + baseX + baseY;
                            double backRightPower = strafeY - strafeX - baseX + baseY;
                            // Set the motor powers
                            front_left_motor.setPower(frontLeftPower);
                            front_right_motor.setPower(frontRightPower);
                            back_left_motor.setPower(backLeftPower);
                            back_right_motor.setPower(backRightPower);


                            if (targetX > 78 && targetX < 88 && targetY > 260 && targetY < 270) {
                                front_left_motor.setPower(0);
                                front_right_motor.setPower(0);
                                back_left_motor.setPower(0);
                                back_right_motor.setPower(0);
                                left_extension.setPosition(0.662);
                                right_extension.setPosition(0.662);
                                if (angle < 0) {
                                    angle = -angle;
                                }
                                //right tilt is 90 to 180
                                //left tilt is 0 to 90
                                bottom_pivot.setPosition(((double) 1 /180) * angle);  //if ll is attached static

                                while (true) {
                                    left_bottom_elbow.setPosition(0.14);
                                    right_bottom_elbow.setPosition(0.14);
                                    bottom_claw.setPosition(0.6);
                                    if (left_bottom_elbow.getPosition() < 0.16) {
                                        bottom_claw.setPosition(0.85);
                                        sleep(50);
                                        left_bottom_elbow.setPosition(0.83);
                                        right_bottom_elbow.setPosition(0.83);
                                        bottom_pivot.setPosition(0.14);
                                        left_extension.setPosition(0.5);
                                        right_extension.setPosition(0.5);
                                        break;
                                    }
                                }
                                top_pivot.setPosition(1);
                            }
                        }
                        front_left_motor.setPower(0);
                        front_right_motor.setPower(0);
                        back_left_motor.setPower(0);
                        back_right_motor.setPower(0);
                    }
                }
            }
        }
    }
}
