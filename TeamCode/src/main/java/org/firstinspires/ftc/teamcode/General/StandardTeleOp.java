package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="StandardTeleOp", group="Robot")
public class StandardTeleOp extends LinearOpMode {

    // Create a RobotHardware object to be used to access robot hardware.
    // Prefix any hardware functions with "robot." to access this class.
    RobotHardware robot = new RobotHardware(this);
    static double slowmodeMult = 0;
    static boolean clawClosed = true;
    static ViperSlideDirections viperSlideMovement = ViperSlideDirections.NONE;
    static IntakeMotorStates intakeMotorMovement = IntakeMotorStates.NONE;
    static boolean drawerSlideOut = false;
    static boolean flipMotorOut = false;

    @Override
    public void runOpMode() {
        // initialize all the hardware, using the hardware class. See how clean and simple this is?
        robot.init();

        // Send telemetry message to signify robot waiting;
        // Wait for the game to start (driver presses START)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //Gamepad 1:

            //Getting desired slow mode:
            if(gamepad1.left_trigger > 0.2){
                slowmodeMult = 0.7;
            }
            else if(gamepad1.right_trigger > 0.2) {
                slowmodeMult = 0.2;
            }
            else{
                slowmodeMult = 0.4;
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

            robot.DriveChain(slowmodeMult, -gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            robot.SetClawPos(clawClosed);
            robot.SetViperSlideMovement(slowmodeMult, viperSlideMovement);

            if(gamepad2.right_trigger > 0.2){
                intakeMotorMovement = IntakeMotorStates.IN;
            }
            else if(gamepad2.cross || gamepad2.left_trigger > 0.2){
                intakeMotorMovement = IntakeMotorStates.OUT;
            }
            else{
                intakeMotorMovement = IntakeMotorStates.NONE;
            }

            if(gamepad2.right_bumper){
                drawerSlideOut = true;
            }
            else if(gamepad2.left_bumper){
                drawerSlideOut = false;
            }

            if(gamepad2.circle){
                flipMotorOut = false;
            }
            else if(gamepad2.triangle){
                flipMotorOut = true;
            }

            robot.SetIntakeMotorMovement(intakeMotorMovement);
            robot.SetDrawerSlidePos(drawerSlideOut);
            robot.SetFlipMotorPos(flipMotorOut);

            // Send telemetry messages to explain controls and show robot status
            telemetry.addLine("GAMEPAD 1")
                    .addData("\nLEFT STICK Y", -gamepad1.left_stick_y)
                    .addData("\nLEFT STICK X", gamepad1.left_stick_x)
                    .addData("\nRIGHT STICK X", gamepad1.right_stick_x);

            telemetry.addLine("GAMEPAD 2").
                    addData("\nRIGHT TRIGGER STATUS", gamepad2.right_trigger)
                    .addData("\nLEFT TRIGGER STATUS", gamepad2.left_trigger)
                    .addData("\nTRIANGLE", gamepad2.triangle)
                    .addData("\nCIRCLE", gamepad2.circle);
            telemetry.addLine("\nPOSITIONS/DIRECTIONS")
                    .addData("\nSLOWMODE MULTIPLIER", slowmodeMult)
                    .addData("\nCLAW CLOSED?", clawClosed)
                    .addData("\nVIPER SLIDE MOVEMENT", viperSlideMovement);
            telemetry.update();
            // Pace this loop so hands move at a reasonable speed.
            sleep(50);
        }
    }
}
