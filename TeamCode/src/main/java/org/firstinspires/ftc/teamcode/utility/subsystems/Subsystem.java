package org.firstinspires.ftc.teamcode.utility.subsystems;

public interface Subsystem {

    /**
     * All subsystems are required to provide a name as a String to use as an identifier.
     * @return Name of the subsystem
     */
    String getName();

    /**
     * Method called periodically by the Action Scheduler {@link }
     * By default has an empty body, able to be overridden.
     */
    default void periodic() {}
}
