//Robot needs to drive (2 motors) and motor on arm needs to spin
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


public class BBotsTeleopArcadeMode extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor liftMotor;
    DcMotor brushMotor;
    Servo hopperServo;


    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        brushMotor = hardwareMap.dcMotor.get("brushMotor");
        hopperServo = hardwareMap.servo.get("hopperServo");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        //get the values from the gamepads
        //note: pushing the stick all the way up returns -1,
        // so we need to reverse the y values
        float xValue = gamepad1.left_stick_x;
        float yValue = -gamepad1.left_stick_y;

        //calculate the power needed for each motor
        float leftPower = yValue + xValue;
        float rightPower = yValue - xValue;

        //clip the power values so that it only goes from -1 to 1
        leftPower = Range.clip(leftPower, -1, 1);
        rightPower = Range.clip(rightPower, -1, 1);

        //set the power of the motors with the gamepad values
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);

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
            hopperServo.setPosition(0);
        }

    }
}

