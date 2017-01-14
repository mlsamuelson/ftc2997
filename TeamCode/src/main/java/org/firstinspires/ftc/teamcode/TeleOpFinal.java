/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


/**
 * Created by Steven on: 12/13/2016
 * Last edited on: 12/13/2016
 */

@TeleOp(name="TeleOp Final", group="USE")

public class TeleOpFinal extends OpMode {
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

    @Override
    public void init() {
        // Initiate motors
        right_front = hardwareMap.dcMotor.get("r_front");
        left_front = hardwareMap.dcMotor.get("l_front");
        right_back = hardwareMap.dcMotor.get("r_back");
        left_back = hardwareMap.dcMotor.get("l_back");
        intake = hardwareMap.dcMotor.get("collect");
        launcher = hardwareMap.dcMotor.get("launch");

        // Reverse left side
        left_front.setDirection(DcMotorSimple.Direction.REVERSE);
        left_back.setDirection(DcMotorSimple.Direction.REVERSE);

        // Initiate powers & drive
        intake_pow = 0;
        launcher_pow = 0;
        _drive = 0;
    }

    @Override
    public void loop() {
        float driveLEFT = gamepad1.left_stick_y;
        float strafeLEFT = gamepad1.left_stick_x;
        float driveRIGHT = gamepad1.right_stick_y;
        float strafeRIGHT = gamepad1.right_stick_x;

        float fl_pow = driveLEFT - strafeLEFT;
        float bl_pow = driveLEFT + strafeLEFT;
        float fr_pow = driveRIGHT + strafeRIGHT;
        float br_pow = driveRIGHT - strafeRIGHT;

        // Use gamepad buttons to turn the intake on (X),off (B), and reverse (Y)
        if (gamepad2.x) {
            _drive = 1;
        } else if (gamepad2.b) {
            _drive = 0;
        }

        if (_drive == 1) {
            intake_pow = -0.5;
        } else if (_drive == 0) {
            intake_pow = 0;
        }

        if (gamepad2.y &&_drive == 1) {
            intake_pow = 0.5;
        }

        // Use the gamepad bumpers to turn the launcher on and off
        if (gamepad2.right_bumper) {
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

        intake.setPower(intake_pow);
        launcher.setPower(launcher_pow);
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
