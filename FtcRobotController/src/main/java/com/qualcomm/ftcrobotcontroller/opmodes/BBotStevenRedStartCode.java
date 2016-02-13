package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.ftcrobotcontroller.opmodes.BBotStevenBaseCode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Steven Schofield on 2/6/2016.
 * Date last modified: 2/6/2016
 */
public class BBotStevenRedStartCode extends BBotStevenBaseCode{
    // Declare variables
    DcMotor lMotor;
    DcMotor rMotor;

    int turnWay1;
    int turnWay2;
    int mid_color_grey;
    int mid_color_white;
    int color_value_LEFT;
    int color_value_RIGHT;

    enum State {forewords, lineFollow, end}
    State state;

    @Override
    public void init(){
        // Set variable value
        lMotor = hardwareMap.dcMotor.get("leftMotor");
        rMotor = hardwareMap.dcMotor.get("rightMotor");
        rMotor.setDirection(DcMotor.Direction.REVERSE);

        state = State.forewords;

        turnWay1 = 0;
        turnWay2 = 1;

        // These are the sensor values that mark the range of values
        mid_color_grey = 1;     // Between grey and the team color
        mid_color_white = 2;    // Between the team color and white
        // This is assuming that white has the higher value
    }
}
