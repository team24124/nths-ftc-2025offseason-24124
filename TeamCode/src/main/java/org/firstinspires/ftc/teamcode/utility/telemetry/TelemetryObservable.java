package org.firstinspires.ftc.teamcode.utility.telemetry;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.subsystems.Subsystem;

public interface TelemetryObservable {
    /**
     * Method called by the TelemetryMaster {@link TelemetryMaster} to update corresponding telemetry.
     * @param telemetry Telemetry system to use.
     */
    void updateTelemetry(Telemetry telemetry);

    default String getName(){
        if(this instanceof Subsystem) return this.getName();

        return ""; // If the observable object is not a subsystem don't give it any name
    }
}
