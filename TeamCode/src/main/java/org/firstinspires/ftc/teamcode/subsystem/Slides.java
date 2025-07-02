package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.Utilities;
import org.firstinspires.ftc.teamcode.utility.selections.ArraySelect;
import org.firstinspires.ftc.teamcode.utility.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryObservable;

public class Slides implements Subsystem, TelemetryObservable {
    private final DcMotorEx leftSlide, rightSlide;
    private final PIDController controller;
    private final VoltageSensor voltageSensor;

    public static int target = 0;
    public static PIDFCoefficients coefficients = new PIDFCoefficients(
            0.00945,
            0,
            0.0001,
            0.01
    );

    // TODO: Tune these positions (maybe)
    public enum State {
        HOME(0),
        PASSTHROUGH(400),
        ACTIVE(750), // 380
        //INBETWEEN(800),
        //HOVER(1200), // 760
        CLIPPER(1600), //1650
        //HANG(2650), // 1700
        HIGH_RUNG(3700), // 2000
        //CLIP_HANG(5000), //3200
        CLIP_HIGH_CHAMBER(7000), // 4000
        HIGH_BUCKET(10000); //5800

        public final int position;
        public static final int MAX = State.values().length - 1;

        State(int position) {
            this.position = position;
        }
    }

    // Create a selection array from all SlideState values
    public final ArraySelect<State> positions = new ArraySelect<>(State.values());

    public Slides(HardwareMap hw){
        leftSlide = hw.get(DcMotorEx.class, "left_slide");
        rightSlide = hw.get(DcMotorEx.class, "right_slide");

        rightSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        voltageSensor = hw.get(VoltageSensor.class, "Control Hub");
        controller = new PIDController(coefficients.p, coefficients.i, coefficients.d);
    }

    @Override
    public void periodic() {
        target = positions.getSelected().position;
        double p = coefficients.p;
        double i = coefficients.i;
        double d = coefficients.d;
        double f = coefficients.f;

        controller.setPID(p, i, d);
        int armPos = leftSlide.getCurrentPosition();
        double pid = controller.calculate(armPos, target);
        double power = (pid + f) * (12.0 / voltageSensor.getVoltage()); // Compensate for voltage discrepencies

        leftSlide.setPower(power);
        rightSlide.setPower(power);
    }

    public Action setStateTo(State state){
        return (TelemetryPacket packet) -> {
            positions.setSelected(state);
            return false;
        };
    }

    public Action nextPos(){
        return (TelemetryPacket packet) -> {
            positions.next();
            return false;
        };
    }

    public Action prevPos(){
        return (TelemetryPacket packet) -> {
            positions.previous();
            return false;
        };
    }

    /**
     * Move the slides to a given target position as a RaodRunner action.
     *
     * @param target Target position in encoder ticks for the slides to travel to
     * @return A RoadRunner Action
     */
    public Action moveTo(int target) {
        return (TelemetryPacket packet) -> {
            int slidePos = leftSlide.getCurrentPosition();
            double tolerance = 0.1 * target + 10; // Check if we are within 1% of the target, with a constant of 1

            double p = coefficients.p, i = coefficients.i, d = coefficients.d, f = coefficients.f;

            // FTCDashboard Telemetry
            packet.put("Position", slidePos);
            packet.put("Target", target);
            packet.put("Position Reached?", Utilities.isBetween(slidePos, target - tolerance, target + tolerance));

            controller.setPID(p, i, d);
            double pid = controller.calculate(slidePos, target);
            double power = (pid + f + 0.4) * (12.0 / voltageSensor.getVoltage()); // Compensate for voltages
            setPower(power);

            packet.put("Power", power);

            if (Utilities.isBetween(slidePos, target - tolerance, target + tolerance)) {
                setPower(f);
                return false; // Stop the command
            } else {
                return true; // Otherwise continue running it
            }
        };
    }

    /**
     * Set the power of both motors driving the slides given a power
     *
     * @param power Power to give to the motors
     */
    public void setPower(double power) {
        rightSlide.setPower(power);
        leftSlide.setPower(power);
    }

    /**
     * Stop and reset all used motors. Sets motor's RunMode to RUN_WITHOUT_ENCODER after.
     */
    public void stopAndResetEncoders() {
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    @Override
    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("Target", target);
        telemetry.addData("Left Slide Pos.", leftSlide.getCurrentPosition());
        telemetry.addData("Right Slide Pos.", rightSlide.getCurrentPosition());
    }

    @Override
    public String getName() {
        return "Slides";
    }
}
