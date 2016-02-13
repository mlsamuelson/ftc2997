package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.LED;


/**
 Created by: Steven Schofield
 Date of creation: 11/13/2015
 Date last modified: 2/12/2016
 **/

public class BBotStevenBaseCode extends OpMode {
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

    public void init(){
        // Set variable value
        lMotor = hardwareMap.dcMotor.get("leftMotor");
        rMotor = hardwareMap.dcMotor.get("rightMotor");
        rMotor.setDirection(DcMotor.Direction.REVERSE);

        state = State.forewords;
        // These will change. They are listed here so this program can run by itself.
        turnWay1 = 0;
        turnWay2 = 0;

        // These are the sensor values that mark the range of values
        mid_color_grey = 1;     // Between grey and the team color
        mid_color_white = 2;    // Between the team color and white
        // This is assuming that white has the highest value and that grey has the lowest
    }
    @Override
    public void loop() {
        // Set two reflection variables

        // They are defined outside of the state so they can be used everywhere
        // They are defined inside the loop so that they get defined over and over
        // There is one on the right, (1), and one on the left (2).
        switch(state){
            case forewords:
                // If either reflection is not on the ground...
                if (mid_color_grey < color_value_LEFT &&
                    color_value_LEFT < mid_color_white ||
                    mid_color_grey < color_value_RIGHT &&
                    color_value_RIGHT < mid_color_white){
                    // ...then change the state to lineFollow
                    state = State.lineFollow;
                } else {
                    // Otherwise, move forwards
                    lMotor.setPower(1);
                    rMotor.setPower(1);
                }
                break;
            case lineFollow:
                if (mid_color_grey < color_value_LEFT &&
                    color_value_LEFT < mid_color_white &&
                    mid_color_grey < color_value_RIGHT &&
                    color_value_RIGHT < mid_color_white){
                    // If BOTH sensors are on the line, then the program can continue.
                    state = State.end;

                    // Otherwise, if either reflection is on the line...
                } else if (mid_color_grey < color_value_LEFT &&
                           color_value_LEFT < mid_color_white ||
                           mid_color_grey < color_value_RIGHT &&
                           color_value_RIGHT < mid_color_white){
                    // ... then turn in the "first" direction.
                    lMotor.setPower(turnWay1);
                    rMotor.setPower(turnWay2);

                    // Otherwise, if neither reflection is on the line/both are on the ground.
                } else if (!(mid_color_grey < color_value_LEFT &&
                            color_value_LEFT < mid_color_white &&
                            mid_color_grey < color_value_RIGHT &&
                            color_value_RIGHT < mid_color_white)){
                    // ... then turn in the "second" direction
                    lMotor.setPower(turnWay2);
                    rMotor.setPower(turnWay1);
                }
                break;
            case end:
                lMotor.setPower(0);
                rMotor.setPower(0);
                break;
        }
    }
}
// http://ftc.edu.intelitek.com/mod/scorm/player.php