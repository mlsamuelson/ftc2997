package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.hardware.ColorSensor;






public class BBotsBlueAutonomous extends LinearOpMode {
    DcMotor lMotor;
    DcMotor rMotor;
    DcMotor lMotor2;
    DcMotor rMotor2;
    DcMotor Sweeper;
    Servo   ClimberDump;
    //ColorSensor sensorRGBL;
    //ColorSensor sensorRGBR;

    @Override
    public void runOpMode() throws InterruptedException {
        lMotor = hardwareMap.dcMotor.get("LeftA");
        rMotor = hardwareMap.dcMotor.get("RightA");
        lMotor2 = hardwareMap.dcMotor.get("LeftB");
        rMotor2 = hardwareMap.dcMotor.get("RightB");
        Sweeper = hardwareMap.dcMotor.get("Sweeper");
        ClimberDump = hardwareMap.servo.get("ClimberDump");
        // Reverse the LEFT motors because the robot is running backwards
        rMotor.setDirection(DcMotor.Direction.REVERSE);
        rMotor2.setDirection(DcMotor.Direction.REVERSE);

        //sensorRGBL = hardwareMap.colorSensor.get("MRColor_left");
        //sensorRGBR = hardwareMap.colorSensor.get("MRColor_right");

        //sensorRGBR.setI2cAddress(0x70);
        //sensorRGBR.enableLed(false);
        //sensorRGBL.enableLed(false);

        final double ClimberDumpUp = 0.1;

        waitForStart();
       //sensorRGBR.enableLed(true);
        //sensorRGBL.enableLed(true);
            Sweeper.setPower(1);
            lMotor.setPower(-1.0); // -0.9); Commented out now that the drivetrain motor gearing matches.
            lMotor2.setPower(-1.0); // -0.9);
            rMotor.setPower(-1.0);
            rMotor2.setPower(-1.0);

            sleep(4600);

            lMotor.setPower(0.9);
            lMotor2.setPower(0.9);
            rMotor.setPower(1.0);
            rMotor2.setPower(1.0);

            sleep(400);

            lMotor.setPower(0.5);
            lMotor2.setPower(0.5);
            rMotor.setPower(-0.5);
            rMotor2.setPower(-0.5);
            sleep(1350);

            lMotor.setPower(-1.0);
            lMotor2.setPower(-1.0);
            rMotor.setPower(-1.0);
            rMotor2.setPower(-1.0);
            sleep(1250);


        ClimberDump.setPosition(ClimberDumpUp);


        lMotor.setPower(0);
            lMotor2.setPower(0);
            rMotor.setPower(0);
            rMotor2.setPower(0);
    }
}