// Controller.java
package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.utilites.CSVParser.parseCsvString;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utilites.Action;
import org.firstinspires.ftc.teamcode.utilites.TelemetryInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private final Driver driver;
    private final Middleman middleman;
    private final Telemetry telemetry;
    private final TelemetryInfo telemetryInfo;
    private final List<Action> actions;
    private final List<Action> completedActions = new ArrayList<>();
    private final Map<String, Action> actionMap = new HashMap<>();
    private final int currentActionIndex = 0;

    public Controller(HardwareMap hardwareMap, Telemetry telemetry, String path) {
        this.telemetry = telemetry;
        this.telemetryInfo = new TelemetryInfo();
        this.driver = new Driver(hardwareMap);
        this.middleman = new Middleman(hardwareMap, telemetryInfo);

        this.actions = parseCsvString(path);
        for (Action action : actions) {
            actionMap.put(action.getId(), action);
            telemetry.addLine("Parsed Action - ID: " + action.getId() + ", Action: " + action.getAction() + ", Value: " + action.getValue() + ", Requirement: " + action.getRequirements()); // Print requirements list
        }
    }

    public void run() {
        if (!actions.isEmpty()) {
            Action currentAction = actions.get(currentActionIndex);

            if (areRequirementsMet(currentAction)) { // Use areRequirementsMet
                if (!currentAction.isCompleted()) {
                    startAction(currentAction);
                } else {
                    if (isActionComplete(currentAction)) {
                        completedActions.add(currentAction);
                        actions.remove(currentActionIndex);
                        telemetry.addLine("Action " + currentAction.getId() + " completed and removed, moving to next action.");
                    }
                }
            } else {
                // Requirement not met yet, wait.
                telemetry.addLine("Waiting for requirements for Action " + currentAction.getId() + ": " + currentAction.getRequirements()); // Print requirements list
            }
        } else {
            telemetry.addLine("Autonomous sequence complete!");
        }

        middleman.loop();

        telemetry.addData("Shoulder Position", telemetryInfo.shoulderPosition);
        telemetry.addData("Lift Position", telemetryInfo.liftPosition);
        telemetry.addData("X Position", telemetryInfo.xPosition);
        telemetry.addData("Y Position", telemetryInfo.yPosition);
        telemetry.addData("Heading Position", telemetryInfo.headingPosition);
        telemetry.addData("Drive X Target", telemetryInfo.driveXTarget);
        telemetry.addData("Drive Y Target", telemetryInfo.driveYTarget);
        telemetry.addData("Drive Heading Target", telemetryInfo.driveHeadingTarget);
        telemetry.addData("Shoulder Target", telemetryInfo.shoulderTarget);
        telemetry.addData("Lift Target", telemetryInfo.liftTarget);
        telemetry.addData("Drive X Busy", telemetryInfo.driveXBusy);
        telemetry.addData("Drive Y Busy", telemetryInfo.driveYBusy);
        telemetry.addData("Drive Heading Busy", telemetryInfo.driveHeadingBusy);
        telemetry.addData("Shoulder Busy", telemetryInfo.shoulderBusy);
        telemetry.addData("Lift Busy", telemetryInfo.liftBusy);
        telemetry.addData("Drive X Power", telemetryInfo.driveXPower);
        telemetry.addData("Drive Y Power", telemetryInfo.driveYPower);
        telemetry.addData("Drive Heading Power", telemetryInfo.driveHeadingPower);
        telemetry.addData("Shoulder Power", telemetryInfo.shoulderPower);
        telemetry.addData("Lift Power", telemetryInfo.liftPower);
        telemetry.addData("Distance Accuracy", telemetryInfo.distanceAccuracyThreshold);
        telemetry.addData("Rotation Accuracy", telemetryInfo.rotationAccuracyThreshold);
        telemetry.addData("Shoulder Accuracy", telemetryInfo.shoulderAccuracyThreshold);
        telemetry.addData("Lift Accuracy", telemetryInfo.liftAccuracyThreshold);
    }

    private boolean areRequirementsMet(Action action) { // Renamed to areRequirementsMet and updated logic
        List<String> requirementIds = action.getRequirements();
        if (requirementIds == null || requirementIds.isEmpty()) { // If no requirements, consider them met
            return true;
        }

        for (String requirementId : requirementIds) { // Iterate through all requirements
            boolean requirementMet = false;
            for (Action completedAction : completedActions) {
                if (completedAction.getId().equals(requirementId)) {
                    requirementMet = true;
                    break; // If one requirement is met, move to the next requirement
                }
            }
            if (!requirementMet) {
                return false; // If any requirement is not met, return false
            }
        }
        return true; // All requirements are met
    }


    private void startAction(Action action) {
        telemetry.addLine("Starting Action: " + action.getId() + ", Type: " + action.getAction());
        String actionType = action.getAction().toUpperCase();
        Double value = action.getValue();
        Double distanceAccuracy = action.getDistanceAccuracy();
        Double rotationAccuracy = action.getRotationAccuracy();
        Double shoulderAccuracy = action.getShoulderAccuracy();
        Double liftAccuracy = action.getLiftAccuracy();

        switch (actionType) {
            case "NOTHING":
                action.setCompleted(true);
                break;
            case "DRIVEX":
                if (value != null) {
                    middleman.setXTarget(value, distanceAccuracy != null ? distanceAccuracy : AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD);
                } else {
                    telemetry.addLine("Error: DRIVEX action requires a value.");
                }
                break;
            case "DRIVEY":
                if (value != null) {
                    middleman.setYTarget(value, distanceAccuracy != null ? distanceAccuracy : AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD);
                } else {
                    telemetry.addLine("Error: DRIVEY action requires a value.");
                }
                break;
            case "TURN":
                if (value != null) {
                    middleman.setHeadingTarget(value, rotationAccuracy != null ? rotationAccuracy : AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD);
                } else {
                    telemetry.addLine("Error: TURN action requires a value.");
                }
                break;
            case "SHOULDER":
                if (value != null) {
                    middleman.setShoulderTarget(value, shoulderAccuracy != null ? shoulderAccuracy : AutonomousConstants.THRESHOLDS.DEFAULT_SHOULDER_THRESHOLD);
                } else {
                    telemetry.addLine("Error: SHOULDER action requires a value.");
                }
                break;
            case "LIFT":
                if (value != null) {
                    middleman.setLiftTarget(value, liftAccuracy != null ? liftAccuracy : AutonomousConstants.THRESHOLDS.DEFAULT_LIFT_THRESHOLD);
                } else {
                    telemetry.addLine("Error: LIFT action requires a value.");
                }
                break;
            default:
                telemetry.addLine("Unknown action type: " + actionType);
                action.setCompleted(true);
                break;
        }
    }

    private boolean isActionComplete(Action action) {
        String actionType = action.getAction().toUpperCase();
        return switch (actionType) {
            case "NOTHING" -> true;
            case "DRIVEX" -> !middleman.isDriveXBusy();
            case "DRIVEY" -> !middleman.isDriveYBusy();
            case "TURN" -> !middleman.isDriveHeadingBusy();
            case "SHOULDER" -> !middleman.isShoulderBusy();
            case "LIFT" -> !middleman.isLiftBusy();
            default -> true;
        };
    }
}