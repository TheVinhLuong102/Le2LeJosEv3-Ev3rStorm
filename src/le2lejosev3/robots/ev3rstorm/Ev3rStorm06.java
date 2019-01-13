/**
 * 
 */
package le2lejosev3.robots.ev3rstorm;

import java.util.logging.Level;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.BrickStatusLight;
import le2lejosev3.pblocks.InfraredSensor;
import le2lejosev3.pblocks.MediumMotor;
import le2lejosev3.pblocks.MoveTank;
import le2lejosev3.pblocks.Sound;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Ev3rStorm Robot Mission 05 using a MoveTank with the Large Motors
 * 
 * @author Roland Blochberger
 */
public class Ev3rStorm06 {

	private static Class<?> clazz = Ev3rStorm06.class;
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
	// move tank block with both large motors
	private static final MoveTank move = new MoveTank(motorPortB, motorPortC);
	// the sensors
	// private static final TouchSensor touch = new TouchSensor(touchPort1);
	// private static final ColorSensor color = new ColorSensor(colorPort3);
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

		// Loop 01
		while (Button.ESCAPE.isUp()) {

			// Brick Status Light on color orange (1) and pulse
			BrickStatusLight.on(BrickStatusLight.COLOR_ORANGE, BrickStatusLight.PULSE);

			// Check result of IR Target Block
			if (irTarget(-3, 70)) {

				// Brick Status Light on color red (2) and pulse
				BrickStatusLight.on(BrickStatusLight.COLOR_RED, BrickStatusLight.PULSE);
				// stop both large motors with brake at end
				move.motorsOff(true);
				// Medium motor A on with power 100 for 3 rotations and brake at end
				motA.motorOnForRotations(100, 3, true);
				// Sound play file "Laughing 2" with volume 100 and wait until done (0)
				Sound.playFile("Laughing 2", 100, Sound.WAIT);
			}
		}

		log.info("The End");
	}

	/**
	 * IR Target Block.
	 * 
	 * @param heading   expected heading to beacon.
	 * @param proximity expected proximity to beacon.
	 * @return true if beacon is at the expected heading and proximity.
	 */
	private static boolean irTarget(int heading, int proximity) {
		// measure Infrared beacon heading, distance, and detected
		int[] irValues = infra.measureBeacon(1);
		// extract measured values for better readibility
		int measuredHeading = irValues[0];
		int measuredProximity = irValues[1];
		boolean detected = (irValues[2] != 0);
		// calculations:
		// compare headings and proximities
		boolean notequal = ((heading != measuredHeading) || (proximity != measuredProximity));
		if (notequal && detected) {
			// beacon detected but off-target:
			// calculate right targeting movement (b - d) * 3 - (a - c) * 4
			int powerRight = ((measuredProximity - proximity) * 3) - ((measuredHeading - heading) * 4);
			// calculate left targeting movement (b - d) * 3 + (a - c) * 4
			int powerLeft = ((measuredProximity - proximity) * 3) + ((measuredHeading - heading) * 4);
			// switch both motors on with their respective power
			move.motorsOn(powerLeft, powerRight);

		} else {
			// beacon not detected or on-target
			// stop both large motors with brake at end
			move.motorsOff(true);
		}
		return (!notequal && detected);
	}
}
