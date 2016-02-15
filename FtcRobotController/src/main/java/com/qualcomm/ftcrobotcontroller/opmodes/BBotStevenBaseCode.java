package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.hardware.ColorSensor;


/**
 Created by: Steven Schofield
 Date of creation: 11/13/2015
 Date last modified: 2/12/2016
 **/

public class BBotStevenBaseCode extends OpMode {
    // Declare variables
    DcMotor lMotor;
    DcMotor rMotor;
    DcMotor lMotor2;
    DcMotor rMotor2;

    int turnWay1;
    int turnWay2;
    int MAX_COLOR;
    int color_value_LEFT;
    int color_value_RIGHT;

    enum State {FOREWARDS, LINE_FOLLOW, END}
    State state;

    ColorSensor sensorRGBL;
    ColorSensor sensorRGBR;

    public void init(){
        // Set variable value
        lMotor = hardwareMap.dcMotor.get("LeftA");
        rMotor = hardwareMap.dcMotor.get("RightA");
        lMotor2 = hardwareMap.dcMotor.get("LeftB");
        rMotor2 = hardwareMap.dcMotor.get("RightB");

        // Reverse the LEFT motors because the robot is running backwards
        lMotor.setDirection(DcMotor.Direction.REVERSE);
        lMotor2.setDirection(DcMotor.Direction.REVERSE);

        state = State.FOREWARDS;
        // These will change. They are listed here so this program can run by itself.
        turnWay1 = 1;
        turnWay2 = 1/2;

        // get a reference to our ColorSensor object.
        sensorRGBL = hardwareMap.colorSensor.get("MRColor_left");
        sensorRGBR = hardwareMap.colorSensor.get("MRColor_right");

        // Change one color sensor to a new address
        // A core device discovery program was used to change this.
        sensorRGBR.setI2cAddress(0x70);

        // Enable the LED lights on both sensors


        // These are the sensor values that mark the range of values
        MAX_COLOR = 6;

        sensorRGBR.enableLed(false);
        sensorRGBL.enableLed(false);
    }

    @Override
    public void loop() {
        sensorRGBR.enableLed(true);
        sensorRGBL.enableLed(true);
        // Find the sum of the Red, Green, and Blue that the sensors are currently showing
        color_value_LEFT = sensorRGBL.red()+sensorRGBL.blue();
        color_value_RIGHT = sensorRGBR.red()+sensorRGBR.blue()-3;
        // They are defined outside of the state so they can be used everywhere
        // They are defined inside the loop so that they get defined over and over

        switch(state){
            case FOREWARDS:
                // If either sensor is on the line...
                if (color_value_LEFT < MAX_COLOR ||
                    color_value_RIGHT < MAX_COLOR){
                    // ...then change the state to lineFollow
                    state = State.LINE_FOLLOW;
                } else {
                    // Otherwise, move forwards
                    /*lMotor.setPower(0.5);
                    rMotor.setPower(0.5);
                    lMotor2.setPower(0.5);
                    rMotor2.setPower(0.5);*/
                }
                break;
            case LINE_FOLLOW:
                if (color_value_LEFT < MAX_COLOR &&
                    color_value_RIGHT < MAX_COLOR){
                    // If BOTH sensors are on the line, then the program can continue.
                    state = State.END;

                    // Otherwise, if either sensor is on the line...
                } else if (color_value_LEFT < MAX_COLOR ||
                           color_value_RIGHT < MAX_COLOR){
                    // ... then turn in the "first" direction.
                    /*lMotor.setPower(turnWay1);
                    rMotor.setPower(turnWay2);
                    lMotor2.setPower(turnWay1);
                    rMotor2.setPower(turnWay2);*/
                    telemetry.addData("EITHER SENSOR IS ON LINE! Left", color_value_LEFT);
                    telemetry.addData("Right", color_value_RIGHT);

                    // Otherwise, if neither sensor are on the line/both are on the ground.
                } else if (color_value_LEFT <= MAX_COLOR &&
                           color_value_RIGHT <= MAX_COLOR){
                    // ... then turn in the "second" direction
                    telemetry.addData("NEITHER SENSOR ARE ON THE LINE! Left", color_value_LEFT);
                    telemetry.addData("Right", color_value_RIGHT);
                    /*lMotor.setPower(turnWay2);
                    rMotor.setPower(turnWay1);
                    lMotor2.setPower(turnWay2);
                    rMotor2.setPower(turnWay1);*/
                }
                break;
            case END:
                telemetry.addData("Left", color_value_LEFT);
                telemetry.addData("Right", color_value_RIGHT);
                lMotor.setPower(0);
                rMotor.setPower(0);
                lMotor2.setPower(0);
                rMotor2.setPower(0);
                break;
        }
        telemetry.addData("state", state);
    }
}
// http://ftc.edu.intelitek.com/mod/scorm/player.php