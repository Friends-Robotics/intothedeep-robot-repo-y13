package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Greedy Auto", group = "Robot")
public class GreedyAuto extends LinearOpMode {

    RobotHardware robot = new RobotHardware(this);
    @Override
    public void runOpMode(){
        robot.init(true);
        waitForStart();

        robot.DriveByEncoderTicks(-563, -696, -728, -713);
    }

    private int CalculateForwardTicks(double forwardsInMeters) {
        return (int) ((-forwardsInMeters/ RobotHardware.CircumferenceOfWheelInMeters) * RobotHardware.WheelMotorEncoderResolution);
    }

    private int CalculateStrafeTicks(double strafeInMeters) {
        return (int) ((-strafeInMeters * Math.sqrt(2) / RobotHardware.CircumferenceOfWheelInMeters) * RobotHardware.WheelMotorEncoderResolution);
    }

    private int CalculateRotateTicks(double degreesClockwise) {
        // Calculate the arc length the wheels need to travel
        double turningDistance = (Math.PI * RobotHardware.WheelbaseInMeters) * (degreesClockwise / 360.0);

        // Convert distance into encoder ticks
        return (int) ((turningDistance / RobotHardware.CircumferenceOfWheelInMeters)
                * RobotHardware.WheelMotorEncoderResolution * 2); // Multiply by 2 for mecanum turning
    }

    private void FromHangingToObservationZonePickup(){
    }

    private void FromObservationZonePickupToHang(){
    }


}
