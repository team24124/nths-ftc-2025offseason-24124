package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.selections.ArraySelect;
import org.firstinspires.ftc.teamcode.utility.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.utility.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.utility.telemetry.TelemetryObservable;

public class Limelight implements Subsystem, TelemetryObservable  {
    private final Limelight3A limelight;
    private final DriveTrain driveTrain;

    public final ArraySelect<Integer> pipelines;

    private final double kP = 0.0022; //add to teleop
    boolean toggleSearch = false; //add to teleop

    public Limelight(HardwareMap hw, DriveTrain driveTrain) {
        limelight = hw.get(Limelight3A.class, "limelight");
        pipelines = new ArraySelect<>(new Integer[]{0, 1, 2});

        // 0 = Red, 1 = Blue, 2 = Yellow
        limelight.pipelineSwitch(2);
        this.driveTrain = driveTrain;
    }

    public Action navigateToBlock(){
        return (TelemetryPacket packet) -> {
            limelight.pipelineSwitch(pipelines.getSelected());
            if (limelight.isRunning() && limelight.getLatestResult() != null) {
                if (limelight.getLatestResult().getPythonOutput() != null && limelight.getLatestResult().getPythonOutput()[0] == 1) {
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
                        driveTrain.setDrivePowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower);

                        if (targetX > 466 && targetX < 476 && targetY > 265 && targetY < 275) { //change servo movements here to what you want
                            driveTrain.setDrivePowers(0, 0, 0, 0);

                            //right tilted block is 90 to 180:  //
                            //left tilted block is 0 to 90:  \\
                            //bottom pivot positions are 0.15 0.5 0.82 from left to right  | _ |
                            break;
                        }
                    }
                }
            }

            return false;
        };
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("Selected Pipeline", pipelines.getSelected());
    }

    @Override
    public String getName() {
        return "Limelight";
    }
}
