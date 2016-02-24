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
    DcMotor Sweeper2;
    DcMotor Winch;
    DcMotor Rack;
    Servo   ClimberLeft;
    Servo   ClimberRight;
    Servo   ClimberDump;

    final double ClimberLeftDown = 1.0;
    final double ClimberLeftUp   = 0.3;
    final double ClimberLeftStart = ClimberLeftUp;
    final double ClimberRightDown = 0.3;
    final double ClimberRightUp   = 1.0;
    final double ClimberRightStart = ClimberRightUp;
    final double ClimberDumpUp = 1.0;
    final double ClimberDumpDown = 0.0;


    @Override
    public void init() {
        //get references to the motors from the hardware map
        LeftA = hardwareMap.dcMotor.get("LeftA");
        RightA = hardwareMap.dcMotor.get("RightA");
        LeftB = hardwareMap.dcMotor.get("LeftB");
        RightB = hardwareMap.dcMotor.get("RightB");
        Winch = hardwareMap.dcMotor.get("Winch");
        Sweeper = hardwareMap.dcMotor.get("Sweeper");
        Sweeper2 = hardwareMap.dcMotor.get("Sweeper2");
        Rack = hardwareMap.dcMotor.get("Rack");
        ClimberLeft = hardwareMap.servo.get("ClimberLeft");
        ClimberRight = hardwareMap.servo.get("ClimberRight");
        ClimberDump = hardwareMap.servo.get("ClimberDump");
         //reverse the right motor
        RightA.setDirection(DcMotor.Direction.REVERSE);
        RightB.setDirection(DcMotor.Direction.REVERSE);
        ClimberDump.setPosition(ClimberDumpUp);
        ClimberRight.setPosition(ClimberRightUp);
        ClimberLeft.setPosition(ClimberLeftUp);
    }

    @Override
    public void loop() {
        //get the values from the gamepads
        //note: pushing the stick all the way up returns -1,
        // so we need to reverse the values
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        float leftY2 = -gamepad1.left_stick_y/2;
        float rightY2 = -gamepad1.right_stick_y/2;

        //set the power of the motors with the gamepad values
        LeftA.setPower(leftY2);
        LeftB.setPower(leftY);
        RightA.setPower(rightY2);
        RightB.setPower(rightY);

        if(gamepad1.b){
            Sweeper.setPower(1);
            Sweeper2.setPower(1);
        }else if(gamepad1.x){
            Sweeper.setPower(-1);
            Sweeper2.setPower(-1);
        }else if(gamepad1.a){
            Sweeper.setPower(0);
            Sweeper2.setPower(0);
        }

        if(gamepad2.a){
            Winch.setPower(.5);
            ClimberLeft.setPosition(ClimberLeftUp);
            ClimberRight.setPosition(ClimberRightUp);
        }else if (gamepad2.y || gamepad1.y){
            Winch.setPower(-.5);
            ClimberLeft.setPosition(ClimberLeftDown);
            ClimberRight.setPosition(ClimberRightDown);
        }else{
            Winch.setPower(0);
        }

        if (gamepad2.x){
            Rack.setPower(.5);
        }else if (gamepad2.b){
            Rack.setPower(-0.5);
        }else{
            Rack.setPower(0);
        }
        if(gamepad1.dpad_right) {
            ClimberLeft.setPosition(ClimberLeftUp);
        }else if(gamepad1.dpad_down){
            ClimberLeft.setPosition(ClimberLeftDown);
        }else if(gamepad1.dpad_up){
            ClimberLeft.setPosition(ClimberLeftStart);
        }

        if(gamepad2.dpad_left){
            ClimberRight.setPosition(ClimberRightDown);
        }else if(gamepad2.dpad_down) {
            ClimberRight.setPosition(ClimberRightUp);
        }else if(gamepad2.dpad_up){
            ClimberRight.setPosition(ClimberRightStart);
        }

        if(gamepad1.left_bumper){
            ClimberDump.setPosition(ClimberDumpUp);
        }else if(gamepad1.right_bumper){
            ClimberDump.setPosition(ClimberDumpDown);
        }

    }
}