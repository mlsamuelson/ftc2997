package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Steven on 9/28/2016.
 * Last edited on 11/30/2016
 */

@TeleOp(name="Test: Drive3", group="S Tests")

public class TEST_third_vortex_bot extends OpMode {
    // Declare variables
    DcMotor left_front;
    DcMotor right_front;
    DcMotor left_back;
    DcMotor right_back;
    //DcMotor intake;
    //DcMotor launcher;
    
    double intake_pow;
    double launcher_pow;
    boolean _drive;

    @Override
    public void init() {
        // Initiate motors
        right_front = hardwareMap.dcMotor.get("r_front");
        left_front = hardwareMap.dcMotor.get("l_front");
        right_back = hardwareMap.dcMotor.get("r_back");
        left_back = hardwareMap.dcMotor.get("l_back");
    //    intake = hardwareMap.dcMotor.get("intake");         // NOT SET IN CONFIGURATION
    //    launcher = hardwareMap.dcMotor.get("launcher");     // DITTO

        // Reverse right side
        right_front.setDirection(DcMotorSimple.Direction.REVERSE);
        right_back.setDirection(DcMotorSimple.Direction.REVERSE);

        // Initiate powers & drive
        intake_pow = 0;
        launcher_pow = 0;
        _drive = false;
        // Powers are initiated here to remove errors and also so that they don't
        //  reset to 0 every time the loop runs.
    }

    @Override
    public void loop() {
        // Using the information on the PDF, match the motors to fit with the sticks
        float drive = gamepad1.left_stick_y;
        float strafe = gamepad1.left_stick_x;
        float rotate = gamepad1.right_stick_x;

        float fl_pow = drive - strafe + rotate;
        float bl_pow = drive + strafe + rotate;
        float fr_pow = drive + strafe - rotate;
        float br_pow = drive - strafe - rotate;

        // Use gamepad buttons to turn the intake on (X),off (B), and reverse (Y)
        // NOT TESTED due to configuration problems
        if (gamepad1.x) {
            intake_pow = 0.5;
        } else if (gamepad1.b) {
            intake_pow = 0;
        }

        if (gamepad1.y){
            intake_pow = Math.abs(intake_pow) * -1;
        } else {
            intake_pow = Math.abs(intake_pow);
        }

        // Use the gamepad bumpers to turn the launcher on and off
        if (gamepad1.right_bumper) {
            launcher_pow = 0.5;
        } else {
            launcher_pow = 0;
        }

        // Scale the inputs to fit the motors
        fl_pow = (float) scaleInput(fl_pow);
        bl_pow = (float) scaleInput(bl_pow);
        fr_pow = (float) scaleInput(fr_pow);
        br_pow = (float) scaleInput(br_pow);

        intake_pow = (float) scaleInput(intake_pow);
        launcher_pow = (float) scaleInput(launcher_pow);

        // Assign values
        left_front.setPower(fl_pow);
        left_back.setPower(bl_pow);
        right_front.setPower(fr_pow);
        right_back.setPower(br_pow);

        //intake.setPower(intake_pow);
        //launcher.setPower(launcher_pow);

        // Show values
        telemetry.addData("1. Right stick X", gamepad1.right_stick_x);
        telemetry.addData("2. Left stick X", gamepad1.left_stick_x);
        telemetry.addData("3. Left stick Y", gamepad1.left_stick_y);

        // Add space
        telemetry.addData("4. ", "");

        // Show power values
        telemetry.addData("5. Front Left:", fl_pow);
        telemetry.addData("6. Front Right:", fr_pow);
        telemetry.addData("7. Back Left:", bl_pow);
        telemetry.addData("8. Back Right:", br_pow);
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
