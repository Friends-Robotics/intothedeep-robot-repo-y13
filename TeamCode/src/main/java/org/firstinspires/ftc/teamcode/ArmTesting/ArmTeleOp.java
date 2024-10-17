package org.firstinspires.ftc.teamcode.ArmTesting;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="ArmTeleOp", group="Robot")
public class ArmTeleOp extends LinearOpMode {

    // Create a RobotHardware object to be used to access robot hardware.
    // Prefix any hardware functions with "robot." to access this class.
    ArmRobotHardware robot = new ArmRobotHardware(this);

    @Override
    public void runOpMode() {
        // initialize all the hardware, using the hardware class. See how clean and simple this is?
        robot.init();

        // Send telemetry message to signify robot waiting;
        // Wait for the game to start (driver presses START)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //Arm

            if(gamepad2.square){
                robot.intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                robot.intakeMotor.setPower(0.6);
            }
            else if(gamepad2.cross){
                robot.intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                robot.intakeMotor.setPower(0.6);
            }
            else{
                robot.intakeMotor.setPower(0);
            }

            // Send telemetry messages to explain controls and show robot status
            telemetry.addData("INTAKE POWER", robot.intakeMotor.getPower());
            telemetry.addData("INTAKE DIRECTION", robot.intakeMotor.getDirection());
            telemetry.update();

            // Pace this loop so hands move at a reasonable speed.
            sleep(50);
        }
    }
}