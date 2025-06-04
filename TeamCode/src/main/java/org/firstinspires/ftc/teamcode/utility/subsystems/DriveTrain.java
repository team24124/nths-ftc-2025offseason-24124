package org.firstinspires.ftc.teamcode.utility.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.MecanumDrive;
//import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
//import org.firstinspires.ftc.teamcode.roadrunner.PinpointDrive;
import org.firstinspires.ftc.teamcode.subsystem.RobotCentricDriveTrain;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryObservable;

/**
 * Abstract DriveTrain class used to implement different types of drives.
 * By default assumes the drive uses the goBILDA Pinpoint computer and mecanum wheels.
 * See {@link RobotCentricDriveTrain} for drive implementation logic.
 * @see <a href="https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html">gm0 Mecanum Drive Guide</a>
 */
public abstract class DriveTrain implements Subsystem, TelemetryObservable {
    @Override
    public String getName() { return "Drive Train"; }

    private final MecanumDrive drive;
    private double heading;

    public DriveTrain(HardwareMap hw, Pose2d start) {
        drive = new MecanumDrive(hw, start);
        heading = start.heading.toDouble();
    }

    @Override
    public void periodic(){
        //heading = drive;
    }

    /**
     * Abstract method used to define how the drive train drives
     * given values for an x, y and a turn
     * @param x Amount of x (Ex. left/right on the left joystick)
     * @param y Amount of y (Ex. up/down on the left joystick)
     * @param turn Amount of turn (Ex. left/right on the right joystick)
     */
    public abstract void drive(double x, double y, double turn);

    /**
     * @return Last saved pose of the drive train.
     */
//    public Pose2d getCurrentPose() {
//        return drive.getPositionRR();
//    }


    public MecanumDrive getDrive(){
        return drive;
    }

    public double getHeading() {
        return drive.localizer.getPose().heading.toDouble();
    }

    /**
     * Set the power of each wheel given a power for each.
     * @param leftFront Power to give the left front wheel
     * @param rightFront Power to give the right front wheel
     * @param leftBack Power to give the left back wheel
     * @param rightBack Power to give the right back wheel
     */
    public void setDrivePowers(double leftFront, double rightFront, double leftBack, double rightBack){
        drive.leftFront.setPower(leftFront);
        drive.rightFront.setPower(rightFront);
        drive.leftBack.setPower(leftBack);
        drive.rightBack.setPower(rightBack);
    }
}
