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

        //Hanging specimen at the start
        robot.SetViperSlidePos(RobotHardware.TopRungRevs);
        robot.DriveByEncoderTicks(CalculateForwardTicks(0.6), 0, 0, 0.6);
        robot.SetViperSlidePos(RobotHardware.TopRungRevs - 0.5);
        robot.SetClawPos(false);

        FromHangingToObservationZonePickup();
        FromObservationZonePickupToHang();
        FromHangingToObservationZonePickup();
        FromHangingToObservationZonePickup();

        robot.DriveByEncoderTicks(0,CalculateStrafeTicks(0.9144), 0,0.6);
        robot.DriveByEncoderTicks(CalculateForwardTicks(-0.6604), 0, 0, 0.6);
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
        robot.DriveByEncoderTicks(0,0,1400,0.6);
        robot.DriveByEncoderTicks(0,CalculateStrafeTicks(-0.9144), 0,0.6);
        robot.DriveByEncoderTicks(CalculateForwardTicks(0.6), 0, 0, 0.6);
        robot.SetClawPos(true);
        robot.SetViperSlidePos(RobotHardware.PickUpFromWallRevs + 0.25);
    }

    private void FromObservationZonePickupToHang(){
        new Thread(() -> {
            robot.SetViperSlidePos(RobotHardware.BottomRevs);
        }).start();
        robot.DriveByEncoderTicks(0,0,1400, 0.6);
        robot.DriveByEncoderTicks(CalculateForwardTicks((2 * 0.6096) - 0.10),CalculateStrafeTicks(-0.9144), 0, 0.6);
        robot.SetViperSlidePos(RobotHardware.TopRungRevs);
        robot.DriveByEncoderTicks(CalculateForwardTicks(0.1), 0,0,0.2);
        robot.SetViperSlidePos(RobotHardware.TopRungRevs - 0.5);
        robot.SetClawPos(false);
    }


}
