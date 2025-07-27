package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.CollectionClaw;
import org.firstinspires.ftc.teamcode.subsystem.ControlClaw;
import org.firstinspires.ftc.teamcode.subsystem.Extension;
import org.firstinspires.ftc.teamcode.subsystem.Robot;
import org.firstinspires.ftc.teamcode.subsystem.Slides;
import org.firstinspires.ftc.teamcode.utility.ActionScheduler;

@Config
@TeleOp(name = "Component Debugger", group = "Utility")
public class ComponentDebugger extends OpMode {
    //private Robot robot;
    private Extension extension;
    private ActionScheduler actions;

    public static double bottomClaw = 0;
    public static double topClaw = 0;
    public static double bottomElbow = 0;
    public static double topElbow = 0;
    public static double extension_Val = 0.5;

    public static int motorArm = 0;
    public static Slides.State slideTarget = Slides.State.HOME;

    boolean right_bumper_just_pressed = false;
    GamepadEx gamepadEx1;
    GamepadEx gamepadEx2;

    @Override
    public void init() {
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);
        //robot = new Robot(hardwareMap, telemetry);
        extension = new Extension(hardwareMap);
        actions = ActionScheduler.INSTANCE;
    }

    @Override
    public void loop() {
        if(gamepad1.right_bumper && !right_bumper_just_pressed){
            right_bumper_just_pressed = true;
        }

//        if(gamepad1.a) actions.schedule(collectionClaw.setElbowPosition(bottomElbow));
//        if(gamepad1.b) actions.schedule(collectionClaw.setClawPosition(bottomClaw));

//        if(gamepadEx1.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)){
//            actions.schedule(robot.collectionClaw.prevPivot());
//        }
//
//        if(gamepadEx1.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)){
//            actions.schedule(robot.collectionClaw.nextPivot());
//        }
//
//
//
//        if(gamepad1.y) actions.schedule(robot.arm.moveTo(motorArm));
//
        if(gamepad1.dpad_left) actions.schedule(extension.extendTo(extension_Val));
//
//        if(gamepad2.a) actions.schedule(robot.controlClaw.setElbowPosition(topElbow));
//        if(gamepad2.b) actions.schedule(robot.controlClaw.setClawPosition(topClaw));
//        if(gamepadEx2.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)){
//            actions.schedule(robot.controlClaw.prevPivot());
//        }
//
//        if(gamepadEx2.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)){
//            actions.schedule(robot.controlClaw.nextPivot());
//        }
//        if(gamepad2.y) actions.schedule(robot.slides.setStateTo(slideTarget));

        gamepadEx1.readButtons();
        gamepadEx2.readButtons();
        //robot.slides.periodic();
        actions.run();

        // Telemetry
        telemetry.addLine("GAMEPAD_ONE");
        telemetry.addData("A", "Bottom Elbow");
        telemetry.addData("B", "Bottom Claw");
        telemetry.addData("X", "Bottom Pivot");
        telemetry.addData("Y", "Arm");
        telemetry.addData("DPAD_LEFT", "Extension");
        //telemetry.addData("Current Rotation", robot.collectionClaw.pivotStates.getSelected());
        telemetry.addLine();
        telemetry.addLine("GAMEPAD_TWO");
        telemetry.addData("A", "Top Elbow");
        telemetry.addData("B", "Top Claw");
        telemetry.addData("X", "Top Pivot");
        telemetry.addData("Y", "Slides");
        telemetry.addLine();
        //telemetry.addData("Arm Pos.", robot.arm.armMotor.getCurrentPosition());
    }
}
