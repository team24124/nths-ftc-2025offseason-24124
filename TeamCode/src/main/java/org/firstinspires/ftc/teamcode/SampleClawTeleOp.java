package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "SampleClawTeleOp", group="Linear OpMode")
public class SampleClawTeleOp extends LinearOpMode {
    // the cool motors
    DcMotor front_left_motor;
    DcMotor front_right_motor;
    DcMotor back_left_motor;
    DcMotor back_right_motor;
    private Servo intake;
    private Servo wrist;
    private DcMotor arm_motor;
    private DcMotor arm_long;
    // Speed multiplier
    double speedMultiplier = 0.6; // Default speed 0.6

    // Variables to track previous button states
    boolean previousA = false;
    boolean previousB = false;
    boolean previousY = false;
    boolean previousX = false;
    boolean what = true;
    @Override
    public void runOpMode() {
        wrist = hardwareMap.get(Servo.class, "wrist");
        intake = hardwareMap.get(Servo.class, "intake");
        arm_motor = hardwareMap.get(DcMotor.class, "arm_motor");
        arm_long = hardwareMap.get(DcMotor.class, "arm_long");
        // Initialize the hardware map
        front_left_motor = hardwareMap.get(DcMotor.class, "front_left_motor");
        front_right_motor = hardwareMap.get(DcMotor.class, "front_right_motor");
        back_left_motor = hardwareMap.get(DcMotor.class, "back_left_motor");
        back_right_motor = hardwareMap.get(DcMotor.class, "back_right_motor");


        // Set the motors to run without encoders for free movement (edit for fun or if needed)
        front_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm_long.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left_motor.setDirection(DcMotorSimple.Direction.REVERSE);
        front_left_motor.setDirection(DcMotorSimple.Direction.REVERSE);
        arm_long.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for game to start

        waitForStart();


        // Main loops
        while (opModeIsActive()) {
            if (gamepad2.right_trigger > 0.67 && gamepad2.left_trigger > 0.67) {
                arm_long.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                arm_long.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            if (gamepad2.a){
                wrist.setPosition(wrist.getPosition()-0.02);
            } else if (gamepad2.b) {
                wrist.setPosition(wrist.getPosition()+0.02);
            }
            if (gamepad2.y) {
                intake.setPosition(0.63);
            }
            if (gamepad2.x) {
                intake.setPosition(0.8);
            }
            if (gamepad2.dpad_up) {
                if (arm_motor.getCurrentPosition() < 4474) {
                    arm_motor.setPower(0.9); // lower the arm
                } else {
                    arm_motor.setPower(0.35);
                }
            } else if (gamepad2.dpad_down) {
                if (arm_motor.getCurrentPosition() < 4474) {
                    arm_motor.setPower(-0.9); // extend the arm
                } else {
                    arm_motor.setPower(-0.35);
                }
            }
            if (!gamepad2.dpad_down && !gamepad2.dpad_up && arm_motor.getCurrentPosition() > 1300 && arm_motor.getCurrentPosition() < 3600) {
                arm_motor.setPower(0);
            }
            if (!gamepad2.dpad_down && !gamepad2.dpad_up){
                if (arm_motor.getCurrentPosition() < 1300) {
                    arm_motor.setPower(0.00166);
                } else if (arm_motor.getCurrentPosition() > 3600) {
                    arm_motor.setPower(-0.00166);
                }
            }
            double prevPos = arm_long.getCurrentPosition();
            if (gamepad2.left_bumper && arm_long.getCurrentPosition() < -150) {
                arm_long.setPower(0.9); // lower the arm
            } else if (gamepad2.right_bumper && arm_long.getCurrentPosition() > -2030) {
                arm_long.setPower(-0.9); // extend the  arm
            } else {
                if (arm_long.getCurrentPosition() > prevPos) {
                    arm_long.setPower(-0.2); // holding power when not moving
                } else {
                    arm_long.setPower(-0.0003);
                }
            }

            // Check if LB is pressed, and if it is track the other button press
            if (gamepad1.left_bumper) {
                // Check if LB + A is pressed (and was not pressed last iteration)
                if (gamepad1.a) {
                    speedMultiplier = 0.2; // Set speed to 0.2
                }
                // Check if LB + B is pressed (and was not pressed last iteration)
                else if (gamepad1.b) {
                    speedMultiplier = 0.4; // Set speed to 0.4
                }
                // Check if LB + Y is pressed (and was not pressed last iteration)
                else if (gamepad1.y) {
                    speedMultiplier = 0.6; // Set speed to 0.6
                }
                // Check if LB + X is pressed (and was not pressed last iteration)
                else if (gamepad1.x) {
                    speedMultiplier = 0.8; // Set speed to 0.8
                }
                else if (gamepad1.right_bumper){
                    speedMultiplier = 1.0;
                }
            }


            // the inputs for the gamepad
            double drive1 = -gamepad1.left_stick_y * speedMultiplier;  // Forward/Backward (left stick Y-axis)
            double strafe = gamepad1.left_stick_x * speedMultiplier;  // Strafing (left stick X-axis)
            double turn = gamepad1.right_stick_x * speedMultiplier;   // Turning (right stick X-axis)


            // Calculate the power for each wheel
            double frontLeftPower = drive1 + strafe + turn;
            double frontRightPower = drive1 - strafe - turn;
            double backLeftPower = drive1 - strafe + turn;
            double backRightPower = drive1 + strafe - turn;


            // Normalize the wheel speeds so they dont go 100%
            double maxPower = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower),
                    Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));


            if (maxPower > 1.0) {
                frontLeftPower = maxPower;
                frontRightPower = maxPower;
                backLeftPower = maxPower;
                backRightPower = maxPower;
            }


            // Set the motor powers
            front_left_motor.setPower(frontLeftPower);
            front_right_motor.setPower(frontRightPower);
            back_left_motor.setPower(backLeftPower);
            back_right_motor.setPower(backRightPower);


            // Print values for debugging
            telemetry.addData("FL Power", frontLeftPower);
            telemetry.addData("FR Power", frontRightPower);
            telemetry.addData("BL Power", backLeftPower);
            telemetry.addData("BR Power", backRightPower);
            telemetry.addData("arm pos", arm_motor.getCurrentPosition());
            telemetry.addData("viper pos", arm_long.getCurrentPosition());
            telemetry.addData("Speed Multiplier", speedMultiplier);
            telemetry.addData("wrist pos", wrist.getPosition());
            telemetry.addData("claw pos", intake.getPosition());
            telemetry.update();


            // Infinite loop for hang
            boolean inLoop = true;
            if (gamepad2.left_bumper && gamepad2.right_bumper && gamepad2.y) {
                while (opModeIsActive() && inLoop) {
                    arm_motor.setPower(0.8);
                    arm_long.setPower(0);
                    back_left_motor.setPower(0);
                    back_right_motor.setPower(0);
                    front_right_motor.setPower(0);
                    front_left_motor.setPower(0);


                    // Quit infinite loop LB+RB+a
                    if (gamepad2.left_bumper && gamepad2.right_bumper && gamepad2.a) {
                        inLoop = false;
                    }
                }
            }


            // Store the current state of the buttons for iteration
            previousA = gamepad1.a;
            previousB = gamepad1.b;
            previousY = gamepad1.y;
            previousX = gamepad1.x;
        }
    }
}
