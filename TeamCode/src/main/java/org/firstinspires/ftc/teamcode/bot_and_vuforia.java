package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
 * Created by Robotics on 12/7/2016.
 */

@Autonomous(name="Test: vuforia1", group="Tests")

public class bot_and_vuforia extends OpMode {
    // Declare variables
    DcMotor left_front;
    DcMotor right_front;
    DcMotor left_back;
    DcMotor right_back;

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
        for (VuforiaTrackable beac : beacons) {
            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();
            if (pose != null) {
                VectorF translation = pose.getTranslation();
                telemetry.addData(beac.getName() + "-Translation", translation);
                double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));   // the two translations are for vertical phone (y and x). 0 and 2 for horizontal
                telemetry.addData(beac.getName() + "-Degrees", degreesToTurn);
            }
        }
    }
}
