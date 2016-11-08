package com.qualcomm.ftcrobotcontroller.opmodes;

/*
https://www.youtube.com/watch?v=2z-o9Ts8XoE

*/

public class VuforiaOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters parama = new VuforiaLocalizer.Parameters(R.id.cameraMoniterViewId);
        params.cameraDirection = VuforiaLocalizer.cameraDirection.BACK;
        params.vuforiaLicenseKey = "AT99xn7/////AAAAGba3Op9lP005p/1muRdA/Lpgd4QiKqy2xzEQL8ZIPxNHzLiWcz1AEATHuJZxfH2diokQKqBuPLkgEVi7HvDJoX9szJym+MhmXjakDmxEODsoeS3V5V7d2DBN1aC1+ekS1C31/QopnqiSKgt8XB0voGrLQ+i9D+6ZVkfhZ2b5Jc6JC7U3r7d2PuVtoWRjv4tDFdQa3fjgdZnTthcOv16LOfoOBrTY3KhMczqewqfPCs+fqGxYU8hdkgNOtIreRyMW2WZH6ZZovg62bVBM2yHRuaalfzyYdRDs1FkExR8V5QSD4dDHPwv5ITnWTPIgrwtEVjEk1+kpBHXJpuTm0vlIzO7h1Y7bU4wLQRnGLH8u/wTv";
        params.cameraMoniterFeedback = VuforiaLocalizer.Parameters.cameraMoniterFeedback.TEAPOT;
        
        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setHint(HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);
        
        VuforiaTrackables beacons = Vuforia.loadTreackablesFromAsset("FTC_2016-16");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");
        
        waitForStart();
        
        beacons.activate();
        
        while (opmodeIsActive()) {
            for (VuforiaTrackable beac : beacons){
                
            }
        }
        
    }
}