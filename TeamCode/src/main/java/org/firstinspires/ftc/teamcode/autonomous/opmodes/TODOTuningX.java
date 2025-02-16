package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.autonomous.Controller;

@Autonomous(name = "Basic Auto", group = "Auto")
public class TODOTuningX extends OpMode {

    private Controller controller;

    @Override
    public void init() {
        String path = "id,action,value,requirement,distanceAccuracy,rotationAccuracy,shoulderAccuracy,liftAccuracy\nNICE!!!!,LIFT,0,NONE,NONE,NONE,NONE,NONE\n";
        controller = new Controller(hardwareMap, telemetry, path);
    }

    @Override
    public void loop() {
        controller.run();
    }
}