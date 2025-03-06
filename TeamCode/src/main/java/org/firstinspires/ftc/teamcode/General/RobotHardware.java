package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

enum ViperSlideDirections {
    UPWARDS,
    DOWNWARDS,
    NONE
}

enum FlipMotorPos{
    IN,
    OUT,
    HANG
}

enum IntakeMotorStates{
    IN,
    OUT,
    NONE
}
public class RobotHardware {
    private final LinearOpMode myOpMode;   // gain access to methods in the calling OpMode.
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private DcMotor intakeMotor = null;
    private Servo rightSlideMotor = null;
    private Servo leftSlideMotor = null;
    private Servo rightFlipMotor = null;
    private Servo leftFlipMotor = null;
    private DcMotor leftViperSlide = null;
    private DcMotor rightViperSlide = null;
    private Servo viperSlideClaw = null;
    public static final int ViperSlideMotorEncoderResolution = 752;
    public static final double CircumferenceOfWheelInMeters = 0.2356;
    public static final double WheelMotorEncoderResolution = 336;
    public static final double WheelbaseInMeters = 0.388;
    public static final int TopRungRevs = 6;
    public static final int PickUpFromWallRevs = 3;
    public static final int BottomRevs = 0;

    // Define a constructor that allows the OpMode to pass a reference to itself.
    public RobotHardware(LinearOpMode opmode)
    {
        myOpMode = opmode;
    }

    /**
     * Initialize all the robot's hardware.
     * This method must be called ONCE when the OpMode is initialized.
     * <p>
     * All of the hardware devices are accessed via the hardware map, and initialized.
     */
    public void init(boolean resetEncoders) {
        // Define and Initialize Motors
        frontLeft  = myOpMode.hardwareMap.get(DcMotor.class, "front_left");
        frontRight = myOpMode.hardwareMap.get(DcMotor.class, "front_right");
        backLeft = myOpMode.hardwareMap.get(DcMotor.class, "back_left");
        backRight = myOpMode.hardwareMap.get(DcMotor.class, "back_right");
        intakeMotor = myOpMode.hardwareMap.get(DcMotor.class, "intake");
        rightSlideMotor = myOpMode.hardwareMap.get(Servo.class, "rSlide");
        leftSlideMotor = myOpMode.hardwareMap.get(Servo.class, "lSlide");
        rightFlipMotor = myOpMode.hardwareMap.get(Servo.class, "rFlip");
        leftFlipMotor = myOpMode.hardwareMap.get(Servo.class, "lFlip");
        leftViperSlide = myOpMode.hardwareMap.get(DcMotor.class, "lvs");
        rightViperSlide = myOpMode.hardwareMap.get(DcMotor.class, "rvs");
        viperSlideClaw = myOpMode.hardwareMap.get(Servo.class, "vsclaw");

        rightSlideMotor.scaleRange(0.5, 0.75);
        leftSlideMotor.scaleRange(0.25, 0.5);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        if (resetEncoders) {
            SetDriveChainMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            SetDriveChainMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else{
            SetDriveChainMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftViperSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightViperSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftViperSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightViperSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        SetClawPos(true);
        IntakeSystem(false, FlipMotorPos.IN);

        myOpMode.telemetry.addData(">", "Hardware Initialized");
    }

    public void SetDriveChainMotorMode(DcMotor.RunMode runMode){
        frontLeft.setMode(runMode);
        backLeft.setMode(runMode);
        frontRight.setMode(runMode);
        backRight.setMode(runMode);
    }

    public void DriveChain(double slowModeMult, double y, double x, double rx){
        //AP: Don't even ask me how this works, I'm not a vectors wizard.... gm0.com
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;


        frontLeftPower *= slowModeMult;
        backLeftPower *= slowModeMult;
        frontRightPower *= slowModeMult;
        backRightPower *= slowModeMult;


        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);

        if(myOpMode.getClass() == PlanningTeleOp.class) {
            myOpMode.telemetry.addLine("Current motor positions in ticks")
                    .addData("Front Left: ", frontLeft.getCurrentPosition())
                    .addData("Front Right: ", frontRight.getCurrentPosition())
                    .addData("Back Right: ", backRight.getCurrentPosition())
                    .addData("Back Left: ", backLeft.getCurrentPosition());
        }
        myOpMode.telemetry.update();
    }
    public void DriveByEncoderTicks(int flTicks, int frTicks, int brTicks, int blTicks) {
        SetDriveChainMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set target positions
        frontLeft.setTargetPosition(flTicks);
        frontRight.setTargetPosition(frTicks);
        backRight.setTargetPosition(brTicks);
        backLeft.setTargetPosition(blTicks);

        // Set mode to RUN_TO_POSITION
        SetDriveChainMotorMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power (ensure all wheels move at the same rate)
        frontLeft.setPower(0.2);
        backLeft.setPower(0.2);
        frontRight.setPower(0.2);
        backRight.setPower(0.2);

        // Wait until all motors reach their target
        while (myOpMode.opModeIsActive() &&
                (frontLeft.isBusy() || backLeft.isBusy() || frontRight.isBusy() || backRight.isBusy())) {
            myOpMode.telemetry.addData("FL Target", frontLeft.getTargetPosition());
            myOpMode.telemetry.addData("BL Target", backLeft.getTargetPosition());
            myOpMode.telemetry.addData("FR Target", frontRight.getTargetPosition());
            myOpMode.telemetry.addData("BR Target", backRight.getTargetPosition());
            myOpMode.telemetry.update();
        }

        // Stop all motors
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
    public void SetClawPos(boolean clawClosed){
        if(clawClosed){
            viperSlideClaw.setPosition(0.5);
        }
        else{
            viperSlideClaw.setPosition(0.25);
        }
    }
    public void SetViperSlideMovement(double slowModeMult, ViperSlideDirections viperSlideMovement){
        switch(viperSlideMovement){
            case UPWARDS:
                leftViperSlide.setDirection(DcMotorSimple.Direction.FORWARD);
                rightViperSlide.setDirection(DcMotorSimple.Direction.REVERSE);

                leftViperSlide.setPower(1);
                rightViperSlide.setPower(1);
                break;
            case DOWNWARDS:
                leftViperSlide.setDirection(DcMotorSimple.Direction.REVERSE);
                rightViperSlide.setDirection(DcMotorSimple.Direction.FORWARD);

                leftViperSlide.setPower(1);
                rightViperSlide.setPower(1);
                break;
            case NONE:
                leftViperSlide.setDirection(DcMotorSimple.Direction.FORWARD);
                rightViperSlide.setDirection(DcMotorSimple.Direction.REVERSE);
                leftViperSlide.setPower(0.08);
                rightViperSlide.setPower(0.08);
                break;
        }
    }
    public void SetIntakeMotorMovement(IntakeMotorStates intakeMotorMovement){
        switch(intakeMotorMovement){
            case IN:
                intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                intakeMotor.setPower(0.4);
                break;
            case OUT:
                intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                intakeMotor.setPower(0.3);
                break;
            case NONE:
                intakeMotor.setPower(0);
                break;
        }
    }
    public void SetDrawerSlidePos(boolean slideOut){ //RIGHT SLIDE MAXXED -> MAX EXTENSION
        if(slideOut){
            rightSlideMotor.setPosition(1);
            leftSlideMotor.setPosition(0);
        }else{
            rightSlideMotor.setPosition(0);
            leftSlideMotor.setPosition(1);
        }
    }
    public void SetFlipMotorPos(FlipMotorPos flipMotorPos){
        if (flipMotorPos == FlipMotorPos.OUT) {
            rightFlipMotor.setPosition(0);
            leftFlipMotor.setPosition(1);
        }
        else if(flipMotorPos == FlipMotorPos.IN){
            rightFlipMotor.setPosition(0.72);
            leftFlipMotor.setPosition(0.28);
        }
        else if(flipMotorPos == FlipMotorPos.HANG){
            rightFlipMotor.setPosition(0.9);
            leftFlipMotor.setPosition(0.1);
        }
    }
    public void SetViperSlidePos(double revsFromBottom){
        int encoderCountsFromBottom = (int)Math.round(revsFromBottom * ViperSlideMotorEncoderResolution);
        rightViperSlide.setTargetPosition(encoderCountsFromBottom);
        leftViperSlide.setTargetPosition(-encoderCountsFromBottom);
        rightViperSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftViperSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void IntakeSystem(boolean slideOut, FlipMotorPos flipMotorOut) {
        SetFlipMotorPos(flipMotorOut);
        SetDrawerSlidePos(slideOut);
    }
}
