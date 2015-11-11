package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


public class BBotsRedAutonomous extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();


            leftMotor.setPower(1.0);
            rightMotor.setPower(1.0);
            sleep(2000);

            leftMotor.setPower(-0.5);
            rightMotor.setPower(0.5);
            sleep(2500);


        leftMotor.setPowerFloat();
        rightMotor.setPowerFloat();
    }
}