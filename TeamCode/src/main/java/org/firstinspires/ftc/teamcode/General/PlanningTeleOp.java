package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Planning", group = "Robot")
public class PlanningTeleOp extends StandardTeleOp {
    @Override
    public void runOpMode() {
        robot.init(true);
        waitForStart();

        while(opModeIsActive()){
            ReceiveInput();
            ApplyInput();
            SendTelemetry();

            sleep(20);
        }

    }

    @Override
    protected void ReceiveInput(){
        super.ReceiveInput();
        if(gamepad1.circle) {
            robot.SetDriveChainMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.SetDriveChainMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}

