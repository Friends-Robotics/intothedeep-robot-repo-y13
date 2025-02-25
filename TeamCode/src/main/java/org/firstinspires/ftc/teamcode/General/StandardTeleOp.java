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
    static int desiredRevs;
    private static final int  topRungRevs = 6;
    private static final int middleRungRevs = 3;
    private static final int bottomRevs = 0;

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

//            robot.SetViperSlidePos(desiredRevs);
            robot.SetIntakeMotorMovement(intakeMotorMovement);
            robot.DriveChain(slowModeMultiplier, -gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            robot.SetClawPos(clawClosed);
            robot.SetFlipMotorPos(flipMotorOut);
            robot.SetDrawerSlidePos(drawerSlideOut);

            // Send telemetry messages to explain controls and show robot status
            telemetry.addLine("GAMEPAD 1")
                    .addData("\nLEFT STICK Y", -gamepad1.left_stick_y)
                    .addData("\nLEFT STICK X", gamepad1.left_stick_x)
                    .addData("\nRIGHT STICK X", gamepad1.right_stick_x);

            telemetry.addLine("GAMEPAD 2").
                    addData("\nRIGHT TRIGGER STATUS", gamepad2.right_trigger)
                    .addData("\n Left x axis", gamepad2.left_stick_x)
                    .addData("\nLEFT TRIGGER STATUS", gamepad2.left_trigger)
                    .addData("\nTRIANGLE", gamepad2.triangle)
                    .addData("\nCIRCLE", gamepad2.circle);
            telemetry.addLine("\nPOSITIONS/DIRECTIONS")
                    .addData("\nSLOWMODE MULTIPLIER", slowModeMultiplier)
                    .addData("\nCLAW CLOSED?", clawClosed)
                    .addData("\nVIPER SLIDE MOVEMENT", viperSlideMovement);
            telemetry.update();
            // Pace this loop so hands move at a reasonable speed.
            sleep(20);
        }
    }
}
