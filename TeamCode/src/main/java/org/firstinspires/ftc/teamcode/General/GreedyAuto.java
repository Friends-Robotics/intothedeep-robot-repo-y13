package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Greedy Auto", group = "Robot")
public class GreedyAuto extends LinearOpMode {

    RobotHardware robot = new RobotHardware(this);
    @Override
    public void runOpMode() {
        robot.init(true);
        waitForStart();

        robot.DriveByEncoderTicks(-563, -696, -728, -713, 0.4);
        for(int i = 0; i < 2; i++){
            Rotate180();
            HangingToBullCharge();
            BullChargeToPickup();
            PickToOutALittle();
            Rotate180();
            OutALittleToHang();
            ForwardALittle();
        }
        Ending();
    }

    void HangingToBullCharge(){
        robot.DriveByEncoderTicks(841, -842, 931, -905, 0.3);
    }

    void BullChargeToPickup(){
        robot.DriveByEncoderTicks(-605, -705, -709, -713, 0.8);
    }

    void PickToOutALittle(){
        robot.DriveByEncoderTicks(208, 280, 252, 266, 0.25);
    }

    void OutALittleToHang(){
        robot.DriveByEncoderTicks(588, -1350, 597, -1369, 0.3);
    }

    void ForwardALittle(){
        robot.DriveByEncoderTicks(-57,-71,-91, -81, 0.2);
    }

    void Rotate180(){
        robot.DriveByEncoderTicks(1328, -1385, -1442, 1403, 0.35);
    }

    void Ending(){
        robot.DriveByEncoderTicks(-1131, 1725, -1139, 1671, 0.9);
    }
}
