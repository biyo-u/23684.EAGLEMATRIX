package org.firstinspires.ftc.teamcode.autonomous;

import android.os.Environment;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.utilites.PIDF;

@Config
public class AutonomousConstants {
    // TODO: FIND THESE
    public static double SHOULDER_DEGREES_PER_TICK = 0.01;
    public static double LIFT_INCHES_PER_TICK = 0.01;
    public static String autoActionsLocation = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/FIRST/config.csv");

    public static class ShoulderPositions {
        double HIGH_BASKET = 5000;
        double HIGH_CHAMBER = 3500;
        double SAMPLE_PICKUP = 300; // TODO: Check this
        double HOME = 0;
    }

    public static class LiftPositions {
        // TODO: Find these
        double HIGH_BASKET = 0;
        double HIGH_CHAMBER = 0;
        double SPECIMEN_PICKUP = 0;
        double HOME = 0;
    }

    public static class PIDFs {
//        public static PIDF shoulderPIDF = new PIDF(0.01, 0, 0, 0);
//        public static PIDF liftPIDF = new PIDF(0.01, 0, 0, 0);
//        public static PIDF driveXPIDF = new PIDF(0.08, 0, 0.003, 0);
//        public static PIDF driveYPIDF = new PIDF(0.08, 0, 0.009, 0);
//        public static PIDF driveHeadingPIDF = new PIDF(0.02, 0, 0.001, 0);

        public static double shoulderP = 0.01, shoulderI = 0, shoulderD = 0, shoulderF = 0;
        public static double liftP = 0.01, liftI = 0, liftD = 0, liftF = 0;
        public static double driveXP = 0.08, driveXI = 0, driveXD = 0.003, driveXF = 0;
        public static double driveYP = 0.08, driveYI = 0, driveYD = 0.009, driveYF = 0;
        public static double driveHeadingP = 0.02, driveHeadingI = 0, driveHeadingD = 0.001, driveHeadingF = 0;

    }

    public static class THRESHOLDS {
        public static final double DEFAULT_DISTANCE_THRESHOLD = 0.5;
        public static final double DEFAULT_ROTATION_THRESHOLD = 5.0;
        public static final double DEFAULT_SHOULDER_THRESHOLD = 2.0;
        public static final double DEFAULT_LIFT_THRESHOLD = 2.0;
        public static final double WAIT_THRESHOLD = 2.0;
    }

    public static class PATHS {
        public static final String TESTING_PATH = "id,action,value,requirement,distanceAccuracy,rotationAccuracy,shoulderAccuracy,liftAccuracy\nNICE!!!!,LIFT,0,NONE,NONE,NONE,NONE,NONE";
    }
}
