/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.General.RobotHardware;

/*
 * This OpMode illustrates the concept of driving a path based on time.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backward for 1 Second
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@Autonomous(name="Robot: Auto Drive By Time", group="Robot")

public class RobotAutoDriveByTime extends LinearOpMode {

    /* Declare OpMode members. */


    private ElapsedTime     runtime = new ElapsedTime();

    RobotHardware robot = new RobotHardware(this);

    static final double     FORWARD_SPEED = 0.6;//DEFO A PLACEHOLDER
    static final long     SLEEPTIME    = 1000;//DEFO A PLACEHOLDER

    @Override
    public void runOpMode() {
        robot.init();

        // Initialize the drive system variables.


        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
//        // Step 1:  Drive forward for 3 seconds
//        //Peter:Should Move It Forward
        //AP: Hopefully after I messed with it as well
            DriveForward();
//
//        // Step 2:   Stop for a second

//
//        // Step 3:  Drive Backward for 1 Second
            DriveBackward();
    }

    public void AutonomousFunctions(double X, double Y, double RX)
    {
        double slowmodeMult;


        //Drive-chain

        slowmodeMult = 0.4;


        double y = -Y; // Remember, Y stick value is reversed
        double x = X;
        double rx = RX;

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

        // Send telemetry messages to explain controls and show robot status
        //telemetry.addData("GAMEPAD 1 LEFT STICK Y", -gamepad1.left_stick_y);
        // telemetry.addData("GAMEPAD 1 LEFT STICK X", gamepad1.left_stick_x);
        // telemetry.addData("GAMEPAD 1 RIGHT STICK X", gamepad1.right_stick_x);
        telemetry.addData("MOTOR STATUS", robot.backLeft.isBusy() && robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy());
        telemetry.addData("--------------------------------------------------", "");
        //telemetry.addData("INTAKE POWER", robot.intakeMotor.getPower());
        //telemetry.addData("INTAKE DIRECTION", robot.intakeMotor.getDirection());
        telemetry.update();

        // Pace this loop so hands move at a reasonable speed.
        //sleep(50);
    }

    private void DriveForward(){
        AutonomousFunctions(0,-FORWARD_SPEED,0);
        sleep(SLEEPTIME);
    }
    private void DriveBackward(){
        AutonomousFunctions(0,FORWARD_SPEED,0);
        sleep(SLEEPTIME);
    }

    private void Rotate90CW(){
        AutonomousFunctions(0,0,FORWARD_SPEED);
        sleep(SLEEPTIME);
    }

    private void Rotate90ACW(){
        AutonomousFunctions(0,0, -FORWARD_SPEED);
        sleep(SLEEPTIME);
    }

}


