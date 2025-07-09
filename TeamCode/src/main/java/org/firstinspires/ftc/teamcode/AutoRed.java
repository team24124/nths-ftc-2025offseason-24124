package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

// Non-RR imports
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@Autonomous(name = "AutoRed", group = "Autonomous")
public class AutoRed extends LinearOpMode {
    public class servoControl {
        private DcMotor arm, left_slide, right_slide;
        private Servo left_extension, right_extension, left_bottom_elbow, right_bottom_elbow, left_top_elbow, right_top_elbow, bottom_pivot, bottom_claw, top_pivot, top_claw;
        public servoControl(HardwareMap hardwareMap) {
            arm = hardwareMap.get(DcMotor.class, "arm");
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            bottom_claw = hardwareMap.get(Servo.class, "bottom_claw");
            bottom_pivot = hardwareMap.get(Servo.class, "bottom_pivot");
            top_claw = hardwareMap.get(Servo.class, "top_claw");
            top_pivot = hardwareMap.get(Servo.class, "top_pivot");

            left_slide = hardwareMap.get(DcMotor.class, "left_slide");
            left_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            left_slide.setDirection(DcMotorSimple.Direction.REVERSE);
            right_slide = hardwareMap.get(DcMotor.class, "right_slide");
            right_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            left_extension = hardwareMap.get(Servo.class, "left_extension");
            right_extension = hardwareMap.get(Servo.class, "right_extension");
            left_bottom_elbow = hardwareMap.get(Servo.class, "left_bottom_elbow");
            right_bottom_elbow = hardwareMap.get(Servo.class, "right_bottom_elbow");
            left_top_elbow = hardwareMap.get(Servo.class, "left_top_elbow");
            right_top_elbow = hardwareMap.get(Servo.class, "right_top_elbow");
            left_extension.setDirection(Servo.Direction.REVERSE);
            left_top_elbow.setDirection(Servo.Direction.REVERSE);
            left_bottom_elbow.setDirection(Servo.Direction.REVERSE);
        }
        public class clipPos implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    arm.setPower(VALUE);
                    top_claw.setPosition(0.8);
                    left_top_elbow.setPosition(VALUE);
                    right_top_elbow.setPosition(VALUE);
                    top_pivot.setPosition(VALUE);
                    left_slide.setTargetPosition(VALUE);
                    right_slide.setTargetPosition(VALUE);
                    initialized = true;
                }

                double pos = arm.getCurrentPosition();
                packet.put("armPos", pos);
                if (pos < 100) {
                    return true;
                } else {
                    arm.setPower(VALUE);
                    left_slide.setTargetPosition(VALUE);
                    right_slide.setTargetPosition(VALUE);
                    top_claw.setPosition(VALUE);
                    return false;
                }
            }
        }
        public Action clipPos() {
            return new servoControl.clipPos();
        }

        public class extendPos implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    arm.setPower(VALUE);
                    bottom_pivot.setPosition(VALUE);
                    bottom_claw.setPosition(VALUE);
                    left_extension.setPosition(VALUE);
                    right_extension.setPosition(VALUE);
                    left_bottom_elbow.setPosition(VALUE);
                    right_bottom_elbow.setPosition(VALUE);
                    initialized = true;
                }

                double pos = arm.getCurrentPosition();
                packet.put("armPos", pos);
                if (pos < VALUE) {
                    return true;
                } else {
                    arm.setPower(VALUE);
                    return false;
                }
            }
        }
        public Action extendPos() {
            return new servoControl.extendPos();
        }

        public class grabGroundPos implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    left_bottom_elbow.setPosition(VALUE);
                    right_bottom_elbow.setPosition(VALUE);
                    while (left_bottom_elbow.getPosition() != VALUE) {
                        left_bottom_elbow.setPosition(VALUE);
                        right_bottom_elbow.setPosition(VALUE);
                    }
                    bottom_claw.setPosition(VALUE);
                    initialized = true;
                }

                double pos = bottom_claw.getPosition();
                packet.put("armPos", pos);
                if (pos != VALUE) {
                    return true;
                } else {
                    left_bottom_elbow.setPosition(VALUE);
                    right_bottom_elbow.setPosition(VALUE);
                    bottom_pivot.setPosition(VALUE);
                    return false;
                }
            }
        }
        public Action grabGroundPos() {
            return new servoControl.grabGroundPos();
        }

        public class dropPos implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                bottom_claw.setPosition(VALUE);
                while (bottom_claw.getPosition() != VALUE) {
                    bottom_claw.setPosition(VALUE);
                }
                left_bottom_elbow.setPosition(VALUE);
                right_bottom_elbow.setPosition(VALUE);
                left_extension.setPosition(VALUE);
                right_extension.setPosition(VALUE);
                top_pivot.setPosition(VALUE);
                return false;
            }
        }
        public Action dropPos() {
            return new servoControl.dropPos();
        }

        public class takeFromWallPos implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    arm.setPower(VALUE);
                    initialized = true;
                }

                double pos = arm.getCurrentPosition();
                packet.put("armPos", pos);
                if (pos < VALUE) {
                    return true;
                } else {
                    arm.setPower(VALUE);
                    top_claw.setPosition(VALUE);
                    return false;
                }
            }
        }
        public Action takeFromWallPos() {
            return new servoControl.takeFromWallPos();
        }
    }
    @Override
    public void runOpMode() {
        // instantiate your MecanumDrive at a particular pose.
        Pose2d initialPose = new Pose2d(-12, 62, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        // actionBuilder builds from the drive steps passed to it
        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
            .strafeTo(new Vector2d(-12, 31.5))
            .strafeTo(new Vector2d(-12, 35))
            //clipped 1
            .strafeToSplineHeading(new Vector2d(-36, 39), Math.toRadians(220))
            .waitSeconds(0.06)
            .strafeToSplineHeading(new Vector2d(-36, 50), Math.toRadians(130))
            .strafeToSplineHeading(new Vector2d(-46, 39), Math.toRadians(220))
            .waitSeconds(0.06)
            .strafeToSplineHeading(new Vector2d(-45, 50), Math.toRadians(130))
            .strafeToSplineHeading(new Vector2d(-56, 39), Math.toRadians(220))
            .waitSeconds(0.06)
            .strafeToSplineHeading(new Vector2d(-52, 49), Math.toRadians(130))
            //pushed back 2
            .strafeToSplineHeading(new Vector2d(-43, 63.3), Math.toRadians(90))
            //took clip from wall
            .strafeToSplineHeading(new Vector2d(-11, 38), Math.toRadians(90))
            //clipped # 2
            .setTangent(Math.toRadians(90))
            .strafeToSplineHeading(new Vector2d(-27, 43), Math.toRadians(90))
            .splineToConstantHeading(new Vector2d(-40, 63), Math.toRadians(90))
                .waitSeconds(0.06)
            .strafeToSplineHeading(new Vector2d(-27, 48), Math.toRadians(90))
            .splineToConstantHeading(new Vector2d(-10, 38), Math.toRadians(270)) //changed from 90 // these are just the flick the robot does at the end. if its 270 itll go down and up, 90 it will go just down
            //clipped # 3
            .setTangent(Math.toRadians(90))
            .strafeToSplineHeading(new Vector2d(-27, 43), Math.toRadians(90))
            .splineToConstantHeading(new Vector2d(-40, 62), Math.toRadians(90))
                .waitSeconds(0.06)
            .strafeToSplineHeading(new Vector2d(-27, 48), Math.toRadians(90))
            .splineToConstantHeading(new Vector2d(-9, 38), Math.toRadians(270))
            //clipped # 4
            .strafeToSplineHeading(new Vector2d(-45, 60), Math.toRadians(90))
            .waitSeconds(20);


        Action trajectoryActionCloseOut = tab1.endTrajectory().build();
        waitForStart();

        if (isStopRequested()) return;
        Action trajectoryActionChosen = tab1.build();
        Actions.runBlocking(
                new SequentialAction(
                        trajectoryActionChosen,
                        trajectoryActionCloseOut
                )
        );
        telemetry.update();
    }
}