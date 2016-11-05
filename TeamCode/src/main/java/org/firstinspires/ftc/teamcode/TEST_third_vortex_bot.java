package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Steven on 9/28/2016.
 * Last edited on 11/5/2016
 */
 
@TeleOp(name="Test: Vortex MK3", group="Iterative Opmode");
 
public class firstVortexBotMK3 extends OpMode {
    // Initialize variables
    DcMotor left_front;
    DcMotor right_front;
    DcMotor left_back;
    DcMotor right_back;

    @Override
    public void init() {
        // Set motors
        right_front = hardwareMap.dcMotor.get("motor_2");
        left_front = hardwareMap.dcMotor.get("motor_1");
        right_back = hardwareMap.dcMotor.get("motor_3");
        left_back = hardwareMap.dcMotor.get("motor_4");
    }

    @Override
    public void loop() {
        // Using the information on the PDF, match the motors to fit with the sticks
        float drive = 1-gamepad1.left_stick_y;
        float strafe = gamepad1.left_stick_x;
        float rotate = gamepad1.right_stick_x;
        
        float fl_pow = drive + strafe + rotate;
        float bl_pow = drive - strafe + rotate;
        float fr_pow = drive - strafe - rotate;
        float br_pow = drive + strafe - rotate;
        
        // Scale the inputs to fit the motors
        fl_pow = scaleInput(fl_pow);
        bl_pow = scaleInput(bl_pow);
        fr_pow = scaleInput(fr_pow);
        br_pow = scaleInput(br_pow);
        
        // Assign values
        left_front.setPower(fl_pow);
        left_back.setPower(bl_pow);
        right_front.setPower(fr_pow);
        right_back.setPower(br_pow);
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
