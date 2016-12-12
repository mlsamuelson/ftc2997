package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.vuforia.HINT;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name = "Test: Vuforia", group = "finished")

public class Vuforia extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AT99xn7/////AAAAGba3Op9lP005p/1muRdA/Lpgd4QiKqy2xzEQL8ZIPxNHzLiWcz1AEATHuJZxfH2diokQKqBuPLkgEVi7HvDJoX9szJym+MhmXjakDmxEODsoeS3V5V7d2DBN1aC1+ekS1C31/QopnqiSKgt8XB0voGrLQ+i9D+6ZVkfhZ2b5Jc6JC7U3r7d2PuVtoWRjv4tDFdQa3fjgdZnTthcOv16LOfoOBrTY3KhMczqewqfPCs+fqGxYU8hdkgNOtIreRyMW2WZH6ZZovg62bVBM2yHRuaalfzyYdRDs1FkExR8V5QSD4dDHPwv5ITnWTPIgrwtEVjEk1+kpBHXJpuTm0vlIzO7h1Y7bU4wLQRnGLH8u/wTv";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.TEAPOT;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        //Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");
        
        waitForStart();
        
        beacons.activate();

        while (opModeIsActive()) {
            for (VuforiaTrackable beac : beacons) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();

                if (pose != null) {
                    VectorF translation = pose.getTranslation();

                    telemetry.addData(beac.getName() + "-Translation", translation);

                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));   // the two translations are for vertical phone (y and x). 0 and 2 for horizontal

                    telemetry.addData(beac.getName() + "-Degrees", degreesToTurn);
                }
            }

            telemetry.update();
        }
    }
}