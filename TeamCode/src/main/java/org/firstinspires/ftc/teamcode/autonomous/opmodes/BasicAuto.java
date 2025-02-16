package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.autonomous.AutonomousConstants;
import org.firstinspires.ftc.teamcode.autonomous.Controller;
import org.firstinspires.ftc.teamcode.utilites.TelemetryInfo;

@Autonomous(name = "Basic Auto", group = "Auto")
public class BasicAuto extends OpMode {

    private Controller controller;

    @Override
    public void init() {
        String path = AutonomousConstants.PATHS.TESTING_PATH;
        this.controller = new Controller(hardwareMap, telemetry, path);
    }

    @Override
    public void loop() {
        controller.run();
    }
}