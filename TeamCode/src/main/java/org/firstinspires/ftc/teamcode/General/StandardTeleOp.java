package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

@TeleOp(name="StandardTeleOp", group="Robot")
public class StandardTeleOp extends LinearOpMode {

    // Create a RobotHardware object to be used to access robot hardware.
    // Prefix any hardware functions with "robot." to access this class.
    RobotHardware robot = new RobotHardware(this);
    static double slowmodeMult = 0;
    static double currentIntakePower = 0;

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

            if(gamepad1.circle){
                slowmodeMult = 0.4;
            }
            else{
                slowmodeMult = 0.8;
            }

            DriveChain();

            //PPLLLLLEEEEEEAAAAASSSSSEEEE
            if(gamepad1.dpad_right){
                //PLEASE WORK
                robot.backRight.setPower(0.6 *slowmodeMult);
                robot.frontLeft.setPower(0.6 *slowmodeMult);
            } else if (gamepad1.dpad_left) {
                //PLEASE WORK
                robot.backRight.setPower(-0.6 * slowmodeMult);
                robot.frontLeft.setPower(-0.6 * slowmodeMult);
            }

            //Arm

            if(gamepad2.right_trigger > 0.2){
                robot.intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                robot.intakeMotor.setPower(0.3);
            }
            else if(gamepad2.triangle){
                robot.intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                robot.intakeMotor.setPower(0.3);
            }
            else{
                robot.intakeMotor.setPower(0);
            }
//            //REVERSE IS SUCK
//            //FORWARD IS SPIT
//            if(gamepad2.square){
//                robot.intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//                robot.intakeMotor.setPower(0.3);
//            }
//            else if(gamepad2.cross){
//                robot.intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
//                robot.intakeMotor.setPower(0.3);
//            }
//            else{
//                robot.intakeMotor.setPower(0);
//            }
//
            if(gamepad2.right_bumper){
                robot.rightSlideMotor.setPosition(1);
                robot.leftSlideMotor.setPosition(0);
            }
            else if(gamepad2.left_bumper){
                robot.rightSlideMotor.setPosition(0);
                robot.leftSlideMotor.setPosition(1);
            }

            if(gamepad2.dpad_down){
                robot.rightFlipMotor.setPosition(0.85);
                robot.leftFlipMotor.setPosition(0.15);
            }
            else if(gamepad2.dpad_up){
                robot.rightFlipMotor.setPosition(0.14);//come back to this AP
                robot.leftFlipMotor.setPosition(0.86);
            }
            else if(gamepad2.dpad_right){
                robot.rightFlipMotor.setPosition(0.5);
                robot.leftFlipMotor.setPosition(0.5);
            }






            //!!!!!!!!!!!!robot.leftFlipMotor.close();!!!!!!!!!!!!







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
            telemetry.addData("Light Detected", ((OpticalDistanceSensor) robot.colorSensor).getLightDetected());
            telemetry.addData("COLOUR SENSOR R", robot.colorSensor.red());
            telemetry.addData("COLOUR SENSOR G", robot.colorSensor.green());
            telemetry.addData("COLOUR SENSOR B", robot.colorSensor.blue());
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
        frontRightPower *= slowmodeMult * 1.05;//TWEAK THIS VALUE!!! LESS!!!
        backRightPower *= slowmodeMult;


        robot.frontLeft.setPower(frontLeftPower);
        robot.backLeft.setPower(backLeftPower);
        robot.frontRight.setPower(frontRightPower);
        robot.backRight.setPower(backRightPower);
    }
}
