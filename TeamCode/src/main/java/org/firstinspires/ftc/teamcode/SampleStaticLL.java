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
    Limelight3A limelight;
    //private Servo wrist;
    private Servo extension_right;
    private Servo extension_left;
    //private Servo intake;
    double kP = 0.004; // Tune this proportional constant
    @Override
    public void runOpMode() {
        //limelight initiation & wrist/intake
        //wrist = hardwareMap.get(Servo.class, "wrist");
        //intake = hardwareMap.get(Servo.class, "intake");
        extension_left = hardwareMap.get(Servo.class, "left_extension");
        extension_right = hardwareMap.get(Servo.class, "right_extension");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        if (limelight.getStatus().getPipelineIndex() != 4) {
            limelight.pipelineSwitch(4); // Switch to pipeline number 0
        }
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        front_left_motor = hardwareMap.get(DcMotor.class, "left_front");
        front_right_motor = hardwareMap.get(DcMotor.class, "right_front");
        back_left_motor = hardwareMap.get(DcMotor.class, "left_back");
        back_right_motor = hardwareMap.get(DcMotor.class, "right_back");


        // Set the motors to run without encoders for free movement (edit for fun or if needed)
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
        extension_left.setDirection(Servo.Direction.REVERSE);

        // Wait for game to start
        waitForStart();
        limelight.start();
        // Main loops
        while (opModeIsActive()) {
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
                        double strafeX = (targetX - 155) * kP;
                        double strafeY = (targetY - 213) * kP;

                        while (targetX < 150 || targetX > 160 || targetY < 208 || targetY > 218) {

                            array = limelight.getLatestResult().getPythonOutput();
                            targetX = array[1];
                            targetY = array[2];
                            strafeX = (targetX - 155) * kP;
                            strafeY = (targetY - 213) * kP;

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


                            if (targetX > 150 && targetX < 160 && targetY > 208 && targetY < 218) {
                                extension_left.setPosition(0.66);
                                extension_right.setPosition(0.66);
                                if (angle < 0) {
                                    angle = -angle;
                                }
                                //telemetry.addData("wristpos", wrist.getPosition());
                                //telemetry.update();
                                //right tilt is 90 to 180
                                //left tilt is 0 to 90
                                //wrist.setPosition(((double) 1 /180) * angle);  //if ll is attached static

                                //SET WRIST TO DOWN POSITION
                                //SET CLAW TO CLOSED
                                //SET WRIST TO UP POSITION
                                //BRING SLIDE BACK
                                break;
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
