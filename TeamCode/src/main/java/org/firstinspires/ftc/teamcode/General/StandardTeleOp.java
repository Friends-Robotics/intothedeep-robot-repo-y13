package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name="StandardTeleOp", group="Robot")
public class StandardTeleOp extends LinearOpMode {

    // Create a RobotHardware object to be used to access robot hardware.
    // Prefix any hardware functions with "robot." to access this class.
    RobotHardware robot = new RobotHardware(this);
    static double slowModeMultiplier = 0;
    static boolean clawClosed = true;
    static ViperSlideDirections viperSlideMovement = ViperSlideDirections.NONE;
    static IntakeMotorStates intakeMotorMovement = IntakeMotorStates.NONE;
    static boolean drawerSlideOut = false;
    static boolean flipMotorOut = false;

    @Override
    public void runOpMode()
    {
        robot.init(true);
        waitForStart();

        while (opModeIsActive()) {
            ReceiveInput();
            ApplyInput();
            SendTelemetry();

            sleep(20);
        }
    }

    protected void ReceiveInput(){
        //Getting desired slow mode:
        if(gamepad1.left_trigger > 0.2){
            slowModeMultiplier = 0.7;
        }
        else if(gamepad1.right_trigger > 0.2) {
            slowModeMultiplier = 0.2;
        }
        else{
            slowModeMultiplier = 0.4;
        }

        //Getting desired claw position
        if(gamepad1.cross){
            clawClosed = false;
        }
        else if(gamepad1.square){
            clawClosed = true;
        }

        //Getting viper slide directions
        if(gamepad1.right_bumper){
            viperSlideMovement = ViperSlideDirections.UPWARDS;
        }
        else if (gamepad1.left_bumper) {
            viperSlideMovement = ViperSlideDirections.DOWNWARDS;
        }
        else{
            viperSlideMovement = ViperSlideDirections.NONE;
        }

        if(gamepad2.dpad_up){
            drawerSlideOut = true;
            flipMotorOut = true;
        }
        else if(gamepad2.dpad_down){
            drawerSlideOut = false;
            flipMotorOut = false;
        }

        if(gamepad2.square){
            drawerSlideOut = true;
        }
        else if(gamepad2.cross){
            drawerSlideOut = false;
        }

        if(gamepad2.touchpad){
            try {
                robot.FinalFold();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(gamepad2.triangle){
            flipMotorOut = true;
        }
        else if(gamepad2.circle){
            flipMotorOut = false;
        }

//            if(gamepad1.dpad_up){
//                desiredRevs = topRungRevs;
//            }
//            else if(gamepad1.dpad_right){
//                desiredRevs = middleRungRevs;
//            }
//            else if(gamepad1.dpad_down) {
//                desiredRevs = bottomRevs;
//            }

        if(gamepad2.right_bumper){
            intakeMotorMovement = IntakeMotorStates.IN;
        }
        else if(gamepad2.left_bumper){
            intakeMotorMovement = IntakeMotorStates.OUT;
        }
        else{
            intakeMotorMovement = IntakeMotorStates.NONE;
        }
    }

    protected void ApplyInput(){
        robot.SetViperSlideMovement(slowModeMultiplier, viperSlideMovement);
        robot.SetIntakeMotorMovement(intakeMotorMovement);
        robot.DriveChain(slowModeMultiplier, -gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        robot.SetClawPos(clawClosed);
        robot.IntakeSystem(drawerSlideOut, flipMotorOut);
    }

    protected void SendTelemetry(){
        telemetry.addLine("GAMEPAD 1")
                .addData("\nLEFT STICK Y", -gamepad1.left_stick_y)
                .addData("\nLEFT STICK X", gamepad1.left_stick_x)
                .addData("\nRIGHT STICK X", gamepad1.right_stick_x);

        telemetry.addLine("GAMEPAD 2").
                addData("\nRIGHT TRIGGER STATUS", gamepad2.right_trigger)
                .addData("\n LEFT STICK X", gamepad2.left_stick_x)
                .addData("\nLEFT TRIGGER STATUS", gamepad2.left_trigger);

        telemetry.addLine("\nPOSITIONS/DIRECTIONS")
                .addData("\nSLOWMODE MULTIPLIER", slowModeMultiplier)
                .addData("\nCLAW CLOSED?", clawClosed)
                .addData("\nVIPER SLIDE MOVEMENT", viperSlideMovement)
                .addData("\nINTAKE MOTOR MOVEMENT", intakeMotorMovement);
        telemetry.update();
    }
}
