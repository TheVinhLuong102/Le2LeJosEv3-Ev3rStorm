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
import le2lejosev3.pblocks.Timer;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Ev3rStorm Robot Mission 04
 * 
 * @author Roland Blochberger
 */
public class Ev3rStorm04 {

	private static Class<?> clazz = Ev3rStorm04.class;
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

			// measure proximity and compare it with 25
			if (infra.measureProximity() < 25) {
				log.info("object detected");
				// Move Tank stop and brake at end
				move.motorsOff(true);
				// BrickStatusLight on with color red and pulsing
				BrickStatusLight.on(BrickStatusLight.COLOR_RED, BrickStatusLight.PULSE);
				// play sound file "Object" with volume 100 and wait until done (play type 0)
				Sound.playFile("Object", 100, Sound.WAIT);
				// play sound file "Detected" with volume 100 and wait until done (play type 0)
				Sound.playFile("Detected", 100, Sound.WAIT);
				// play sound file "Error alarm" with volume 100 and wait until done (play type
				// 0)
				Sound.playFile("Error alarm", 100, Sound.WAIT);

				// Medium motor A on with power 100
				motA.motorOn(100);
				// MoveTank on with power 100, 80 for 1 rotation and brake at end
				move.motorsOnForRotations(100, 80, 1, true);
				// Medium motor A on with power -100
				motA.motorOn(-100);
				// MoveTank on with power -100, -80 for 1 rotation and brake at end
				move.motorsOnForRotations(-100, -80, 1, true);
				// Medium motor A off with brake at end
				motA.motorOff(true);
				// MoveTank on with power 100, -100 for 2 rotations and brake at end
				move.motorsOnForRotations(100, -100, 2, true);
				// MoveTank on with power 0, 100 for 1 rotations and brake at end
				move.motorsOnForRotations(0, 100, 1, true);

			} else {
				log.info("free running");
				// BrickStatusLight on with color green and pulsing
				BrickStatusLight.on(BrickStatusLight.COLOR_GREEN, BrickStatusLight.PULSE);
				// call Skate Block with power 100
				skate(100);
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
}
