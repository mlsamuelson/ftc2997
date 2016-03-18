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

    float turnWay1;
    float turnWay2;

    int color_LEFT;
    int color_RIGHT;

    int wait;

    enum State {WAIT, FOREWARDS, LINE_FOLLOW, END}
    State state;

    ColorSensor sensorRGBL;
    ColorSensor sensorRGBR;

    public void init(){
        // Set variable value
        lMotor = hardwareMap.dcMotor.get("LeftA");
        rMotor = hardwareMap.dcMotor.get("RightA");
        lMotor2 = hardwareMap.dcMotor.get("LeftB");
        rMotor2 = hardwareMap.dcMotor.get("RightB");

        // Initialise the wait variable
        wait = 0;

        // Reverse the LEFT motors because the robot is running backwards
        lMotor.setDirection(DcMotor.Direction.REVERSE);
        lMotor2.setDirection(DcMotor.Direction.REVERSE);

        state = State.WAIT;
        // These will change. They are listed here so this program can run by itself.
        turnWay1 = 1/2;
        turnWay2 = 1;
        // TURN WAYS SET FOR BLUE TEAM!!!!!

        // get a reference to our ColorSensor object.
        sensorRGBL = hardwareMap.colorSensor.get("MRColor_left");
        sensorRGBR = hardwareMap.colorSensor.get("MRColor_right");

        // Change one color sensor to a new address
        // A core device discovery program was used to change this.
        sensorRGBR.setI2cAddress(0x70);

        // Enable the LED lights on both sensors
        sensorRGBR.enableLed(true);
        sensorRGBL.enableLed(true);
    }

    @Override
    public void loop() {
        if (wait == 0){
            this.resetStartTime();
            wait ++;
        }

        // Find if one color sensor is on red/blue or not.
        // 1 is on red/blue. 0 is not.
        // For the If Statements, the first number is between red & gray,
        // Second number is between blue and gray
        // If needed, blue() can be changed to green(), red(), or alpha()
        // First set of If Statements are for the right, second for the left
        // Note: zClear, Hue, and Alpha are all the same.
        if (sensorRGBR.blue() <= 2 || sensorRGBR.blue() >= 6){
            color_RIGHT = 1;
        } else {
            color_RIGHT = 0;
        }
        if (sensorRGBL.blue() <= 0.5 || sensorRGBL.blue() >= 4){
            color_LEFT = 1;
        } else {
            color_LEFT = 0;
        }
        switch(state){
            case WAIT:
                if (this.getRuntime() > 9.0){
                    state = State.FOREWARDS;
                }
                break;
            case FOREWARDS:
                // If either sensor is on the line...
                if (color_LEFT == 1 || color_RIGHT < 1){
                    // ...then change the state to lineFollow
                    state = State.LINE_FOLLOW;
                } else {
                    // Otherwise, move forwards
                    lMotor.setPower(0.5);
                    rMotor.setPower(0.5);
                    lMotor2.setPower(0.5);
                    rMotor2.setPower(0.5);
                }
                break;
            case LINE_FOLLOW:
                if (color_LEFT == 1 && color_RIGHT == 1){
                    // If BOTH sensors are on the line, then the program can continue.
                    state = State.END;

                    // Otherwise, if either sensor is on the line...
                } else if (color_LEFT == 1 || color_RIGHT == 1){
                    // ... then turn in the "first" direction.
                    lMotor.setPower(turnWay1);
                    rMotor.setPower(turnWay2);
                    lMotor2.setPower(turnWay1);
                    rMotor2.setPower(turnWay2);

                    // Otherwise, if neither sensor are on the line/both are on the ground.
                } else if (color_LEFT == 0 && color_RIGHT == 0){
                    // ... then turn in the "second" direction
                    lMotor.setPower(turnWay2);
                    rMotor.setPower(turnWay1);
                    lMotor2.setPower(turnWay2);
                    rMotor2.setPower(turnWay1);
                }
                break;
            case END:
                lMotor.setPower(0);
                rMotor.setPower(0);
                lMotor2.setPower(0);
                rMotor2.setPower(0);
                break;
        }
        telemetry.addData("state", state);
        telemetry.addData("Left", color_LEFT);
        telemetry.addData("Right", color_RIGHT);

        telemetry.addData("L blue", sensorRGBL.blue());
        telemetry.addData("L green", sensorRGBL.green());
        telemetry.addData("L red", sensorRGBL.red());
        telemetry.addData("L zClear", sensorRGBL.alpha());

        telemetry.addData("R blue", sensorRGBR.blue());
        telemetry.addData("R green", sensorRGBR.green());
        telemetry.addData("R red", sensorRGBR.red());
        telemetry.addData("R zClear", sensorRGBR.alpha());
    }
}
// http://ftc.edu.intelitek.com/mod/scorm/player.php