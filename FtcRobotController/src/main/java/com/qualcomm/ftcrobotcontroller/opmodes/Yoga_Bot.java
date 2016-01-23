//Robot needs to drive (2 motors) and motor on arm needs to spin
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;



public class Yoga_Bot extends OpMode {

    DcMotor LeftA;
    DcMotor RightA;
    DcMotor LeftB;
    DcMotor RightB;
    DcMotor Sweeper;
    DcMotor Lift;
    Servo   Basket;

    @Override
    public void init() {
        //get references to the motors from the hardware map
        Basket = hardwareMap.servo.get("Basket");
        LeftA = hardwareMap.dcMotor.get("LeftA");
        RightA = hardwareMap.dcMotor.get("RightA");
        LeftB = hardwareMap.dcMotor.get("LeftB");
        RightB = hardwareMap.dcMotor.get("RightB");
        Lift = hardwareMap.dcMotor.get("Lift");
        Sweeper = hardwareMap.dcMotor.get("Sweeper");
         //reverse the right motor
        RightA.setDirection(DcMotor.Direction.REVERSE);
        RightB.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        //get the values from the gamepads
        //note: pushing the stick all the way up returns -1,
        // so we need to reverse the values
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        //set the power of the motors with the gamepad values
        LeftA.setPower(leftY);
        LeftB.setPower(leftY);
        RightA.setPower(rightY);
        RightB.setPower(rightY);

        if(gamepad1.x){
            Sweeper.setPower(1);
        }else if(gamepad1.b){
            Sweeper.setPower(-1);
        }else{
            Sweeper.setPower(0);
        }

        if(gamepad1.a){
            Lift.setPower(.5);
        }else if(gamepad1.y){
            Lift.setPower(-.5);
        }else{
            Lift.setPower(0);
        }

        if(gamepad1.dpad_right){
            Basket.setPosition(1);
        }else if(gamepad1.dpad_left){
            Basket.setPosition(-1);
        }else if(gamepad1.dpad_down){
            Basket.setPosition(0);
        }

    }
}
