package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystem.FieldCentricDriveTrain;
import org.firstinspires.ftc.teamcode.utility.ActionScheduler;
import org.firstinspires.ftc.teamcode.utility.subsystems.DriveTrain;

@TeleOp(name = "Drive Only")
public class DriveOnly extends OpMode {

    DriveTrain driveTrain;
    private ActionScheduler actions;
    private GamepadEx driver;

    @Override
    public void init() {
        driveTrain = new FieldCentricDriveTrain(hardwareMap, new Pose2d(0, 0, 0));

        actions = ActionScheduler.INSTANCE;
        actions.init();

        driver = new GamepadEx(gamepad1);
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        // Change speeds of drive train
        if(driver.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)) { actions.schedule(new InstantAction(driveTrain.getSpeeds()::previous)); }
        if(driver.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)) { actions.schedule(new InstantAction(driveTrain.getSpeeds()::next)); }

        driver.readButtons();
        driveTrain.drive(x, y, rx);
        driveTrain.periodic();
        ActionScheduler.INSTANCE.run(); // Call this in order for actions to work
    }

    @Override
    public void stop() {
        actions.stop();
    }
}
