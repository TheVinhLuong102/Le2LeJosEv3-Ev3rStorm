/**
 * 
 */
package le2lejosev3.robots.ev3rstorm;

import java.util.logging.Level;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.ColorSensor;
import le2lejosev3.pblocks.Display;
import le2lejosev3.pblocks.InfraredSensor;
import le2lejosev3.pblocks.LargeMotor;
import le2lejosev3.pblocks.MediumMotor;
import le2lejosev3.pblocks.Sound;
import le2lejosev3.pblocks.Timer;
import le2lejosev3.pblocks.TouchSensor;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Ev3rStorm Robot Mission 05 using individual Large Motors.
 * 
 * @author Roland Blochberger
 */
public class Ev3rStorm05 {

	private static Class<?> clazz = Ev3rStorm05.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	static final Port motorPortA = MotorPort.A; // Medium Motor
	static final Port motorPortB = MotorPort.B; // Large Motor
	static final Port motorPortC = MotorPort.C; // Large Motor
	static final Port touchPort1 = SensorPort.S1; // Touch Sensor
	static final Port colorPort3 = SensorPort.S3; // Color Sensor
	static final Port infraPort4 = SensorPort.S4; // Infrared Sensor

	// the medium motor
	private static final MediumMotor motA = new MediumMotor(motorPortA);
	// the large motors
	private static final LargeMotor motB = new LargeMotor(motorPortB);
	private static final LargeMotor motC = new LargeMotor(motorPortC);
	// the sensors
	private static final TouchSensor touch = new TouchSensor(touchPort1);
	private static final ColorSensor color = new ColorSensor(colorPort3);
	private static final InfraredSensor infra = new InfraredSensor(infraPort4);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file for all levels
		Setup.log2File(clazz, Level.ALL);
		// setup logging to file
		// Setup.log2File(clazz);
		log.info("Starting ...");

		// Display image file "Target" at coordinates 0,0 with clear screen before
		Display.image("Target", true, 0, 0);

		// start the IR remote control thread
		Thread irth = new InfraredControlThread();
		irth.start();

		// Loop 01
		while (Button.ESCAPE.isUp()) {

			// wait until touch sensor pressed (state 1)
			touch.waitCompareState(TouchSensor.PRESSED);

			// measure ambient light intensity and compare it with 25
			if (color.measureAmbientLightIntensity() < 25) {

				// play sound file "Up" with volume 100 and wait until done (play type 0)
				Sound.playFile("Up", 100, Sound.WAIT);
				// Medium motor A on with power -100 for 3 rotations and brake at end
				motA.motorOnForRotations(-100, 3, true);

			} else {
				
				// play sound file "Down" with volume 100 and wait until done (play type 0)
				Sound.playFile("Down", 100, Sound.WAIT);
				// Medium motor A on with power 100 for 3 rotations and brake at end
				motA.motorOnForRotations(100, 3, true);

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
			motB.motorOn(power / 2);
			// Large motor C on with full power
			motC.motorOn(power);

		} else {

			// Large motor C on with half of the power
			motC.motorOn(power / 2);
			// Large motor B on with full power
			motB.motorOn(power);
		}
	}

	/**
	 * IR Control Block.
	 * 
	 * @param power
	 */
	private static void irControl(int power) {
		// react on infrared remote command on channel 1
		switch (infra.measureRemote(1)) {
		case InfraredSensor.TOP_LEFT:
			motB.motorOn(power);
			motC.motorOn(0);
			break;

		case InfraredSensor.BOTTOM_LEFT:
			motB.motorOn(-power);
			motC.motorOn(0);
			break;

		case InfraredSensor.TOP_RIGHT:
			motB.motorOn(0);
			motC.motorOn(power);
			break;

		case InfraredSensor.BOTTOM_RIGHT:
			motB.motorOn(0);
			motC.motorOn(-power);
			break;

		case InfraredSensor.TOP_BOTH:
			skate(power);
			break;

		case InfraredSensor.TOP_LEFT_BOTTOM_RIGHT:
			motB.motorOn(power);
			motC.motorOn(-power);
			break;

		case InfraredSensor.TOP_RIGHT_BOTTOM_LEFT:
			motB.motorOn(-power);
			motC.motorOn(power);
			break;

		case InfraredSensor.BOTTOM_BOTH:
			skate(-power);
			break;

		case InfraredSensor.NONE:
		default:
			// stop both Large motors and do not brake
			motB.motorOff(false);
			motC.motorOff(false);
			break;
		}
	}

	/**
	 * Thread handling infrared remote control.
	 */
	static class InfraredControlThread extends Thread {

		/**
		 * Constructor
		 */
		public InfraredControlThread() {
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
				// call IR Control Block
				irControl(100);
			}
		}
	}
}
