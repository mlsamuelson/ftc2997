/*
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

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name="Autonomous Gyro", group="Test")

public class Auto_Gyro extends LinearOpMode {

    /* Declare OpMode members. */

    ModernRoboticsI2cGyro   gyro    = null;                    // Additional Gyro device

    static final double     COUNTS_PER_MOTOR_REV    = 1120;    // eg: TETRIX Motor Encoder (1440)
    static final double     DRIVE_GEAR_REDUCTION    = 1.0;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);

    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.
    static final double     DRIVE_SPEED             = 0.4;     // Nominal speed for better accuracy.
    static final double     TURN_SPEED              = 0.2;     // Nominal half speed for better accuracy.

    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;    // Larger is more responsive, but also less stable

    DcMotor left_front;
    DcMotor right_front;
    DcMotor left_back;
    DcMotor right_back;

    boolean vuforiaGoal = false;

    @Override
    public void runOpMode() {

        /*
         * Initialize the standard drive system variables.
         */
        right_front = hardwareMap.dcMotor.get("r_front");
        left_front = hardwareMap.dcMotor.get("l_front");
        right_back = hardwareMap.dcMotor.get("r_back");
        left_back = hardwareMap.dcMotor.get("l_back");

        right_front.setDirection(DcMotorSimple.Direction.REVERSE);
        right_back.setDirection(DcMotorSimple.Direction.REVERSE);

        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");

        // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
        right_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Send telemetry message to alert driver that we are calibrating;
        telemetry.addData(">", "Calibrating Gyro");    //
        telemetry.update();

        gyro.calibrate();

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && gyro.isCalibrating())  {
            sleep(50);
            idle();
        }

        telemetry.addData(">", "Robot Ready.");
        telemetry.update();

        right_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set up Vuforia
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AT99xn7/////AAAAGba3Op9lP005p/1muRdA/Lpgd4QiKqy2xzEQL8ZIPxNHzLiWcz1AEATHuJZxfH2diokQKqBuPLkgEVi7HvDJoX9szJym+MhmXjakDmxEODsoeS3V5V7d2DBN1aC1+ekS1C31/QopnqiSKgt8XB0voGrLQ+i9D+6ZVkfhZ2b5Jc6JC7U3r7d2PuVtoWRjv4tDFdQa3fjgdZnTthcOv16LOfoOBrTY3KhMczqewqfPCs+fqGxYU8hdkgNOtIreRyMW2WZH6ZZovg62bVBM2yHRuaalfzyYdRDs1FkExR8V5QSD4dDHPwv5ITnWTPIgrwtEVjEk1+kpBHXJpuTm0vlIzO7h1Y7bU4wLQRnGLH8u/wTv";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.TEAPOT;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");

        VuforiaTrackableDefaultListener wheels = (VuforiaTrackableDefaultListener) beacons.get(0).getListener();
        VuforiaTrackableDefaultListener tools = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();
        VuforiaTrackableDefaultListener lego = (VuforiaTrackableDefaultListener) beacons.get(2).getListener();
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();

        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
        while (!isStarted()) {
            telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
            telemetry.update();
            idle();
        }
        gyro.resetZAxisIntegrator();

        // Activate beacons (vuforia)
        beacons.activate();

        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // Put a hold after each turn

        // KNOCK OFF CAP BALL
        gyroDrive(DRIVE_SPEED, -45.0, 0);      // Drive FWD 72 inches

        // FIND BEACON (Vuforia)
        while (opModeIsActive() && !vuforiaGoal) {
            VectorF angles = anglesFromTarget(wheels);
            VectorF trans = navOffWall(wheels.getPose().getTranslation(), Math.toDegrees(angles.get(0))-90, new VectorF(0, 0, 0));

            if (trans.get(0) > 0){
                left_front.setPower(0.01);
                left_back.setPower(0.01);
                right_front.setPower(-0.01);
                right_back.setPower(-0.01);
            } else if (trans.get(0) < 0){
                left_front.setPower(-0.01);
                left_back.setPower(-0.01);
                right_front.setPower(0.01);
                right_back.setPower(0.01);
            } else {
                vuforiaGoal = true;
            }
        }

        left_front.setPower(0);
        left_back.setPower(0);
        right_front.setPower(0);
        right_back.setPower(0);

        // DRIVE TO BEACON (Distance sensor)
        // PRESS BEACON BUTTON (Color sensor)
        // MOVE TO OTHER BEACON (Vuforia?)

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

   /**
    *  Method to drive on a fixed compass bearing (angle), based on encoder counts.
    *  Move will stop if either of these conditions occur:
    *  1) Move gets to the desired position
    *  2) Driver stops the opmode running.
    *
    * @param speed      Target speed for forward motion.  Should allow for _/- variance for adjusting heading
    * @param distance   Distance (in inches) to move from current position.  Negative distance means move backwards.
    * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
    *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
    *                   If a relative angle is required, add/subtract from current heading.
    */
    public void gyroDrive ( double speed, double distance, double angle) {

        int     newFrontRightTarget;
        int     newFrontLeftTarget;
        int     newBackRightTarget;
        int     newBackLeftTarget;

        int     moveCounts;
        double  max;
        double  error;
        double  steer;

        double  frontRightSpeed;
        double  frontLeftSpeed;
        double  backRightSpeed;
        double  backLeftSpeed;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            moveCounts = (int)(distance * COUNTS_PER_INCH);
            newFrontRightTarget = right_front.getCurrentPosition() + moveCounts;
            newFrontLeftTarget = left_front.getCurrentPosition() + moveCounts;
            newBackRightTarget = right_back.getCurrentPosition() + moveCounts;
            newBackLeftTarget = left_back.getCurrentPosition() + moveCounts;

            // Set Target and Turn On RUN_TO_POSITION
            right_front.setTargetPosition(newFrontRightTarget);
            left_front.setTargetPosition(newFrontLeftTarget);
            right_back.setTargetPosition(newBackRightTarget);
            left_back.setTargetPosition(newBackLeftTarget);

            right_front.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_front.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_back.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_back.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion.
            speed = Range.clip(Math.abs(speed), 0.0, 1.0);
            right_front.setPower(speed);
            left_front.setPower(speed);
            right_back.setPower(speed);
            left_back.setPower(speed);

            // keep looping while we are still active, and ALL FOUR motors are running.
            while (opModeIsActive() &&
                    (right_front.isBusy() &&
                    left_front.isBusy() &&
                    right_back.isBusy() &&
                    left_back.isBusy())
            ) {

                // adjust relative speed based on heading error.
                error = getError(angle);
                steer = getSteer(error, P_DRIVE_COEFF);

                // if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0){steer *= -1.0;}

                frontRightSpeed = speed + steer;
                frontLeftSpeed = speed - steer;
                backRightSpeed = speed + steer;
                backLeftSpeed = speed - steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                max = Math.max(Math.abs(frontRightSpeed), Math.abs(frontLeftSpeed));
                if (max > 1.0)
                {
                    frontRightSpeed /= max;
                    frontLeftSpeed /= max;
                }
                max = Math.max(Math.abs(backRightSpeed), Math.abs(backLeftSpeed));
                if (max > 1.0)
                {
                    backRightSpeed /= max;
                    backLeftSpeed /= max;
                }

                right_front.setPower(frontRightSpeed);
                left_front.setPower(frontLeftSpeed);
                right_back.setPower(backRightSpeed);
                left_back.setPower(backLeftSpeed);

                // Display drive status for the driver.
                telemetry.addData("1. Err/St",  "%5.1f/%5.1f",   error, steer);
                telemetry.addData("2. Front Target",  "%7d:%7d", newFrontLeftTarget,  newFrontRightTarget);
                telemetry.addData("3. Back Target", "%7d:%7d",   newBackLeftTarget, newBackRightTarget);
                telemetry.addData("4. Front Actual",  "%7d:%7d", left_front.getCurrentPosition(),
                                                                 right_front.getCurrentPosition());
                telemetry.addData("5. Back Actual",  "%7d:%7d",  left_back.getCurrentPosition(),
                                                                 right_back.getCurrentPosition());
                telemetry.addData("6. Front Speed","%5.2f:%5.2f",frontLeftSpeed,
                                                                 frontRightSpeed);
                telemetry.addData("7. Back Speed","%5.2f:5.2f",  backLeftSpeed,
                                                                 backRightSpeed);
                telemetry.update();
            }

            // Stop all motion;
            left_front.setPower(0);
            right_front.setPower(0);
            left_back.setPower(0);
            right_back.setPower(0);

            // Turn off RUN_TO_POSITION
            left_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /**
     *  Method to spin on central axis to point in a new direction.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the heading (angle)
     *  2) Driver stops the opmode running.
     *
     * @param speed Desired speed of turn.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */
    public void gyroTurn (  double speed, double angle) {

        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }

    /**
     *  Method to obtain & hold a heading for a finite amount of time
     *  Move will stop once the requested time has elapsed
     *
     * @param speed      Desired speed of turn.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     * @param holdTime   Length of time (in seconds) to hold the specified heading.
     */
    public void gyroHold(   double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
            onHeading(speed, angle, P_TURN_COEFF);
            telemetry.update();
        }

        // Stop all motion;
        left_front.setPower(0);
        right_front.setPower(0);
        left_back.setPower(0);
        right_front.setPower(0);
    }

    /**
     * Perform one cycle of closed loop heading control.
     *
     * @param speed     Desired speed of turn.
     * @param angle     Absolute Angle (in Degrees) relative to last gyro reset.
     *                  0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                  If a relative angle is required, add/subtract from current heading.
     * @param PCoeff    Proportional Gain coefficient
     * @return
     */
    boolean onHeading(      double speed, double angle, double PCoeff) {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double frontLeftSpeed;
        double frontRightSpeed;
        double backLeftSeed;
        double backRightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            frontLeftSpeed  = 0.0;
            frontRightSpeed = 0.0;
            backLeftSeed  = 0.0;
            backRightSpeed = 0.0;
            onTarget = true;
        }
        else {
            steer = getSteer(error, PCoeff);
            frontRightSpeed = speed * steer;
            frontLeftSpeed = -frontRightSpeed;
            backRightSpeed = frontRightSpeed;
            backLeftSeed = frontLeftSpeed;
        }

        // Send desired speeds to motors.
        left_front.setPower(frontLeftSpeed);
        right_front.setPower(frontRightSpeed);
        left_back.setPower(backLeftSeed);
        right_back.setPower(backRightSpeed);

        // Display it for the driver.
        telemetry.addData("1. Target", "%5.2f", angle);
        telemetry.addData("2. Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("3. Front Speed", "%5.2f:%5.2f", frontLeftSpeed, frontRightSpeed);
        telemetry.addData("4. Back Speed", "%5.2f:%5.2f", backLeftSeed, backRightSpeed);

        return onTarget;
    }

    /**
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError( double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - gyro.getIntegratedZValue();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param error   Error angle in robot relative degrees
     * @param PCoeff  Proportional Gain Coefficient
     * @return
     */
    public double getSteer( double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    public VectorF navOffWall(VectorF trans, double robotAngle, VectorF offWall){
        return new VectorF(
                (float) (trans.get(0) - offWall.get(0) * Math.sin(Math.toRadians(robotAngle)) - offWall.get(2) * Math.cos(Math.toRadians(robotAngle))),
                trans.get(1),
                (float) (trans.get(2) + offWall.get(0) * Math.cos(Math.toRadians(robotAngle)) - offWall.get(2) * Math.sin(Math.toRadians(robotAngle)))
        );
    }

    public VectorF anglesFromTarget(VuforiaTrackableDefaultListener image){
        float [] data = image.getRawPose().getData();
        float [] [] rotation = {
                {data[0], data[1]},
                {data[4], data[5], data[6]},
                {data[8], data[9], data[10]}
        };
        double thetaX = Math.atan2(rotation[2][1], rotation[2][2]);
        double thetaY = Math.atan2(-rotation[2][0], Math.sqrt(rotation[2][1] * rotation[2][1] + rotation[2][2] * rotation[2][2]));
        double thetaZ = Math.atan2(rotation[1][0], rotation[0][0]);
        return new VectorF((float)thetaX, (float)thetaY, (float)thetaZ);
    }

}
