package org.firstinspires.ftc.teamcode.General;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

enum ViperSlideDirections {
    UPWARDS,
    DOWNWARDS,
    NONE
}

enum IntakeMotorStates{
    IN,
    OUT,
    NONE
}
public class RobotHardware {

    /* Declare OpMode members. */
    private final LinearOpMode myOpMode;   // gain access to methods in the calling OpMode.

    // Define Motor and Servo objects  (Make them private so they can't be accessed externally)
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

    // Define a constructor that allows the OpMode to pass a reference to itself.
    public RobotHardware(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    /**
     * Initialize all the robot's hardware.
     * This method must be called ONCE when the OpMode is initialized.
     * <p>
     * All of the hardware devices are accessed via the hardware map, and initialized.
     */
    public void init()    {
        // Define and Initialize Motors (note: need to use reference to actual OpMode).
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


//        // AP: As long as all the left motors' directions are different from the right ones it should be fine
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightSlideMotor.scaleRange(0.43,0.67);
        leftSlideMotor.scaleRange(0.33,0.57);

//        leftViperSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightViperSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftViperSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightViperSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set initial positions for servos
        rightSlideMotor.setPosition(1);  // Retracted
        leftSlideMotor.setPosition(0);   // Retracted

        rightFlipMotor.setPosition(0.55); // Default position
        leftFlipMotor.setPosition(0.45);  // Default position

        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }

    public void DriveChain(double slowModeMult, double y, double x, double rx){

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]

        //AP: Don't even ask me how this works, I'm not a vectors wizard.... gm0.com
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;


        frontLeftPower *= slowModeMult;
        backLeftPower *= slowModeMult;
        frontRightPower *= slowModeMult;//bad
        backRightPower *= slowModeMult;


        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    public void DriveByEncoderTicks(int forwardTicks, int strafeTicks, int rotateTicks) {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Calculate the target positions for mecanum movement
        int frontLeftTarget = forwardTicks + strafeTicks + rotateTicks;
        int backLeftTarget = forwardTicks - strafeTicks + rotateTicks;
        int frontRightTarget = forwardTicks - strafeTicks - rotateTicks;
        int backRightTarget = forwardTicks + strafeTicks - rotateTicks;

        // Set target positions
        frontLeft.setTargetPosition(frontLeftTarget);
        backLeft.setTargetPosition(backLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backRight.setTargetPosition(backRightTarget);

        // Set mode to RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power (ensure all wheels move at the same rate)
        frontLeft.setPower(0.5);
        backLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backRight.setPower(0.5);

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
            viperSlideClaw.setPosition(0);
        }
        else{
            viperSlideClaw.setPosition(1);
        }
    }

    public void SetViperSlideMovement(double slowModeMult, ViperSlideDirections viperSlideMovement){
        leftViperSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightViperSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        switch(viperSlideMovement){
            case UPWARDS:
                leftViperSlide.setDirection(DcMotorSimple.Direction.REVERSE); //Goes up
                rightViperSlide.setDirection(DcMotorSimple.Direction.FORWARD);

                if(rightViperSlide.getCurrentPosition() / ViperSlideMotorEncoderResolution < 8){
                    leftViperSlide.setPower(1 * (slowModeMult + 0.3));
                    rightViperSlide.setPower(1 * (slowModeMult + 0.3));
                }
                else{
                    SetViperSlideMovement(slowModeMult, ViperSlideDirections.NONE);
                }
                break;
            case DOWNWARDS:
                leftViperSlide.setDirection(DcMotorSimple.Direction.FORWARD); //Goes down
                rightViperSlide.setDirection(DcMotorSimple.Direction.REVERSE);

                if(rightViperSlide.getCurrentPosition() > 0){
                    leftViperSlide.setPower(1 * (slowModeMult + 0.3));
                    rightViperSlide.setPower(1 * (slowModeMult + 0.3));
                }
                break;
            case NONE:
                leftViperSlide.setDirection(DcMotorSimple.Direction.REVERSE);
                rightViperSlide.setDirection(DcMotorSimple.Direction.FORWARD);
                leftViperSlide.setPower(0.08);
                rightViperSlide.setPower(0.08);
                break;
        }
    }

    public void SetIntakeMotorMovement(IntakeMotorStates intakeMotorMovement){
        switch(intakeMotorMovement){
            case IN:
                intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                intakeMotor.setPower(0.3);
                break;
            case OUT:
                intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                intakeMotor.setPower(0.4);
                break;
            case NONE:
                intakeMotor.setPower(0);
                break;
        }
    }

    public void SetDrawerSlidePos(boolean drawerSlideOut){
        if(drawerSlideOut){
            rightSlideMotor.setPosition(0);
            leftSlideMotor.setPosition(1);
        }
        else{
            rightSlideMotor.setPosition(1);
            leftSlideMotor.setPosition(0);
        }
    }

    public void SetFlipMotorPos(boolean flipMotorOut){
        if(flipMotorOut){
            rightFlipMotor.setPosition(0);
            leftFlipMotor.setPosition(1);
        }
        else{
            rightFlipMotor.setPosition(0.55);
            leftFlipMotor.setPosition(0.45);
        }
    }

    public void SetViperSlidePos(int revsFromBottom){
        int encoderCountsFromBottom = revsFromBottom * ViperSlideMotorEncoderResolution;
        rightViperSlide.setTargetPosition(encoderCountsFromBottom);
        leftViperSlide.setTargetPosition(-encoderCountsFromBottom);
        rightViperSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftViperSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void IntakeSystem(boolean slidePos, boolean flipMotorOut) {
        SetDrawerSlidePos(slidePos);
        SetFlipMotorPos(flipMotorOut);
    }
}
