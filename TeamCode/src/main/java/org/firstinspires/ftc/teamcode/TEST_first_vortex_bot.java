package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Steven on 9/14/2016.
 * Last edited on 10/3/2016
 */
 
@TeleOp(name="Test: Vortex MK1", group="Iterative Opmode");

public class firstVortexBot extends OpMode {
    // Initialize DcMotor variables
    DcMotor left_front;
    DcMotor right_front;
    DcMotor left_back;
    DcMotor right_back;
    DcMotor other;
    
    // Code that runs when the Init button is pressed
    @Override
    public void init() {
        // Assign the motors to the DcMotor variables
        right_front = hardwareMap.dcMotor.get("motor_2");
        left_front = hardwareMap.dcMotor.get("motor_1");
        right_back = hardwareMap.dcMotor.get("motor_3");
        left_back = hardwareMap.dcMotor.get("motor_4");
        other = hardwarePam.dcMotor.get("motor_5");
    }
    
    // Code that runs after the play button is pressed
    @Override
    public void loop() {
        // If either analog stick is up/down... (y value is not 0)
        if (gamepad1.left_stick_y != 0 || gamepad1.right_stick_y != 0) {
            // Create variables to hold the Y values of the analog sticks
            float left_power = -gamepad1.left_stick_y;
            float right_power = gamepad1.right_stick_y;
            
            // Constrain the analog stick values between -1 (up) and 1(down)
            left_power = Range.clip(left_power, -1, 1);
            right_power = Range.clip(right_power, -1, 1);
            
            // Change the analog stick values into power settings
            left_power = (float)scaleInput(left_power);
            right_power = (float)scaleInput(right_power);
            
            // Set the power to the motors
            left_front.setPower(left_power);
            left_back.setPower(left_power);
            right_front.setPower(right_power);
            right_back.setPower(right_power);
        } else { // Otherwise (both sticks are not up nor down)...
            // Create variables that hold the X values of the analog sticks
            float left_power = gamepad1.left_stick_x;
            float right_power = gamepad1.right_stick_x;
            
            // Constrain the values between -1 (?) and 1 (?)
            left_power = Range.clip(left_power, -1, 1);
            right_power = Range.clip(right_power, -1, 1);
            
            // Change the analog stick values into power settings
            left_power = (float) scaleInput(left_power);
            right_power = (float) scaleInput(right_power);
            
            // Set the motor power (negating some values for strafing)
            left_front.setPower(-left_power);
            left_back.setPower(left_power);
            right_front.setPower(right_power);
            right_back.setPower(-right_power);
        }
        
        // If the right bumper is pressed...
        if (gamepad1.right_bumper) {
            // Set the power of the other motor
            other.setPower(0.5);
        } else { // Otherwise...
            // Remove the power.
            other.setPower(0);
        }
    }
    
    // Define the scaleInput function/class
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