package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


public class Yoga_Bot extends OpMode {

    DcMotor LeftA;
    DcMotor RightA;
    DcMotor LeftB;
    DcMotor RightB;
    DcMotor Sweeper;
    DcMotor Winch;
    DcMotor Rack;
    Servo   ClimberLeft;
    Servo   ClimberRight;

    final double ClimberLeftDown = 0.3;
    final double ClimberLeftUp   = 0.5;
    final double ClimberRightDown = 0.5;
    final double ClimberRightUp   = 0.3;



    @Override
    public void init() {
        //get references to the motors from the hardware map
        Rack = hardwareMap.dcMotor.get("Rack");
        LeftA = hardwareMap.dcMotor.get("LeftA");
        RightA = hardwareMap.dcMotor.get("RightA");
        LeftB = hardwareMap.dcMotor.get("LeftB");
        RightB = hardwareMap.dcMotor.get("RightB");
        Winch = hardwareMap.dcMotor.get("Winch");
        Sweeper = hardwareMap.dcMotor.get("Sweeper");
        ClimberLeft = hardwareMap.servo.get("ClimberLeft");
        ClimberRight = hardwareMap.servo.get("ClimberRight");
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

        float leftY2 = -gamepad2.left_stick_y/2;
        float rightY2 = -gamepad2.right_stick_y/2;

        //set the power of the motors with the gamepad values
        LeftA.setPower(leftY);
        LeftB.setPower(leftY);
        RightA.setPower(rightY);
        RightB.setPower(rightY);

        LeftA.setPower(leftY2);
        LeftB.setPower(leftY2);
        RightA.setPower(rightY2);
        RightB.setPower(rightY2);

        if(gamepad1.x){
            Sweeper.setPower(1);
        }else if(gamepad1.b){
            Sweeper.setPower(-1);
        }else{
            Sweeper.setPower(0);
        }

        if(gamepad2.a){
            Winch.setPower(.5);
        }else if(gamepad2.y){
            Winch.setPower(-.5);
        }else{
            Winch.setPower(0);
        }

        if(gamepad2.x){
            Rack.setPower(.5);
        }else if(gamepad2.b){
            Rack.setPower(-0.5);
        }else{
            Rack.setPower(0);
        }
        if(gamepad1.left_bumper) {
            ClimberLeft.setPosition(ClimberLeftDown);
        }else if(gamepad1.left_trigger > 0 ){
            ClimberLeft.setPosition(ClimberLeftUp);
        }
        if(gamepad1.right_bumper) {
            ClimberRight.setPosition(ClimberRightUp);
        }else if(gamepad1.right_trigger > 0) {
            ClimberRight.setPosition(ClimberRightDown);
        }

    }
}