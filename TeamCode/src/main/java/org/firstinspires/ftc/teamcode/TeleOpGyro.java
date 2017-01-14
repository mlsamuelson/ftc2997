package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Steven on 12/14/2016.
 * Last edited on 12/14/2016
 */

@TeleOp(name="Test: GyroDrive3", group="USE")

public class TeleOpGyro extends OpMode {
    // Declare variables
    DcMotor left_front;
    DcMotor right_front;
    DcMotor left_back;
    DcMotor right_back;
    DcMotor intake;
    DcMotor launcher;
    
    double intake_pow;
    double launcher_pow;
    int _drive;

    ModernRoboticsI2cGyro gyro;   // Hardware Device Object
    int xVal, yVal, zVal = 0;     // Gyro rate Values
    int heading = 0;              // Gyro integrated heading
    int angleZ = 0;
    boolean lastResetState = false;
    boolean curResetState  = false;

    @Override
    public void init() {
        // Initiate motors
        right_front = hardwareMap.dcMotor.get("r_front");
        left_front = hardwareMap.dcMotor.get("l_front");
        right_back = hardwareMap.dcMotor.get("r_back");
        left_back = hardwareMap.dcMotor.get("l_back");
        intake = hardwareMap.dcMotor.get("collect");
        launcher = hardwareMap.dcMotor.get("launch");

        // Reverse right side
        right_front.setDirection(DcMotorSimple.Direction.REVERSE);
        right_back.setDirection(DcMotorSimple.Direction.REVERSE);

        // Initiate powers & drive
        intake_pow = 0;
        launcher_pow = 0;
        _drive = 0;

        // Initiate Gyro
        gyro.calibrate();

        while (gyro.isCalibrating()){
            telemetry.addData(">", "Calibrating Gyro...");
            telemetry.update();
        }

        telemetry.addData(">", "Gyro Calibrated");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Use gamepad buttons to turn the intake on (X),off (B), and reverse (Y)
        if (gamepad2.y && _drive == 1) {
            intake_pow = 0.5;
        } else {
            intake_pow = -0.5;
        }

        if (gamepad2.x) {
            _drive = 1;
            intake_pow = -0.5;
        } else if (gamepad2.b) {
            _drive = 0;
            intake_pow = 0;
        }

        // Use the gamepad bumpers to turn the launcher on and off
        if (gamepad2.right_bumper) {
            launcher_pow = 0.5;
        } else {
            launcher_pow = 0;
        }

        // Using the information on the PDF, match the motors to fit with the sticks
        float drive = gamepad1.left_stick_y;
        float rotate = gamepad1.right_stick_x;

        // get the x, y, and z values (rate of change of angle).
        xVal = gyro.rawX();
        yVal = gyro.rawY();
        zVal = gyro.rawZ();

        // get the heading info.
        heading = gyro.getHeading();
        angleZ  = gyro.getIntegratedZValue();

        float fl_pow = drive + rotate;
        float bl_pow = drive + rotate;
        float fr_pow = drive - rotate;
        float br_pow = drive - rotate;

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

        intake.setPower(intake_pow);
        launcher.setPower(launcher_pow);

        telemetry.addData(">", "Press A & B to reset Heading.");
        telemetry.addData("0", "Heading %03d", heading);
        telemetry.addData("1", "Int. Ang. %03d", angleZ);
        telemetry.addData("2", "X av. %03d", xVal);
        telemetry.addData("3", "Y av. %03d", yVal);
        telemetry.addData("4", "Z av. %03d", zVal);
        telemetry.update();
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
