//Robot needs to drive (2 motors) and motor on arm needs to spin
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


public class BBotsTeleopTankMode extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor liftMotor;
    DcMotor brushMotor;
    Servo   hopperServo;

    @Override
    public void init() {
        //get references to the motors from the hardware map
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        brushMotor = hardwareMap.dcMotor.get("brushMotor");
        hopperServo = hardwareMap.servo.get("hopperServo");

        //reverse the right motor
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        //get the values from the gamepads
        //note: pushing the stick all the way up returns -1,
        // so we need to reverse the values
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        //set the power of the motors with the gamepad values
        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
        if(gamepad1.y) {
            liftMotor.setPower(1);
        }
        else if(gamepad1.b) {
            liftMotor.setPower(-1);
        }
        else {
            liftMotor.setPower(0);
        }

        if(gamepad1.a){
            brushMotor.setPower(1);
        }else if(gamepad1.x) {
            brushMotor.setPower(-1);
        }else{
            brushMotor.setPower(0);
        }

        if(gamepad1.dpad_left){
            hopperServo.setPosition(1);
        }else if(gamepad1.dpad_right){
            hopperServo.setPosition(.54);
        }else if(gamepad1.dpad_up){
            hopperServo.setPosition(.75);
        }


    }
}

