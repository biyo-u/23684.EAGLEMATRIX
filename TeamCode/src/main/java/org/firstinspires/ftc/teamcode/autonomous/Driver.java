package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.utilites.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.utilites.TelemetryInfo;

public class Driver {
    private final DcMotor shoulder;
    private final DcMotor liftLeft;
    private final DcMotor liftRight;
    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor rearLeft;
    private final DcMotor rearRight;
    private final GoBildaPinpointDriver odometry;
    private double shoulderPosition = 0;
    private double liftPosition = 0;
    private Pose2D position = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);

    public Driver(HardwareMap hardwareMap) {
        shoulder = hardwareMap.get(DcMotor.class, "shoulder");
        liftLeft = hardwareMap.get(DcMotor.class, "liftMotorLeft");
        liftRight = hardwareMap.get(DcMotor.class, "liftMotorRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        odometry = hardwareMap.get(GoBildaPinpointDriver.class, "odo");

        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shoulder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setDirection(DcMotor.Direction.REVERSE);
        liftRight.setDirection(DcMotor.Direction.FORWARD);
        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        rearRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        odometry.setOffsets(-173.0, 156); //measured in mm
        odometry.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        odometry.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odometry.resetPosAndIMU();
    }

    public void loop() {
        odometry.update();
        position = odometry.getPosition();
        shoulderPosition = shoulder.getCurrentPosition();
        liftPosition = (liftLeft.getCurrentPosition() + liftRight.getCurrentPosition()) / 2.0;
    }

    public void setLiftPower(double power) {
        liftLeft.setPower(power);
        liftRight.setPower(power);
    }

    public void setShoulderPower(double power) {
        shoulder.setPower(power);
    }

    public void setDrivePower(double x, double y, double turn) {
        double heading = position.getHeading(AngleUnit.DEGREES);
        double rotX = x * Math.cos(-heading) + y * Math.sin(-heading);
        double rotY = x * Math.sin(-heading) - y * Math.cos(-heading);
        // TODO: Make sure this is needed and doesn't just cause problems
        rotX = rotX * 1.1;
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn), 1);
        double frontLeftPower = ((rotY + rotX + turn) / denominator);
        double backLeftPower = ((rotY - rotX + turn) / denominator);
        double frontRightPower = ((rotY - rotX - turn) / denominator);
        double backRightPower = ((rotY + rotX - turn) / denominator);

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        rearLeft.setPower(backLeftPower);
        rearRight.setPower(backRightPower);
    }

    public Pose2D getPosition() {
        return position;
    }

    public double getShoulderPosition() {
        return shoulderPosition;
    }

    public double getLiftPosition() {
        return liftPosition;
    }

    public void telemetry(TelemetryInfo telemetryInfo) {
        telemetryInfo.xPosition = position.getX(DistanceUnit.INCH);
        telemetryInfo.yPosition = position.getY(DistanceUnit.INCH);
        telemetryInfo.headingPosition = position.getHeading(AngleUnit.DEGREES);
        telemetryInfo.shoulderPosition = shoulderPosition;
        telemetryInfo.liftPosition = liftPosition;
    }
}
