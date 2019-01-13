/**
 * 
 */
package le2lejosev3.robots.ev3rstorm;

import java.util.logging.Level;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.ColorSensor;
import le2lejosev3.pblocks.Display;
import le2lejosev3.pblocks.MediumMotor;
import le2lejosev3.pblocks.MoveTank;
import le2lejosev3.pblocks.Sound;
import le2lejosev3.pblocks.Timer;
import le2lejosev3.pblocks.TouchSensor;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Ev3rStorm Robot Mission 03
 * 
 * @author Roland Blochberger
 */
public class Ev3rStorm03 {

	private static Class<?> clazz = Ev3rStorm03.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	static final Port motorPortA = MotorPort.A; // Medium Motor
	static final Port motorPortB = MotorPort.B; // Large Motor
	static final Port motorPortC = MotorPort.C; // Large Motor
	static final Port touchPort1 = SensorPort.S1; // Touch Sensor
	static final Port colorPort3 = SensorPort.S3; // Color Sensor

	// the medium motor
	private static final MediumMotor motA = new MediumMotor(motorPortA);
	// move tank block with both large motors
	private static final MoveTank move = new MoveTank(motorPortB, motorPortC);
	// the sensors
	private static final TouchSensor touch = new TouchSensor(touchPort1);
	private static final ColorSensor color = new ColorSensor(colorPort3);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file for all levels
		Setup.log2File(clazz, Level.ALL);
		// setup logging to file
		// Setup.log2File(clazz);
		log.info("Starting ...");

		// start the touch sensor thread
		Thread tsth = new TouchSensorThread();
		tsth.start();

		// Loop 01
		while (Button.ESCAPE.isUp()) {

			// measure ambient light intensity and compare it with 25
			if (color.measureAmbientLightIntensity() < 25) {
				log.info("in the dark");
				// Display image file "Middle left" at coordinates 0,0 with clear screen before
				Display.image("Middle left", true, 0, 0);
				// Move Tank with power -80, -100 for 1.5 seconds and brake at end
				move.motorsOnForSeconds(-80, -100, 1.5F, true);

				// Display image file "Middle right" at coordinates 0,0 with clear screen before
				Display.image("Middle right", true, 0, 0);
				// Move Tank with power -100, 100 for 1.5 seconds and brake at end
				move.motorsOnForSeconds(-100, 100, 1.5F, true);

			} else {
				log.info("in the light");
				// call Skate Block with power 100
				skate(100);
				// Display image file "Awake" at coordinates 0,0 with clear screen before
				Display.image("Awake", true, 0, 0);
			}
		}

		log.info("The End");
	}

	/**
	 * Skate Block
	 * 
	 * @param power
	 */
	private static void skate(int power) {
		log.info("");
		// Timer 1 measure time
		float t = Timer.measure(1);
		// calculations
		t = (t - ((float) Math.floor(t / 3F)) * 3F);
		log.info("t: " + t);
		// compare
		if (t < 1.5F) {
			// Large motor B on with half of the power
			move.getLeftMotor().motorOn(power / 2);
			// Large motor C on with full power
			move.getRightMotor().motorOn(power);

		} else {

			// Large motor C on with half of the power
			move.getRightMotor().motorOn(power / 2);
			// Large motor B on with full power
			move.getLeftMotor().motorOn(power);
		}
	}

	/**
	 * Thread handling touch sensor press
	 */
	static class TouchSensorThread extends Thread {

		/**
		 * Constructor
		 */
		public TouchSensorThread() {
			// set as daemon to be stopped when the main program ends
			setDaemon(true);
		}

		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// Loop 01
			while (Button.ESCAPE.isUp()) {

				// wait until touch sensor pressed (state 1)
				touch.waitCompareState(TouchSensor.PRESSED);
				// play sound file "Laughing 1" with volume 100 and wait until done (play type
				// 0)
				log.info("laughing");
				Sound.playFile("Laughing 1", 100, Sound.WAIT);
				// Medium motor A on with power 100 for 6 rotations and brake at end
				motA.motorOnForRotations(100, 6, true);

			}
		}
	}
}
