/* Copyright (c) 2018 FIRST. All rights reserved.
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

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

import java.util.ArrayList;
import java.util.List;


/**
 * This 2018-2019 OpMode illustrates the basics of using the Vuforia localizer to determine
 * positioning and orientation of robot on the FTC field.
 * The code is structured as a LinearOpMode
 *
 * Vuforia uses the phone's camera to inspect it's surroundings, and attempt to locate target images.
 *
 * When images are located, Vuforia is able to determine the position and orientation of the
 * image relative to the camera.  This sample code than combines that information with a
 * knowledge of where the target images are on the field, to determine the location of the camera.
 *
 * This example assumes a "square" field configuration where the red and blue alliance stations
 * are on opposite walls of each other.
 *
 * From the Audience perspective, the Red Alliance station is on the right and the
 * Blue Alliance Station is on the left.

 * The four vision targets are located in the center of each of the perimeter walls with
 * the images facing inwards towards the robots:
 *     - BlueRover is the Mars Rover image target on the wall closest to the blue alliance
 *     - RedFootprint is the Lunar Footprint target on the wall closest to the red alliance
 *     - FrontCraters is the Lunar Craters image target on the wall closest to the audience
 *     - BackSpace is the Deep Space image target on the wall farthest from the audience
 *
 * A final calculation then uses the location of the camera on the robot to determine the
 * robot's location and orientation on the field.
 *
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  ftc_app/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */

@TeleOp(name="Concept: Vuforia Rover Nan", group ="Concept")
//@Disabled
public class FinalCode extends LinearOpMode {

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY = "AWlS9/z/////AAABmZYM1T0Mjkv7qjhAix38xFgXDvaBvXaJf4gdeBwhqFmXiTWLW+JleQYEpn4bzqHKk5rzOpzvNFmK7+FnfJYZaI7l+bwaCfpQbBWd0NqDhKe65QsKkR594nF8ZqbwUlhYDjLX97f8elu0ZY8jzqyU3mJ3OntbZ1SWspCn/JzF1TjKZ8J6Hc3jc/eI+fLiLhwC1kWAhyp17D3bEQF02P4yCYrkLZfg2A6Y7XTf8x2sIYPu6h4YScz4mY/zCpuQzU8ppqzEZIY0BbV3YLxw1VL2laRnFgXxSlm3foUtXsPb9GVT4jiA7qLp/xcIg41TPp0icUTBiY4Njx6v3t/NjFfajPcEe4eG22tGMdsPmqxMo1IZ";

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    // the height of the center of the target image above the floor

    // Select which camera you want use.  The FRONT camera is the one on the same side as the screen.
    // Valid choices are:  BACK or FRONT
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;


    private boolean targetVisible = false;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets that for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        VuforiaTrackables targetsRoverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsRoverRuckus);

        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * This Rover Ruckus sample places a specific target in the middle of each perimeter wall.
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        /**
         * To place the BlueRover target in the middle of the blue perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Then, we translate it along the Y axis to the blue perimeter wall.


         /**
         * To place the RedFootprint target in the middle of the red perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 180 around the field's Z axis so the image is flat against the red perimeter wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative Y axis to the red perimeter wall.
         */

        /**
         * To place the FrontCraters target in the middle of the front perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 90 around the field's Z axis so the image is flat against the front wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative X axis to the front perimeter wall.
         */


        /**
         * To place the BackSpace target in the middle of the back perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it -90 around the field's Z axis so the image is flat against the back wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the X axis to the back perimeter wall.
         */

        /**
         * Create a transformation matrix describing where the phone is on the robot.
         *
         * The coordinate frame for the robot looks the same as the field.
         * The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
         * Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
         *
         * The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
         * pointing to the LEFT side of the Robot.  It's very important when you test this code that the top of the
         * camera is pointing to the left side of the  robot.  The rotation angles don't work if you flip the phone.
         *
         * If using the rear (High Res) camera:
         * We need to rotate the camera around it's long axis to bring the rear camera forward.
         * This requires a negative 90 degree rotation on the Y axis
         *
         * If using the Front (Low Res) camera
         * We need to rotate the camera around it's long axis to bring the FRONT camera forward.
         * This requires a Positive 90 degree rotation on the Y axis
         *
         * Next, translate the camera lens to where it is on the robot.
         * In this example, it is centered (left to right), but 110 mm forward of the middle of the robot, and 200 mm above ground level.
         */


        /**  Let all the trackable listeners know where the phone is.  */


        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        /** Start tracking the data sets we care about. */
        targetsRoverRuckus.activate();
        while (opModeIsActive()) {

            // check all the trackable target to see which one (if any) is visible.
            targetVisible = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetVisible = true;

                }


                if (targetVisible) {
                    telemetry.addData("Visible Targets", "None");







                    //will see Blue Rover if it on the bottom blue line
                    if (trackable.getName() == "Blue-Rover") {


                        ElapsedTime runtime = new ElapsedTime();

                        DcMotor leftDrive;
                        DcMotor rightDrive;

                        ColorSensor colorSensorLeft = null;  // First Hardware Device Object
                        ColorSensor colorSensorRight = null;  // Second Hardware Device Object

                        DigitalChannel digitalTouchLeft;  // Third Hardware Device Object
                        DigitalChannel digitalTouchRight;  // Fourth Hardware Device Object


                        // configuring all the vars to actual parts on the robot
                        // if trying to set up configs on phone use the names given in the green " " marks

                        colorSensorLeft = hardwareMap.colorSensor.get("Color_Sensor_Left");
                        colorSensorRight = hardwareMap.colorSensor.get("Color_Sensor_Right");

                        leftDrive = hardwareMap.dcMotor.get("Left_Drive");
                        rightDrive = hardwareMap.dcMotor.get("Right_Drive");

                        rightDrive.setDirection(DcMotor.Direction.REVERSE);

                        digitalTouchLeft = hardwareMap.get(DigitalChannel.class, "sensor_digital_left");
                        digitalTouchRight = hardwareMap.get(DigitalChannel.class, "sensor_digital_right");

                        double BlueValLeft = colorSensorLeft.blue();        // these two are given by reading
                        double BlueValRight = colorSensorRight.blue();       // of the sensors not changeable
                        // long waitTimeMili = 500;                    // wait time (given in miliseconds)
                        double noColorDrive = 0.5;                 // power given when there is no line visible
                        double WheelThatNeedsCorrection = 0.1;     // power given to the wheel that needs to be moved forward
                        double WheelThatIsOnLine = 0;               // power given to the wheel that is alredy in place


                        boolean loopRunner = true;

                        if (loopRunner) {

                            while (colorSensorLeft.blue() < 50 && colorSensorRight.blue() < 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + BlueValLeft + "," + "Blue Value for right Sensor" + BlueValRight);
                                leftDrive.setPower(noColorDrive);
                                rightDrive.setPower(noColorDrive);
                                telemetry.update();

                            }
                            while (colorSensorLeft.blue() >= 50 && colorSensorRight.blue() <= 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + BlueValLeft + "," + "Blue Value for right Sensor" + BlueValRight);
                                leftDrive.setPower(WheelThatIsOnLine);
                                rightDrive.setPower(WheelThatNeedsCorrection);
                                telemetry.update();
                            }
                            while (colorSensorLeft.blue() < 50 && colorSensorRight.blue() > 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + BlueValLeft + "," + "Blue Value for right Sensor" + BlueValRight);
                                leftDrive.setPower(WheelThatNeedsCorrection);
                                rightDrive.setPower(WheelThatIsOnLine);
                                telemetry.update();
                            }
                            while (colorSensorLeft.blue() > 50 && colorSensorRight.blue() > 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + BlueValLeft + "," + "Blue Value for right Sensor" + BlueValRight);
                                // for single touch sensor
                                double power = 0;
                                leftDrive.setPower(power);
                                rightDrive.setPower(power);
                                telemetry.update();
                                sleep(2000);
                                break;

                            }
                            /**
                             * this is for the building team to figure out idk what the turns should
                             * be and the only way to figure it out is to experiment with variables
                             */
                            // this is the straight part (might not be actually needed)
                            leftDrive.setPower(.5); //power given
                            rightDrive.setPower(.5); //power given
                            sleep(1500); //how long it goes for

                            //this is the turn
                            leftDrive.setPower(0); //power given
                            rightDrive.setPower(.5); //power given
                            sleep(3000); // amount of time it turns for



                            while (digitalTouchLeft.getState() == true && digitalTouchRight.getState() == true) {
                                telemetry.addData("Digital Touch Sensors", "Both are not pressed");
                                leftDrive.setPower(1.00);
                                rightDrive.setPower(1.00);
                                telemetry.update();
                            }
                            while (digitalTouchLeft.getState() == false && digitalTouchRight.getState() == true) {
                                telemetry.addData("Digital Touch Sensors", "Left touch is only being pressed");
                                leftDrive.setPower(0);
                                rightDrive.setPower(1.00);
                                telemetry.update();
                            }
                            while (digitalTouchLeft.getState() == true && digitalTouchRight.getState() == false) {
                                telemetry.addData("Digital Touch Sensors", "Right touch is only being pressed");
                                leftDrive.setPower(1.00);
                                rightDrive.setPower(0);
                                telemetry.update();
                            }

                            while (digitalTouchLeft.getState() == false && digitalTouchRight.getState() == false){
                                telemetry.addData(" Digital Touch Sensors", "Both are pressed");
                                // for single touch sensor
                                double power = 0;
                                leftDrive.setPower(power);
                                rightDrive.setPower(power);
                                telemetry.update();
                                break;
                            }
                            // this fully aligns the robot to the wall.
                            leftDrive.setPower(1.00);
                            rightDrive.setPower(1.00);
                            sleep (300);
                            leftDrive.setPower(0.00);
                            rightDrive.setPower(0.00);
                            sleep (500);

                            //backing up and turning
                            leftDrive.setPower(-1.00); //full power backwards
                            rightDrive.setPower(-1.00); //full power backwards
                            sleep(300); //  3/10's of a second going bacwards
                            leftDrive.setPower(-1.00); //full power backwards
                            rightDrive.setPower(0);
                            sleep(2000); // 2 full seconds for turn

                            //idk what the 50 should be.
                            // this moves
                            if (colorSensorLeft.blue()<= 50 && colorSensorRight.blue()<= 50) {

                                double moveForward = 1.00;

                                leftDrive.setPower(moveForward);
                                rightDrive.setPower(moveForward);

                            }

                            double stop = 0.00;
                            leftDrive.setPower(stop);
                            rightDrive.setPower(stop);

                            // then input the code for flag drop

                            double moveBack = -1.00;
                            leftDrive.setPower(moveBack);
                            rightDrive.setPower(moveBack);
                            sleep(10000000); //idk how long its should go



                        }


                    }










                    //will see Red Footprint if it on the bottom red line
                    if (targetsRoverRuckus.getName() == "Red-Footprint") {


                        DcMotor leftDrive;
                        DcMotor rightDrive;

                        ColorSensor colorSensorLeft = null;  // First Hardware Device Object
                        ColorSensor colorSensorRight = null;  // Second Hardware Device Object

                        DigitalChannel digitalTouchLeft;  // Third Hardware Device Object
                        DigitalChannel digitalTouchRight;  // Fourth Hardware Device Object


                        // configuring all the vars to actual parts on the robot
                        // if trying to set up configs on phone use the names given in the green " " marks

                        colorSensorLeft = hardwareMap.colorSensor.get("Color_Sensor_Left");
                        colorSensorRight = hardwareMap.colorSensor.get("Color_Sensor_Right");

                        leftDrive = hardwareMap.dcMotor.get("Left_Drive");
                        rightDrive = hardwareMap.dcMotor.get("Right_Drive");

                        rightDrive.setDirection(DcMotor.Direction.REVERSE);

                        digitalTouchLeft = hardwareMap.get(DigitalChannel.class, "sensor_digital_left");
                        digitalTouchRight = hardwareMap.get(DigitalChannel.class, "sensor_digital_right");

                        double RedValLeft = colorSensorLeft.red();        // these two are given by reading
                        double RedValRight = colorSensorRight.red();       // of the sensors not changeable
                        // long waitTimeMili = 500;                    // wait time (given in miliseconds)
                        double noColorDrive = 0.5;                 // power given when there is no line visible
                        double WheelThatNeedsCorrection = 0.1;     // power given to the wheel that needs to be moved forward
                        double WheelThatIsOnLine = 0;               // power given to the wheel that is alredy in place


                        boolean loopRunner = true;

                        if (loopRunner) {

                            while (colorSensorLeft.red() < 50 && colorSensorRight.red() < 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + RedValLeft + "," + "Blue Value for right Sensor" + RedValRight);
                                leftDrive.setPower(noColorDrive);
                                rightDrive.setPower(noColorDrive);
                                telemetry.update();

                            }
                            while (colorSensorLeft.red() >= 50 && colorSensorRight.red() <= 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + RedValLeft + "," + "Blue Value for right Sensor" + RedValRight);
                                leftDrive.setPower(WheelThatIsOnLine);
                                rightDrive.setPower(WheelThatNeedsCorrection);
                                telemetry.update();
                            }
                            while (colorSensorLeft.red() < 50 && colorSensorRight.red() > 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + RedValLeft + "," + "Blue Value for right Sensor" + RedValRight);
                                leftDrive.setPower(WheelThatNeedsCorrection);
                                rightDrive.setPower(WheelThatIsOnLine);
                                telemetry.update();
                            }
                            while (colorSensorLeft.red() > 50 && colorSensorRight.red() > 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + RedValLeft + "," + "Blue Value for right Sensor" + RedValRight);
                                // for single touch sensor
                                double power = 0;
                                leftDrive.setPower(power);
                                rightDrive.setPower(power);
                                telemetry.update();
                                sleep(2000);
                                break;

                            }

                            /**
                             * this is for the building team to figure out idk what the turns should
                             * be and the only way to figure it out is to experiment with variables
                             */
                            // this is the straight part (might not be actually needed)
                            leftDrive.setPower(.5); //power given
                            rightDrive.setPower(.5); //power given
                            sleep(1500); //how long it goes for

                            //this is the turn
                            leftDrive.setPower(0); //power given
                            rightDrive.setPower(.5); //power given
                            sleep(3000); // amount of time it turns for




                            while (digitalTouchLeft.getState() == true && digitalTouchRight.getState() == true) {
                                telemetry.addData("Digital Touch Sensors", "Both are not pressed");
                                leftDrive.setPower(1.00);
                                rightDrive.setPower(1.00);
                                telemetry.update();
                            }
                            while (digitalTouchLeft.getState() == false && digitalTouchRight.getState() == true) {
                                telemetry.addData("Digital Touch Sensors", "Left touch is only being pressed");
                                leftDrive.setPower(0);
                                rightDrive.setPower(1.00);
                                telemetry.update();
                            }
                            while (digitalTouchLeft.getState() == true && digitalTouchRight.getState() == false) {
                                telemetry.addData("Digital Touch Sensors", "Right touch is only being pressed");
                                leftDrive.setPower(1.00);
                                rightDrive.setPower(0);
                                telemetry.update();
                            }

                            while (digitalTouchLeft.getState() == false && digitalTouchRight.getState() == false){
                                telemetry.addData(" Digital Touch Sensors", "Both are pressed");
                                // for single touch sensor
                                double power = 0;
                                leftDrive.setPower(power);
                                rightDrive.setPower(power);
                                telemetry.update();
                                break;
                            }
                            // this fully aligns the robot to the wall.
                            leftDrive.setPower(1.00);
                            rightDrive.setPower(1.00);
                            sleep (500);
                            leftDrive.setPower(0.00);
                            rightDrive.setPower(0.00);
                            sleep (500);

                            //backing up and turning
                            leftDrive.setPower(-1.00); //full power backwards
                            rightDrive.setPower(-1.00); //full power backwards
                            sleep(300); //  3/10's of a second going bacwards
                            leftDrive.setPower(-1.00); //full power backwards
                            rightDrive.setPower(0);
                            sleep(2000); // 2 full seconds for turn

                            //idk what the 50 should be.
                            // this moves until line
                            if (colorSensorLeft.red()<= 50 && colorSensorRight.red()<= 50) {

                                double moveForward = 1.00;

                                leftDrive.setPower(moveForward);
                                rightDrive.setPower(moveForward);

                            }

                            double stop = 0.00;
                            leftDrive.setPower(stop);
                            rightDrive.setPower(stop);

                            // then input the code for flag drop

                            double moveBack = -1.00;
                            leftDrive.setPower(moveBack);
                            rightDrive.setPower(moveBack);
                            sleep(10000000); //idk how long its should go

                        }

                    }









                    //will see Front Crater if it on the top red line
                    if (targetsRoverRuckus.getName() == "Front-Craters") {


                        DcMotor leftDrive;
                        DcMotor rightDrive;

                        ColorSensor colorSensorLeft = null;  // First Hardware Device Object
                        ColorSensor colorSensorRight = null;  // Second Hardware Device Object

                        DigitalChannel digitalTouchLeft;  // Third Hardware Device Object
                        DigitalChannel digitalTouchRight;  // Fourth Hardware Device Object


                        // configuring all the vars to actual parts on the robot
                        // if trying to set up configs on phone use the names given in the green " " marks

                        colorSensorLeft = hardwareMap.colorSensor.get("Color_Sensor_Left");
                        colorSensorRight = hardwareMap.colorSensor.get("Color_Sensor_Right");

                        leftDrive = hardwareMap.dcMotor.get("Left_Drive");
                        rightDrive = hardwareMap.dcMotor.get("Right_Drive");

                        rightDrive.setDirection(DcMotor.Direction.REVERSE);

                        digitalTouchLeft = hardwareMap.get(DigitalChannel.class, "sensor_digital_left");
                        digitalTouchRight = hardwareMap.get(DigitalChannel.class, "sensor_digital_right");

                        double BlueValLeft = colorSensorLeft.blue();        // these two are given by reading
                        double BlueValRight = colorSensorRight.blue();       // of the sensors not changeable
                        // long waitTimeMili = 500;                    // wait time (given in miliseconds)
                        double noColorDrive = 0.5;                 // power given when there is no line visible
                        double WheelThatNeedsCorrection = 0.1;     // power given to the wheel that needs to be moved forward
                        double WheelThatIsOnLine = 0;               // power given to the wheel that is alredy in place

                        boolean looprunner = true;

                        if (looprunner) {

                            while (colorSensorLeft.blue() < 50 && colorSensorRight.blue() < 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + BlueValLeft + "," + "Blue Value for right Sensor" + BlueValRight);
                                leftDrive.setPower(noColorDrive);
                                rightDrive.setPower(noColorDrive);
                                telemetry.update();
                            }
                            while (colorSensorLeft.blue() >= 50 && colorSensorRight.blue() <= 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + BlueValLeft + "," + "Blue Value for right Sensor" + BlueValRight);
                                leftDrive.setPower(WheelThatIsOnLine);
                                rightDrive.setPower(WheelThatNeedsCorrection);
                                telemetry.update();
                            }
                            while (colorSensorLeft.blue() < 50 && colorSensorRight.blue() > 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + BlueValLeft + "," + "Blue Value for right Sensor" + BlueValRight);
                                leftDrive.setPower(WheelThatNeedsCorrection);
                                rightDrive.setPower(WheelThatIsOnLine);
                                telemetry.update();
                            }
                            while (colorSensorLeft.blue() > 50 && colorSensorRight.blue() > 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + BlueValLeft + "," + "Blue Value for right Sensor" + BlueValRight);
                                // for single touch sensor
                                double power = 0;
                                leftDrive.setPower(power);
                                rightDrive.setPower(power);
                                telemetry.update();
                                sleep(2000);
                                break;

                            }

                            /**
                             * this is for the building team to figure out idk what the turns should
                             * be and the only way to figure it out is to experiment with variables
                             */
                            // this is the straight part (might not be actually needed)
                            leftDrive.setPower(.5); //power given
                            rightDrive.setPower(.5); //power given
                            sleep(1500); //how long it goes for

                            //this is the turn
                            leftDrive.setPower(0); //power given
                            rightDrive.setPower(.5); //power given
                            sleep(3000); // amount of time it turns for



                            while (digitalTouchLeft.getState() == true && digitalTouchRight.getState() == true) {
                                telemetry.addData("Digital Touch Sensors", "Both are not pressed");
                                leftDrive.setPower(1.00);
                                rightDrive.setPower(1.00);
                                telemetry.update();
                            }
                            while (digitalTouchLeft.getState() == false && digitalTouchRight.getState() == true) {
                                telemetry.addData("Digital Touch Sensors", "Left touch is only being pressed");
                                leftDrive.setPower(0);
                                rightDrive.setPower(1.00);
                                telemetry.update();
                            }
                            while (digitalTouchLeft.getState() == true && digitalTouchRight.getState() == false) {
                                telemetry.addData("Digital Touch Sensors", "Right touch is only being pressed");
                                leftDrive.setPower(1.00);
                                rightDrive.setPower(0);
                                telemetry.update();
                            }

                            while (digitalTouchLeft.getState() == false && digitalTouchRight.getState() == false){
                                telemetry.addData(" Digital Touch Sensors", "Both are pressed");
                                // for single touch sensor
                                double power = 0;
                                leftDrive.setPower(power);
                                rightDrive.setPower(power);
                                telemetry.update();
                                break;
                            }

                            // this fully aligns the robot to the wall.
                            leftDrive.setPower(1.00);
                            rightDrive.setPower(1.00);
                            sleep (500);
                            leftDrive.setPower(0.00);
                            rightDrive.setPower(0.00);
                            sleep (500);

                            //backing up and turning
                            leftDrive.setPower(-1.00); //full power backwards
                            rightDrive.setPower(-1.00); //full power backwards
                            sleep(300); //  3/10's of a second going bacwards
                            leftDrive.setPower(-1.00); //full power backwards
                            rightDrive.setPower(0);
                            sleep(2000); // 2 full seconds for turn

                            //idk what the 50 should be.
                            // this moves
                            if (colorSensorLeft.blue()<= 50 && colorSensorRight.blue()<= 50) {

                                double moveForward = 1.00;

                                leftDrive.setPower(moveForward);
                                rightDrive.setPower(moveForward);

                            }

                            double stop = 0.00;
                            leftDrive.setPower(stop);
                            rightDrive.setPower(stop);

                            // then input the code for flag drop

                            double moveBack = -1.00;
                            leftDrive.setPower(moveBack);
                            rightDrive.setPower(moveBack);
                            sleep(10000000); //idk how long its should go

                        }


                    }










                    //will see Back Space if it on the bottom blue line
                    if (targetsRoverRuckus.getName() == "Back-Space") {


                        DcMotor leftDrive;
                        DcMotor rightDrive;

                        ColorSensor colorSensorLeft = null;  // First Hardware Device Object
                        ColorSensor colorSensorRight = null;  // Second Hardware Device Object

                        DigitalChannel digitalTouchLeft;  // Third Hardware Device Object
                        DigitalChannel digitalTouchRight;  // Fourth Hardware Device Object


                        // configuring all the vars to actual parts on the robot
                        // if trying to set up configs on phone use the names given in the green " " marks

                        colorSensorLeft = hardwareMap.colorSensor.get("Color_Sensor_Left");
                        colorSensorRight = hardwareMap.colorSensor.get("Color_Sensor_Right");

                        leftDrive = hardwareMap.dcMotor.get("Left_Drive");
                        rightDrive = hardwareMap.dcMotor.get("Right_Drive");
                        rightDrive.setDirection(DcMotor.Direction.REVERSE);


                        digitalTouchLeft = hardwareMap.get(DigitalChannel.class, "sensor_digital_left");
                        digitalTouchRight = hardwareMap.get(DigitalChannel.class, "sensor_digital_right");

                        double RedValLeft = colorSensorLeft.red();        // these two are given by reading
                        double RedValRight = colorSensorRight.red();       // of the sensors not changeable
                        // long waitTimeMili = 500;                    // wait time (given in miliseconds)
                        double noColorDrive = 0.5;                 // power given when there is no line visible
                        double WheelThatNeedsCorrection = 0.1;     // power given to the wheel that needs to be moved forward
                        double WheelThatIsOnLine = 0;               // power given to the wheel that is alredy in place


                        boolean loopRunner = true;

                        if (loopRunner) {

                            while (colorSensorLeft.red() < 50 && colorSensorRight.red() < 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + RedValLeft + "," + "Blue Value for right Sensor" + RedValRight);
                                leftDrive.setPower(noColorDrive);
                                rightDrive.setPower(noColorDrive);
                                telemetry.update();

                            }
                            while (colorSensorLeft.red() >= 50 && colorSensorRight.red() <= 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + RedValLeft + "," + "Blue Value for right Sensor" + RedValRight);
                                leftDrive.setPower(WheelThatIsOnLine);
                                rightDrive.setPower(WheelThatNeedsCorrection);
                                telemetry.update();
                            }
                            while (colorSensorLeft.red() < 50 && colorSensorRight.red() > 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + RedValLeft + "," + "Blue Value for right Sensor" + RedValRight);
                                leftDrive.setPower(WheelThatNeedsCorrection);
                                rightDrive.setPower(WheelThatIsOnLine);
                                telemetry.update();
                            }
                            while (colorSensorLeft.red() > 50 && colorSensorRight.red() > 50) {
                                telemetry.addData("Digital Touch Sensors", "Blue Value for left Sensor" + RedValLeft + "," + "Blue Value for right Sensor" + RedValRight);
                                // for single touch sensor
                                double power = 0;
                                leftDrive.setPower(power);
                                rightDrive.setPower(power);
                                telemetry.update();
                                sleep(2000);
                                break;

                            }

                            /**
                             * this is for the building team to figure out idk what the turns should
                             * be and the only way to figure it out is to experiment with variables
                             */
                            // this is the straight part (might not be actually needed)
                            leftDrive.setPower(.5); //power given
                            rightDrive.setPower(.5); //power given
                            sleep(1500); //how long it goes for

                            //this is the turn
                            leftDrive.setPower(0); //power given
                            rightDrive.setPower(.5); //power given
                            sleep(3000); // amount of time it turns for


                            while (digitalTouchLeft.getState() == true && digitalTouchRight.getState() == true) {
                                telemetry.addData("Digital Touch Sensors", "Both are not pressed");
                                leftDrive.setPower(1.00);
                                rightDrive.setPower(1.00);
                                telemetry.update();
                            }
                            while (digitalTouchLeft.getState() == false && digitalTouchRight.getState() == true) {
                                telemetry.addData("Digital Touch Sensors", "Left touch is only being pressed");
                                leftDrive.setPower(0);
                                rightDrive.setPower(1.00);
                                telemetry.update();
                            }
                            while (digitalTouchLeft.getState() == true && digitalTouchRight.getState() == false) {
                                telemetry.addData("Digital Touch Sensors", "Right touch is only being pressed");
                                leftDrive.setPower(1.00);
                                rightDrive.setPower(0);
                                telemetry.update();
                            }

                            while (digitalTouchLeft.getState() == false && digitalTouchRight.getState() == false){
                                telemetry.addData(" Digital Touch Sensors", "Both are pressed");
                                // for single touch sensor
                                double power = 0;
                                leftDrive.setPower(power);
                                rightDrive.setPower(power);
                                telemetry.update();
                                break;
                            }
                        // this fully aligns the robot to the wall.
                            leftDrive.setPower(1.00);
                            rightDrive.setPower(1.00);
                            sleep (500);
                            leftDrive.setPower(0.00);
                            rightDrive.setPower(0.00);
                            sleep (500);

                            //backing up and turning
                            leftDrive.setPower(-1.00); //full power backwards
                            rightDrive.setPower(-1.00); //full power backwards
                            sleep(300); //  3/10's of a second going bacwards
                            leftDrive.setPower(-1.00); //full power backwards
                            rightDrive.setPower(0);
                            sleep(2000); // 2 full seconds for turn

                            //idk what the 50 should be.
                            // this moves
                            if (colorSensorLeft.blue()<= 50 && colorSensorRight.blue()<= 50) {

                                double moveForward = 1.00;

                                leftDrive.setPower(moveForward);
                                rightDrive.setPower(moveForward);

                            }

                            double stop = 0.00;
                            leftDrive.setPower(stop);
                            rightDrive.setPower(stop);

                            // then input the code for flag drop

                            double moveBack = -1.00;
                            leftDrive.setPower(moveBack);
                            rightDrive.setPower(moveBack);
                            sleep(10000000); //idk how long its should go


                        }

                    }
                }


                telemetry.update();
            }
        }
    }
}
