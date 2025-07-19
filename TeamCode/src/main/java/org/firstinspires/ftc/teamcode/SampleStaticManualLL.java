package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "SampleStaticManualLL", group="Linear OpMode")
public class SampleStaticManualLL extends LinearOpMode {
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
    double kP = 0.0022; //add to teleop
    boolean toggleSearch = false; //add to teleop
    double x, y, r;
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
        if (limelight.getStatus().getPipelineIndex() != 2) {
            limelight.pipelineSwitch(2); // Switch to pipeline number 0
        }
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        //add limelight stuff above as well
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
        limelight.start(); //add this too
        // Main loops
        while (opModeIsActive()) {
            if (gamepad1.x) {
                bottom_pivot.setPosition(bottom_pivot.getPosition() + 0.002);
            }
            if (gamepad1.y) {
                bottom_pivot.setPosition(bottom_pivot.getPosition() - 0.002);
            }
            if (gamepad1.b) {
                bottom_claw.setPosition(1);
                bottom_pivot.setPosition(0.5);
                left_bottom_elbow.setPosition(0.5);
            }
            if (gamepad1.leftBumperWasPressed()) {
                left_bottom_elbow.setPosition(0);
                left_extension.setPosition(0.5);
                right_extension.setPosition(0.5);
            }
            if (gamepad1.left_stick_y > 0.2) {
                y = 1;
            } else if (gamepad1.left_stick_y < -0.2) {
                y = -1;
            } else if (gamepad1.right_stick_y > -0.2 && gamepad1.right_stick_y < 0.2 && gamepad1.left_stick_y < 0.2 && gamepad1.left_stick_y > -0.2 && gamepad1.left_stick_x < 0.2 && gamepad1.left_stick_x > -0.2) {
                y = 0;
            }
            if (gamepad1.left_stick_x > 0.2) {
                x = 1;
            } else if (gamepad1.left_stick_x < -0.2) {
                x = -1;
            } else if (gamepad1.right_stick_y > -0.2 && gamepad1.right_stick_y < 0.2 && gamepad1.left_stick_y < 0.2 && gamepad1.left_stick_y > -0.2 && gamepad1.left_stick_x < 0.2 && gamepad1.left_stick_x > -0.2) {
                x = 0;
            }
            if (gamepad1.right_stick_y > 0.2) {
                r = 1;
            } else if (gamepad1.right_stick_y < -0.2) {
                r = -1;
            } else if (gamepad1.right_stick_y > -0.2 && gamepad1.right_stick_y < 0.2 && gamepad1.left_stick_y < 0.2 && gamepad1.left_stick_y > -0.2 && gamepad1.left_stick_x < 0.2 && gamepad1.left_stick_x > -0.2) {
                r = 0;
            }
            double ofrontLeftPower = y - x + r;
            double ofrontRightPower = y + x - r;
            double obackLeftPower = y + x + r;
            double obackRightPower = y - x - r;
            front_left_motor.setPower(ofrontLeftPower);
            front_right_motor.setPower(ofrontRightPower);
            back_left_motor.setPower(obackLeftPower);
            back_right_motor.setPower(obackRightPower);

            if (limelight.isRunning() && limelight.getLatestResult() != null) { //everything in this if statement goes into teleop
                if (limelight.getLatestResult().getPythonOutput() != null) {
                    LLResult result = limelight.getLatestResult();
                    double[] pythonOutputs = result.getPythonOutput();
                    if (pythonOutputs != null && pythonOutputs.length > 0) {
                        telemetry.addData("valid target", pythonOutputs[0]);
                        telemetry.addData("targetX", pythonOutputs[1]);
                        telemetry.addData("targetY", pythonOutputs[2]);
                        telemetry.addData("angle", pythonOutputs[3]);
                        telemetry.addData("area %", pythonOutputs[4]);
                        telemetry.addData("searching ", toggleSearch);
                        telemetry.update();
                    }
                }

                if (gamepad1.aWasPressed()) toggleSearch = !toggleSearch;

                if (limelight.getLatestResult().getPythonOutput() != null && limelight.getLatestResult().getPythonOutput()[0] == 1 && toggleSearch) {
                    double[] array = limelight.getLatestResult().getPythonOutput();
                    double targetX = array[1];
                    double targetY = array[2];
                    double angle = array[3];
                    double strafeX = (targetX - 471) * kP;
                    double strafeY = (targetY - 270) * kP;
                    while (targetX < 466 || targetX > 476 || targetY < 265 || targetY > 275) {
                        array = limelight.getLatestResult().getPythonOutput();
                        targetX = array[1];
                        targetY = array[2];
                        angle = array[3];
                        strafeX = (targetX - 471) * kP;
                        strafeY = (targetY - 270) * kP;

                        if (array[0] == 0) break;

                        double baseX = 0;
                        double baseY = 0;

                        if (strafeX < 0) baseX = -0.1;
                        else if (strafeX > 0) baseX = 0.1;

                        if (strafeY < 0) baseY = -0.1;
                        else if (strafeY > 0) baseY = 0.1;

                        // Calculate the power for each wheel
                        double frontLeftPower = -strafeY + strafeX + baseX - baseY;
                        double frontRightPower = strafeY - strafeX - baseX - baseY;
                        double backLeftPower = -strafeY - strafeX - baseX - baseY;
                        double backRightPower = -strafeY + strafeX + baseX - baseY;
                        // Set the motor powers
                        front_left_motor.setPower(frontLeftPower);
                        front_right_motor.setPower(frontRightPower);
                        back_left_motor.setPower(backLeftPower);
                        back_right_motor.setPower(backRightPower);

                        if (gamepad1.right_bumper) {
                            toggleSearch = false;
                            front_left_motor.setPower(0);
                            front_right_motor.setPower(0);
                            back_left_motor.setPower(0);
                            back_right_motor.setPower(0);
                            break;
                        }

                        if (targetX > 466 && targetX < 476 && targetY > 265 && targetY < 275) { //change servo movements here to what you want
                            front_left_motor.setPower(0);
                            front_right_motor.setPower(0);
                            back_left_motor.setPower(0);
                            back_right_motor.setPower(0); //keep motor powers

                            left_extension.setPosition(0.8);
                            right_extension.setPosition(0.8);
                            left_bottom_elbow.setPosition(0.88);
                            //right tilted block is 90 to 180:  //
                            //left tilted block is 0 to 90:  \\
                            bottom_pivot.setPosition(   0.82 - (((double) 0.67/180) * angle)  );  //if ll is attached static
                            //bottom pivot positions are 0.15 0.5 0.82 from left to right  | _ |
                            sleep(1800);
                            bottom_claw.setPosition(0.6);
                            sleep(500);
                            left_bottom_elbow.setPosition(0.65);

                            toggleSearch = !toggleSearch;
                            break;
                        }
                    }
                }
            }
        }
    }
}
