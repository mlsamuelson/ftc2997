package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Steven on 9/28/2016.
 * Last edited on 10/3/2016
 */
 
@TeleOp(name="Test: Vortex MK3", group="Iterative Opmode");
 
public class firstVortexBotMK3 extends OpMode {
    DcMotor left_front;
    DcMotor right_front;
    DcMotor left_back;
    DcMotor right_back;

    @Override
    public void init() {
        right_front = hardwareMap.dcMotor.get("motor_2");
        left_front = hardwareMap.dcMotor.get("motor_1");
        right_back = hardwareMap.dcMotor.get("motor_3");
        left_back = hardwareMap.dcMotor.get("motor_4");
    }

    @Override
    public void loop() {
        float left_power;
        float right_power;

        float turn = (float)scaleInput(gamepad1.right_stick_y);

        left_power = turn;
        right_power = turn;

        left_front.setPower((float)scaleInput(left_power));
        left_back.setPower((float)scaleInput(left_power));
        right_front.setPower((float)scaleInput(right_power));
        right_back.setPower((float)scaleInput(right_power));
    }

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}
