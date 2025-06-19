package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.CollectionClaw;
import org.firstinspires.ftc.teamcode.subsystem.ControlClaw;
import org.firstinspires.ftc.teamcode.subsystem.Robot;
import org.firstinspires.ftc.teamcode.utility.ActionScheduler;

@Config
@TeleOp(name = "Component Debugger", group = "Utility")
public class ComponentDebugger extends OpMode {
    private Robot robot;
    private ActionScheduler actions;

    public static double bottomClaw = 0;
    public static double topClaw = 0;
    public static double bottomElbow = 0;
    public static double topElbow = 0;
    public static double extension = 0.5;

    public static int motorArm = 0;
    public static int slideTarget = 0;


    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        actions = ActionScheduler.INSTANCE;
    }

    @Override
    public void loop() {
        if(gamepad1.a) actions.schedule(robot.collectionClaw.setElbowPosition(bottomElbow));
        if(gamepad1.b) actions.schedule(robot.collectionClaw.setClawPosition(bottomClaw));
        if(gamepad1.x) actions.schedule(robot.collectionClaw.rotatePivot());
        if(gamepad1.y) actions.schedule(robot.arm.moveTo(motorArm));

        if(gamepad1.dpad_left) actions.schedule(robot.extension.extendTo(extension));

        if(gamepad2.a) actions.schedule(robot.controlClaw.setElbowPosition(topElbow));
        if(gamepad2.b) actions.schedule(robot.controlClaw.setClawPosition(topClaw));
        if(gamepad2.x) actions.schedule(robot.controlClaw.rotatePivot());
        if(gamepad2.y) actions.schedule(robot.slides.moveTo(slideTarget));

        // Telemetry
        telemetry.addLine("GAMEPAD_ONE");
        telemetry.addData("A", "Bottom Elbow");
        telemetry.addData("B", "Bottom Claw");
        telemetry.addData("X", "Bottom Pivot");
        telemetry.addData("Y", "Arm");
        telemetry.addData("DPAD_LEFT", "Extension");
        telemetry.addLine();
        telemetry.addLine("GAMEPAD_TWO");
        telemetry.addData("A", "Top Elbow");
        telemetry.addData("B", "Top Claw");
        telemetry.addData("X", "Top Pivot");
        telemetry.addData("Y", "Slides");

    }
}
