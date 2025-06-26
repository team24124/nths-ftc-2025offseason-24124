package org.firstinspires.ftc.teamcode.utility;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import java.util.ArrayList;
import java.util.List;

public enum ActionScheduler {
    INSTANCE;

    private List<Action> runningActions = new ArrayList<>();
    private boolean isStopRequested = false;

    public void schedule(Action action){
        runningActions.add(action);
    }

    public void init(){
        isStopRequested = false;
        runningActions.clear();
    }

    public void run(){
        TelemetryPacket packet = new TelemetryPacket();

        List<Action> newActions = new ArrayList<>();
        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (action.run(packet) && !isStopRequested) {
                newActions.add(action);
            }
        }
        runningActions = newActions;
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }

    public void stop(){
        isStopRequested = true;
        runningActions.clear();
    }
}
