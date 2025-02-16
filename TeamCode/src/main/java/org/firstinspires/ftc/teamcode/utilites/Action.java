// Action.java
package org.firstinspires.ftc.teamcode.utilites;

import java.util.List;

public class Action {
    private final String id;
    private String action;
    private Double value;
    private List<String> requirements; // Changed to List<String>
    private Double distanceAccuracy;
    private Double rotationAccuracy;
    private Double shoulderAccuracy;
    private Double liftAccuracy;
    private boolean completed = false;

    public Action(String id, String action, Double value, List<String> requirements, Double distanceAccuracy, Double rotationAccuracy, Double shoulderAccuracy, Double liftAccuracy) { // Requirement is now List<String>
        this.id = id;
        this.action = action;
        this.value = value;
        this.requirements = requirements; // Assign List<String>
        this.distanceAccuracy = distanceAccuracy;
        this.rotationAccuracy = rotationAccuracy;
        this.shoulderAccuracy = shoulderAccuracy;
        this.liftAccuracy = liftAccuracy;
    }

    public String getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    // No setter for ID as it should be immutable
    public void setAction(String action) {
        this.action = action;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public List<String> getRequirements() { // Changed return type to List<String>
        return requirements;
    }

    public void setRequirements(List<String> requirements) { // Setter for List<String>
        this.requirements = requirements;
    }

    public Double getDistanceAccuracy() {
        return distanceAccuracy;
    }

    public void setDistanceAccuracy(Double distanceAccuracy) {
        this.distanceAccuracy = distanceAccuracy;
    }

    public Double getRotationAccuracy() {
        return rotationAccuracy;
    }

    public void setRotationAccuracy(Double rotationAccuracy) {
        this.rotationAccuracy = rotationAccuracy;
    }

    public Double getShoulderAccuracy() {
        return shoulderAccuracy;
    }

    public void setShoulderAccuracy(Double shoulderAccuracy) {
        this.shoulderAccuracy = shoulderAccuracy;
    }

    public Double getLiftAccuracy() {
        return liftAccuracy;
    }

    public void setLiftAccuracy(Double liftAccuracy) {
        this.liftAccuracy = liftAccuracy;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}