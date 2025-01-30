package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

@TeleOp(name="StandardTeleOp", group="Robot")
public class StandardTeleOp extends LinearOpMode {

    // Create a RobotHardware object to be used to access robot hardware.
    // Prefix any hardware functions with "robot." to access this class.
    RobotHardware robot = new RobotHardware(this);
    static double slowmodeMult = 0;

    @Override
    public void runOpMode() {
        // initialize all the hardware, using the hardware class. See how clean and simple this is?
        robot.init();

        // Send telemetry message to signify robot waiting;
        // Wait for the game to start (driver presses START)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            //Drivechain

            if(gamepad1.left_trigger > 0.2){
                slowmodeMult = 0.2;
            }
            else if(gamepad1.right_trigger > 0.2) {
                slowmodeMult = 0.7;
            }
            else{
                slowmodeMult = 0.4;
            }

            if(gamepad1.cross){
                robot.viperSlideClaw.setPosition(1);
            }
            else if(gamepad1.square){
                robot.viperSlideClaw.setPosition(0.1);
            }


            DriveChain();

            //Arm

            if(gamepad1.right_bumper){
                robot.leftViperSlide.setDirection(DcMotorSimple.Direction.FORWARD);
                robot.rightViperSlide.setDirection(DcMotorSimple.Direction.REVERSE);

                robot.leftViperSlide.setPower(1);
                robot.rightViperSlide.setPower(1);
            }
            else if (gamepad1.left_bumper) {
                robot.leftViperSlide.setDirection(DcMotorSimple.Direction.REVERSE);
                robot.rightViperSlide.setDirection(DcMotorSimple.Direction.FORWARD);

                robot.leftViperSlide.setPower(1);
                robot.rightViperSlide.setPower(1);
            }
            else{
                robot.leftViperSlide.setPower(0);
                robot.rightViperSlide.setPower(0);
            }

            if(gamepad1.touchpad){
                robot.leftViperSlide.setDirection(DcMotorSimple.Direction.REVERSE);
                robot.rightViperSlide.setDirection(DcMotorSimple.Direction.FORWARD);

                robot.leftViperSlide.setPower(1);
                robot.rightViperSlide.setPower(1);
            }

            if(gamepad2.right_trigger > 0.2){
                robot.intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                robot.intakeMotor.setPower(0.3);
            }
            else if(gamepad2.cross || gamepad2.left_trigger > 0.2){
                robot.intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                robot.intakeMotor.setPower(0.4);
            }
            else{
                robot.intakeMotor.setPower(0);
            }

            if(gamepad2.right_bumper){
                robot.rightSlideMotor.setPosition(0);
                robot.leftSlideMotor.setPosition(1);
            }
            else if(gamepad2.left_bumper){
                robot.rightSlideMotor.setPosition(1);
                robot.leftSlideMotor.setPosition(0);
            }

            if(gamepad2.circle){
                robot.rightFlipMotor.setPosition(0.55);
                robot.leftFlipMotor.setPosition(0.45);
            }
            else if(gamepad2.triangle){
                robot.rightFlipMotor.setPosition(0);
                robot.leftFlipMotor.setPosition(1);
            }
            else if(gamepad2.dpad_right){
                robot.rightFlipMotor.setPosition(0.5);
                robot.leftFlipMotor.setPosition(0.5);
            }

            // Send telemetry messages to explain controls and show robot status
            telemetry.addData("GAMEPAD 1 LEFT STICK Y", -gamepad1.left_stick_y);
            telemetry.addData("GAMEPAD 1 LEFT STICK X", gamepad1.left_stick_x);
            telemetry.addData("GAMEPAD 1 RIGHT STICK X", gamepad1.right_stick_x);
            telemetry.addData("MOTOR STATUS", robot.backLeft.isBusy() && robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy());
            telemetry.addData("--------------------------------------------------", "");
            telemetry.addData("INTAKE POWER", robot.intakeMotor.getPower());
            telemetry.addData("INTAKE DIRECTION", robot.intakeMotor.getDirection());
            telemetry.addData("FLIP MOTOR POS", robot.rightFlipMotor.getDirection());
            telemetry.addData("RIGHT TRIGGER STATUS", gamepad2.right_trigger);
            telemetry.addData("LEFT TRIGGER STATUS", gamepad2.left_trigger);
            telemetry.addData("RSLIDE MOTOR STATUS", robot.rightSlideMotor.getPosition());
            telemetry.addData("LSLIDE MOTOR STATUS", robot.leftSlideMotor.getPosition());
            telemetry.addData("Gamepad2 Triangle", gamepad2.triangle);
            telemetry.addData("Gamepad2 Circle", gamepad2.circle);
//            telemetry.addData("Light Detected", ((OpticalDistanceSensor) robot.colorSensor).getLightDetected());
//            telemetry.addData("COLOUR SENSOR R", robot.colorSensor.red());
//            telemetry.addData("COLOUR SENSOR G", robot.colorSensor.green());
//            telemetry.addData("COLOUR SENSOR B", robot.colorSensor.blue());
            telemetry.update();

            // Pace this loop so hands move at a reasonable speed.
            sleep(50);
        }
    }

    void DriveChain(){
        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x; //Remember, Aaron is stupid
        double rx = gamepad1.right_stick_x;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]

        //AP: Don't even ask me how this works, I'm not a vectors wizard.... gm0.com
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;


        frontLeftPower *= slowmodeMult;
        backLeftPower *= slowmodeMult;
        frontRightPower *= slowmodeMult; //bad
        backRightPower *= slowmodeMult;


        robot.frontLeft.setPower(frontLeftPower);
        robot.backLeft.setPower(backLeftPower);
        robot.frontRight.setPower(frontRightPower);
        robot.backRight.setPower(backRightPower);
    }
}
