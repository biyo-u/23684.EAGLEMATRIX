package org.firstinspires.ftc.teamcode.autonomous;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.utilites.Timer;

import java.util.concurrent.TimeUnit;

public class Middleman {
    private final Driver driver;
    private final Telemetry telemetry;
    private final PIDFController shoulderController;
    private final PIDFController liftController;
    private final PIDFController driveXController;
    private final PIDFController driveYController;
    private final PIDFController driveHeadingController;
    private final Timer driveXTimer = new Timer(TimeUnit.HOURS.toMillis(1));
    private final Timer driveYTimer = new Timer(TimeUnit.HOURS.toMillis(1));
    private final Timer driveHeadingTimer = new Timer(TimeUnit.HOURS.toMillis(1));
    private final Timer shoulderTimer = new Timer(TimeUnit.HOURS.toMillis(1));
    private final Timer liftTimer = new Timer(TimeUnit.HOURS.toMillis(1));
    private double shoulderTarget = 0;
    private double liftTarget = 0;
    private Pose2D positionTarget = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
    private double distanceAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD;
    private double rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    private double shoulderAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_SHOULDER_THRESHOLD;
    private double liftAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_LIFT_THRESHOLD;
    private boolean driveXBusy = false;
    private boolean driveYBusy = false;
    private boolean driveHeadingBusy = false;
    private boolean shoulderBusy = false;
    private boolean liftBusy = false;


    public Middleman(HardwareMap hardwareMap, Telemetry telemetry) {
        this.driver = new Driver(hardwareMap);
        this.telemetry = telemetry;
//        this.shoulderController = new PIDFController(AutonomousConstants.PIDFs.shoulderPIDF.getP(), AutonomousConstants.PIDFs.shoulderPIDF.getI(), AutonomousConstants.PIDFs.shoulderPIDF.getD(), AutonomousConstants.PIDFs.shoulderPIDF.getF());
//        this.liftController = new PIDFController(AutonomousConstants.PIDFs.liftPIDF.getP(), AutonomousConstants.PIDFs.liftPIDF.getI(), AutonomousConstants.PIDFs.liftPIDF.getD(), AutonomousConstants.PIDFs.liftPIDF.getF());
//        this.driveXController = new PIDFController(AutonomousConstants.PIDFs.driveXPIDF.getP(), AutonomousConstants.PIDFs.driveXPIDF.getI(), AutonomousConstants.PIDFs.driveXPIDF.getD(), AutonomousConstants.PIDFs.driveXPIDF.getF());
//        this.driveYController = new PIDFController(AutonomousConstants.PIDFs.driveYPIDF.getP(), AutonomousConstants.PIDFs.driveYPIDF.getI(), AutonomousConstants.PIDFs.driveYPIDF.getD(), AutonomousConstants.PIDFs.driveYPIDF.getF());
//        this.driveHeadingController = new PIDFController(AutonomousConstants.PIDFs.driveHeadingPIDF.getP(), AutonomousConstants.PIDFs.driveHeadingPIDF.getI(), AutonomousConstants.PIDFs.driveHeadingPIDF.getD(), AutonomousConstants.PIDFs.driveHeadingPIDF.getF());
        this.shoulderController = new PIDFController(AutonomousConstants.PIDFs.shoulderP, AutonomousConstants.PIDFs.shoulderI, AutonomousConstants.PIDFs.shoulderD, AutonomousConstants.PIDFs.shoulderF);
        this.liftController = new PIDFController(AutonomousConstants.PIDFs.liftP, AutonomousConstants.PIDFs.liftI, AutonomousConstants.PIDFs.liftD, AutonomousConstants.PIDFs.liftF);
        this.driveXController = new PIDFController(AutonomousConstants.PIDFs.driveXP, AutonomousConstants.PIDFs.driveXI, AutonomousConstants.PIDFs.driveXD, AutonomousConstants.PIDFs.driveXF);
        this.driveYController = new PIDFController(AutonomousConstants.PIDFs.driveYP, AutonomousConstants.PIDFs.driveYI, AutonomousConstants.PIDFs.driveYD, AutonomousConstants.PIDFs.driveYF);
        this.driveHeadingController = new PIDFController(AutonomousConstants.PIDFs.driveHeadingP, AutonomousConstants.PIDFs.driveHeadingI, AutonomousConstants.PIDFs.driveHeadingD, AutonomousConstants.PIDFs.driveHeadingF);
    }

    public void loop() {
        double antiNormalizedHeading = ((driver.getPosition().getHeading(AngleUnit.DEGREES) - positionTarget.getHeading(AngleUnit.DEGREES)) % 360 + 360) % 360;

        if (antiNormalizedHeading > 180) {
            antiNormalizedHeading -= 360;
        }

        driver.setShoulderPower(shoulderController.calculate(driver.getShoulderPosition(), shoulderTarget / AutonomousConstants.SHOULDER_DEGREES_PER_TICK));
        driver.setLiftPower(liftController.calculate(driver.getLiftPosition(), liftTarget / AutonomousConstants.LIFT_INCHES_PER_TICK));
        driver.setDrivePower(
                driveXController.calculate(driver.getPosition().getX(DistanceUnit.INCH), positionTarget.getX(DistanceUnit.INCH)),
                driveYController.calculate(driver.getPosition().getY(DistanceUnit.INCH), positionTarget.getY(DistanceUnit.INCH)),
                driveHeadingController.calculate(antiNormalizedHeading, positionTarget.getHeading(AngleUnit.DEGREES)));
        driver.loop();
        driver.telemetry(telemetry);
        telemetry();

        if (Math.abs(driver.getPosition().getX(DistanceUnit.INCH) - positionTarget.getX(DistanceUnit.INCH)) <= distanceAccuracyThreshold) {
            if (!driveXTimer.isTimerOn()) {
                driveXTimer.reset();
                driveXTimer.start();
                driveXBusy = true;
            } else if (driveXTimer.elapsedTime() >= AutonomousConstants.THRESHOLDS.WAIT_THRESHOLD) {
                driveXBusy = false;
                driveXTimer.pause();
            }
        } else {
            driveXBusy = true;
            driveXTimer.reset();
            driveXTimer.pause();
        }

        if (Math.abs(driver.getPosition().getY(DistanceUnit.INCH) - positionTarget.getY(DistanceUnit.INCH)) <= distanceAccuracyThreshold) {
            if (!driveYTimer.isTimerOn()) {
                driveYTimer.reset();
                driveYTimer.start();
                driveYBusy = true;
            } else if (driveYTimer.elapsedTime() >= AutonomousConstants.THRESHOLDS.WAIT_THRESHOLD) {
                driveYBusy = false;
                driveYTimer.pause();
            }
        } else {
            driveYBusy = true;
            driveYTimer.reset();
            driveYTimer.pause();
        }

        if (Math.abs(driver.getPosition().getHeading(AngleUnit.DEGREES) - positionTarget.getHeading(AngleUnit.DEGREES)) <= rotationAccuracyThreshold) {
            if (!driveHeadingTimer.isTimerOn()) {
                driveHeadingTimer.reset();
                driveHeadingTimer.start();
                driveHeadingBusy = true;
            } else if (driveHeadingTimer.elapsedTime() >= AutonomousConstants.THRESHOLDS.WAIT_THRESHOLD) {
                driveHeadingBusy = false;
                driveHeadingTimer.pause();
            }
        } else {
            driveHeadingBusy = true;
            driveHeadingTimer.reset();
            driveHeadingTimer.pause();
        }

        if (Math.abs(driver.getShoulderPosition() - (shoulderTarget / AutonomousConstants.SHOULDER_DEGREES_PER_TICK)) <= shoulderAccuracyThreshold) {
            if (!shoulderTimer.isTimerOn()) {
                shoulderTimer.reset();
                shoulderTimer.start();
                shoulderBusy = true;
            } else if (shoulderTimer.elapsedTime() >= AutonomousConstants.THRESHOLDS.WAIT_THRESHOLD) {
                shoulderBusy = false;
                shoulderTimer.pause();
            }
        } else {
            shoulderBusy = true;
            shoulderTimer.reset();
            shoulderTimer.pause();
        }

        if (Math.abs(driver.getLiftPosition() - (liftTarget / AutonomousConstants.LIFT_INCHES_PER_TICK)) <= liftAccuracyThreshold) {
            if (!liftTimer.isTimerOn()) {
                liftTimer.reset();
                liftTimer.start();
                liftBusy = true;
            } else if (liftTimer.elapsedTime() >= AutonomousConstants.THRESHOLDS.WAIT_THRESHOLD) {
                liftBusy = false;
                liftTimer.pause();
            }
        } else {
            liftBusy = true;
            liftTimer.reset();
            liftTimer.pause();
        }
    }

    public void telemetry() {
        telemetry.addData("Shoulder Target", shoulderTarget);
        telemetry.addData("Lift Target", liftTarget);
        telemetry.addData("X Target", positionTarget.getX(DistanceUnit.INCH));
        telemetry.addData("Y Target", positionTarget.getY(DistanceUnit.INCH));
        telemetry.addData("Heading Target", positionTarget.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Distance Accuracy Threshold", distanceAccuracyThreshold);
        telemetry.addData("Rotation Accuracy Threshold", rotationAccuracyThreshold);
        telemetry.addData("Shoulder Accuracy Threshold", shoulderAccuracyThreshold);
        telemetry.addData("Lift Accuracy Threshold", liftAccuracyThreshold);

        telemetry.addData("isDriveXBusy", isDriveXBusy());
        telemetry.addData("isDriveYBusy", isDriveYBusy());
        telemetry.addData("isDriveHeadingBusy", isDriveHeadingBusy());
        telemetry.addData("isShoulderBusy", isShoulderBusy());
        telemetry.addData("isLiftBusy", isLiftBusy());

        telemetry.update();
    }

    public boolean isDriveXBusy() {
        return driveXBusy;
    }

    public boolean isDriveYBusy() {
        return driveYBusy;
    }

    public boolean isDriveHeadingBusy() {
        return driveHeadingBusy;
    }

    public boolean isShoulderBusy() {
        return shoulderBusy;
    }

    public boolean isLiftBusy() {
        return liftBusy;
    }

    public void setShoulderTarget(double target, double shoulderAccuracy) {
        shoulderTarget = target;
        shoulderAccuracyThreshold = shoulderAccuracy;
    }

    public void setShoulderTarget(double target) {
        shoulderTarget = target;
        shoulderAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_SHOULDER_THRESHOLD;
    }

    public void setLiftTarget(double target, double liftAccuracy) {
        liftTarget = target;
        liftAccuracyThreshold = liftAccuracy;
    }

    public void setLiftTarget(double target) {
        liftTarget = target;
        liftAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_LIFT_THRESHOLD;
    }

    public void setPositionTarget(Pose2D target, double distanceAccuracy, double rotationAccuracy) {
        positionTarget = target;
        distanceAccuracyThreshold = distanceAccuracy;
        rotationAccuracyThreshold = rotationAccuracy;
    }

    public void setPositionTarget(double x, double y, double heading, double distanceAccuracy, double rotationAccuracy) {
        positionTarget = new Pose2D(DistanceUnit.INCH, x, y, AngleUnit.DEGREES, heading);
        distanceAccuracyThreshold = distanceAccuracy;
        rotationAccuracyThreshold = rotationAccuracy;
    }

    public void setXYTarget(double x, double y, double distanceAccuracy) {
        positionTarget = new Pose2D(DistanceUnit.INCH, x, y, AngleUnit.DEGREES, positionTarget.getHeading(AngleUnit.DEGREES));
        distanceAccuracyThreshold = distanceAccuracy;
        rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    }


    public void setXYTarget(double x, double y) {
        positionTarget = new Pose2D(DistanceUnit.INCH, x, y, AngleUnit.DEGREES, positionTarget.getHeading(AngleUnit.DEGREES));
        distanceAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD;
        rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    }

    public void setXTarget(double x, double distanceAccuracy) {
        positionTarget = new Pose2D(DistanceUnit.INCH, x, positionTarget.getY(DistanceUnit.INCH), AngleUnit.DEGREES, positionTarget.getHeading(AngleUnit.DEGREES));
        distanceAccuracyThreshold = distanceAccuracy;
        rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    }

    public void setXTarget(double x) {
        positionTarget = new Pose2D(DistanceUnit.INCH, x, positionTarget.getY(DistanceUnit.INCH), AngleUnit.DEGREES, positionTarget.getHeading(AngleUnit.DEGREES));
        distanceAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD;
        rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    }

    public void setYTarget(double y, double distanceAccuracy) {
        positionTarget = new Pose2D(DistanceUnit.INCH, positionTarget.getX(DistanceUnit.INCH), y, AngleUnit.DEGREES, positionTarget.getHeading(AngleUnit.DEGREES));
        distanceAccuracyThreshold = distanceAccuracy;
        rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    }

    public void setYTarget(double y) {
        positionTarget = new Pose2D(DistanceUnit.INCH, positionTarget.getX(DistanceUnit.INCH), y, AngleUnit.DEGREES, positionTarget.getHeading(AngleUnit.DEGREES));
        distanceAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD;
        rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    }

    public void setHeadingTarget(double heading, double rotationAccuracy) {
        positionTarget = new Pose2D(DistanceUnit.INCH, positionTarget.getX(DistanceUnit.INCH), positionTarget.getY(DistanceUnit.INCH), AngleUnit.DEGREES, heading);
        distanceAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD;
        rotationAccuracyThreshold = rotationAccuracy;
    }

    public void setHeadingTarget(double heading) {
        positionTarget = new Pose2D(DistanceUnit.INCH, positionTarget.getX(DistanceUnit.INCH), positionTarget.getY(DistanceUnit.INCH), AngleUnit.DEGREES, heading);
        distanceAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD;
        rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    }

    public void setPositionTarget(Pose2D target) {
        positionTarget = target;
        distanceAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD;
        rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    }

    public void setPositionTarget(double x, double y, double heading) {
        positionTarget = new Pose2D(DistanceUnit.INCH, x, y, AngleUnit.DEGREES, heading);
        distanceAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_DISTANCE_THRESHOLD;
        rotationAccuracyThreshold = AutonomousConstants.THRESHOLDS.DEFAULT_ROTATION_THRESHOLD;
    }
}