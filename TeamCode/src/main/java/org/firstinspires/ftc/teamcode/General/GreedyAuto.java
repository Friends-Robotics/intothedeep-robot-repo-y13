package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Greedy Auto", group = "Robot")
public class GreedyAuto extends LinearOpMode {

    RobotHardware robot = new RobotHardware(this);
    @Override
    public void runOpMode(){
        robot.init();
        waitForStart();

        robot.DriveByEncoderTicks(calculateForwardTicks(18), calculateStrafeTicks(0), calculateRotateTicks(0));
        robot.DriveByEncoderTicks(calculateForwardTicks(0), calculateStrafeTicks(18), calculateRotateTicks(0));
        robot.DriveByEncoderTicks(calculateForwardTicks(0), calculateStrafeTicks(0), calculateRotateTicks(9000));



    }

    public int calculateForwardTicks(double forwardsInMeters) {
        return (int) ((forwardsInMeters/ RobotHardware.CircumferenceOfWheelInMeters) * RobotHardware.WheelMotorEncoderResolution);
    }

    public int calculateStrafeTicks(double strafeInMeters) {
        return (int) ((strafeInMeters * Math.sqrt(2) / RobotHardware.CircumferenceOfWheelInMeters) * RobotHardware.WheelMotorEncoderResolution);
    }

    public int calculateRotateTicks(double degreesClockwise) {
        // Calculate the arc length the wheels need to travel
        double turningDistance = (Math.PI * RobotHardware.WheelbaseInMeters) * (degreesClockwise / 360.0);

        // Convert distance into encoder ticks
        return (int) ((turningDistance / RobotHardware.CircumferenceOfWheelInMeters)
                * RobotHardware.WheelMotorEncoderResolution * 2); // Multiply by 2 for mecanum turning
    }


}
