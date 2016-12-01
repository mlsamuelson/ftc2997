package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.vuforia.HINT;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Steven on 11/30/2016.
 * Last edited on 11/30/2016
 */

@TeleOp(name="Test: auto1", group="Tests")

public class auto_test_1 extends OpMode {
    // Declare variables
    DcMotor left_front;
    DcMotor right_front;
    DcMotor left_back;
    DcMotor right_back;
    //DcMotor intake;
    //DcMotor launcher;

    double intake_pow;
    double launcher_pow;

    VuforiaTrackables beacons;

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

        // Init Vuforia
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AT99xn7/////AAAAGba3Op9lP005p/1muRdA/Lpgd4QiKqy2xzEQL8ZIPxNHzLiWcz1AEATHuJZxfH2diokQKqBuPLkgEVi7HvDJoX9szJym+MhmXjakDmxEODsoeS3V5V7d2DBN1aC1+ekS1C31/QopnqiSKgt8XB0voGrLQ+i9D+6ZVkfhZ2b5Jc6JC7U3r7d2PuVtoWRjv4tDFdQa3fjgdZnTthcOv16LOfoOBrTY3KhMczqewqfPCs+fqGxYU8hdkgNOtIreRyMW2WZH6ZZovg62bVBM2yHRuaalfzyYdRDs1FkExR8V5QSD4dDHPwv5ITnWTPIgrwtEVjEk1+kpBHXJpuTm0vlIzO7h1Y7bU4wLQRnGLH8u/wTv";

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        //Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");
    }

    @Override
    public void start() {
        beacons.activate();
    }

    @Override
    public void loop() {
        // set power variables
        float fl_pow = 0;
        float bl_pow = 0;
        float fr_pow = 0;
        float br_pow = 0;

        int step = 1;
        int switch_case = 1;
        /***
         * 1: Beacon(s)
         * 2: Knock off ball
         * 3: Shoot
         * 4: Park
         ***/

        switch (switch_case){
            case 1:{
                // Get vuforia information
                // This should cause the bot to move towards the closest beacon it sees
                for (VuforiaTrackable beac : beacons) {
                    OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();
                    if (pose != null) {
                        VectorF translation = pose.getTranslation();
                        telemetry.addData(beac.getName() + "-Translation", translation);
                        double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));   // the two translations are for vertical phone (y and x). 0 and 2 for horizontal
                        telemetry.addData(beac.getName() + "-Degrees", degreesToTurn);
                    }
                }
                switch (step){
                    case 1: // FIND BEACON
                        break;
                    case 2: // TURN TOWARDS BEACON
                        break;
                    case 3: // GO TO BEACON
                        break;
                    case 4: // PRESS BEACON
                }
            }break;
            case 2:{

            }
        }
        // Using the information on the PDF, match the motors to fit with the sticks
        /*
        float fl_pow = drive - strafe + rotate;
        float bl_pow = drive + strafe + rotate;
        float fr_pow = drive + strafe - rotate;
        float br_pow = drive - strafe - rotate;
        */

        intake_pow = (float) scaleInput(intake_pow);
        launcher_pow = (float) scaleInput(launcher_pow);

        // Assign values
        left_front.setPower(fl_pow);
        left_back.setPower(bl_pow);
        right_front.setPower(fr_pow);
        right_back.setPower(br_pow);

        //intake.setPower(intake_pow);
        //launcher.setPower(launcher_pow);
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
