package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Dalek2 extends OpMode {

    DcMotor Left;
    DcMotor Right;
    Servo Head;

    @Override
    public void init() {
        Left = hardwareMap.dcMotor.get("Left");
        Right = hardwareMap.dcMotor.get("Right");
        Head = hardwareMap.servo.get("Head");
        Right.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        float leftY = -gamepad1.left_stick_y/3;
        float rightY = -gamepad1.right_stick_y/3;

        Left.setPower(leftY);
        Right.setPower(rightY);

        if(gamepad1.dpad_up) {
            Head.setPosition(0.5);
        }else if(gamepad1.dpad_right){
            Head.setPosition(0.9);
        }else if(gamepad1.dpad_left){
            Head.setPosition(0.1);
        }
    }

}

