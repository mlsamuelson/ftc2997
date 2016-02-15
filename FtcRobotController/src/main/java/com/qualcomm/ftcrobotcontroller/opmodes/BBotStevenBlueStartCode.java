package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Steven Schofield on 2/6/2016.
 * Date last modified: 2/15/2016
 */
public class BBotStevenBlueStartCode extends BBotStevenBaseCode {
    // Declare variables
    DcMotor lMotor;
    DcMotor rMotor;
    DcMotor lMotor2;
    DcMotor rMotor2;

    int turnWay1;
    int turnWay2;
    int mid_color_grey;
    int mid_color_white;

    enum State {FOREWARDS}
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
        // This is assuming that white has the highest value and that grey has the lowest
    }
}