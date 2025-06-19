package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryMaster;

public class Robot {
    public Arm arm;
    public CollectionClaw collectionClaw;
    public ControlClaw controlClaw;
    public Extension extension;
    public Slides slides;
    public RobotCentricDriveTrain driveTrain;

    public TelemetryMaster telemetryMaster;

    public Robot(HardwareMap hw, Telemetry telemetry) {
        arm = new Arm(hw);
        collectionClaw = new CollectionClaw(hw);
        controlClaw = new ControlClaw(hw);
        extension = new Extension(hw);
        slides = new Slides(hw);

        Pose2d startPose = new Pose2d(new Vector2d(0, 0), Math.toRadians(0));
        driveTrain = new RobotCentricDriveTrain(hw, startPose);

        telemetryMaster = new TelemetryMaster(telemetry);
        telemetryMaster
                .subscribe(arm)
                .subscribe(collectionClaw)
                .subscribe(controlClaw)
                .subscribe(extension)
                .subscribe(slides)
                .subscribe(driveTrain);
    }
}
