package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "MotorControlJava", group = "TeleOp")
public class testingtele extends LinearOpMode {

    private DcMotor shoulder;
    private DcMotor liftMotorLeft;
    private DcMotor liftMotorRight;

    @Override
    public void runOpMode() {
        // Initialize motors
        shoulder = hardwareMap.get(DcMotor.class, "shoulder");
        liftMotorLeft = hardwareMap.get(DcMotor.class, "liftMotorLeft");
        liftMotorRight = hardwareMap.get(DcMotor.class, "liftMotorRight");

        // Reverse one lift motor so they move in the same direction
        liftMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shoulder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        liftMotorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        telemetry.addData("Status", "Started");
        telemetry.update();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Control lift motors with left stick Y
            double liftPower = -gamepad1.left_stick_y;
            liftMotorLeft.setPower(liftPower);
            liftMotorRight.setPower(liftPower);

            // Control shoulder motor with right stick Y
            double shoulderPower = -gamepad1.right_stick_y;
            shoulder.setPower(shoulderPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: ");
            telemetry.addData("Lift Motor Left", liftMotorLeft.getCurrentPosition());
            telemetry.addData("Lift Motor Right", liftMotorRight.getCurrentPosition());
            telemetry.addData("Shoulder", shoulder.getCurrentPosition());
            telemetry.update();
        }
        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}