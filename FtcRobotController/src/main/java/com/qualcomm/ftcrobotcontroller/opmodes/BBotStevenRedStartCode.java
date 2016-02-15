package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Steven Schofield on 2/6/2016.
 * Date last modified: 2/15/2016
 */
public class BBotStevenRedStartCode extends BBotStevenBaseCode{
    // Declare variables
    DcMotor lMotor;
    DcMotor rMotor;

    ColorSensor sensorRGBL;
    ColorSensor sensorRGBR;

    int turnWay1;
    int turnWay2;
    int mid_color_grey;
    int mid_color_white;
    //int color_value_LEFT;
    //int color_value_RIGHT;

    enum State {forewords, lineFollow, end}
    State state;

    @Override
    public void init(){
        // Set variable value
        lMotor = hardwareMap.dcMotor.get("leftMotor");
        rMotor = hardwareMap.dcMotor.get("rightMotor");
        rMotor.setDirection(DcMotor.Direction.REVERSE);

        state = State.forewords;

        turnWay1 = 1;
        turnWay2 = 0;

        // get a reference to our ColorSensor object.
        sensorRGBL = hardwareMap.colorSensor.get("MRColor_left");
        sensorRGBR = hardwareMap.colorSensor.get("MRColor_right");

        // Change one color sensor to a new address
        // A core device discovery program was used to change this.
        sensorRGBR.setI2cAddress(0x70);

        // Enable the LED lights on both sensors
        sensorRGBR.enableLed(true);
        sensorRGBL.enableLed(true);

        // These are the sensor values that mark the range of values
        mid_color_grey = 6;     // Between grey and the team color
        mid_color_white = 53;    // Between the team color and white
    }
}
