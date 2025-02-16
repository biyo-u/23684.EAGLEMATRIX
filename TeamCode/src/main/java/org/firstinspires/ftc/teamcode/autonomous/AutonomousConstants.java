package org.firstinspires.ftc.teamcode.autonomous;

import android.os.Environment;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.utilites.PIDF;

@Config
public class AutonomousConstants {
    // TODO: FIND THESE
    public static double SHOULDER_DEGREES_PER_TICK = 0.0235;
    public static double LIFT_INCHES_PER_TICK = 0.00332;
    public static String autoActionsLocation = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/FIRST/config.csv");

    @Config
    public static class ShoulderPositions {
        public static double HIGH_BASKET = 5000;
        public static double HIGH_CHAMBER = 3500;
        public static double SAMPLE_PICKUP = 300; // TODO: Check this
        public static double HOME = 0;
    }

    @Config
    public static class LiftPositions {
        // TODO: Find these
        public static double HIGH_BASKET = 0;
        public static double HIGH_CHAMBER = 0;
        public static double SPECIMEN_PICKUP = 0;
        public static double HOME = 0;
    }

    @Config
    public static class PIDFs {
        public static PIDF shoulderPIDF = new PIDF(0.01, 0, 0, 0);
        public static PIDF liftPIDF = new PIDF(0.01, 0, 0, 0);
        public static PIDF driveXPIDF = new PIDF(0.08, 0, 0.003, 0);
        public static PIDF driveYPIDF = new PIDF(0.08, 0, 0.009, 0);
        public static PIDF driveHeadingPIDF = new PIDF(0.02, 0, 0.001, 0);
    }

    @Config
    public static class THRESHOLDS {
        public static double DEFAULT_DISTANCE_THRESHOLD = 0.5;
        public static double DEFAULT_ROTATION_THRESHOLD = 5.0;
        public static double DEFAULT_SHOULDER_THRESHOLD = 2.0;
        public static double DEFAULT_LIFT_THRESHOLD = 2.0;
        public static double WAIT_THRESHOLD = 2.0;
    }

    @Config
    public static class PATHS {
//        public static String TESTING_PATH = "id,action,value,requirement,distanceAccuracy,rotationAccuracy,shoulderAccuracy,liftAccuracy\nNICE!!!!,LIFT,0,NONE,NONE,NONE,NONE,NONE";
        public static String TESTING_PATH = "id,action,value,requirement,distanceAccuracy,rotationAccuracy,shoulderAccuracy,liftAccuracy";
    }
}