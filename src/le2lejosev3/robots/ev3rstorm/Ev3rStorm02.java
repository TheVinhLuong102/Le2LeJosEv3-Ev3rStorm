/**
 * 
 */
package le2lejosev3.robots.ev3rstorm;

import java.util.logging.Level;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.BrickStatusLight;
import le2lejosev3.pblocks.Display;
import le2lejosev3.pblocks.MoveSteering;
import le2lejosev3.pblocks.Sound;
import le2lejosev3.pblocks.TouchSensor;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Ev3rStorm Robot Mission 02
 * 
 * @author Roland Blochberger
 */
public class Ev3rStorm02 {

	private static Class<?> clazz = Ev3rStorm02.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	static final Port motorPortB = MotorPort.B; // Large Motor
	static final Port motorPortC = MotorPort.C; // Large Motor
	static final Port touchPort1 = SensorPort.S1; // Touch Sensor

	// move steering block with both large motors
	private static final MoveSteering move = new MoveSteering(motorPortB, motorPortC);
	// the sensors
	private static final TouchSensor touch = new TouchSensor(touchPort1);

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

			// Display image file "Sleeping" at coordinates 0,0 with clear screen before
			Display.image("Sleeping", true, 0, 0);
			// Brick Status Light on color orange (1) and no pulse
			BrickStatusLight.on(BrickStatusLight.COLOR_ORANGE, BrickStatusLight.CONSTANT);
			// Sound play file "Snoring" with volume 100 and repeat in background (2)
			Sound.playFile("Snoring", 100, Sound.REPEAT);

			// Wait for Touch Sensor bumped (status 2)
			touch.waitCompareState(TouchSensor.BUMPED);

			// Display image file "Winking" at coordinates 0,0 with clear screen before
			Display.image("Winking", true, 0, 0);

			// Sound play file "Activate" with volume 100 and wait until done (0)
			Sound.playFile("Activate", 100, Sound.WAIT);
			// Sound play file "EV3" with volume 100 and wait until done (0)
			Sound.playFile("EV3", 100, Sound.WAIT);

			// Display image file "Neutral" at coordinates 0,0 with clear screen before
			Display.image("Neutral", true, 0, 0);

			// Brick Status Light on color green (0) and pulse
			BrickStatusLight.on(BrickStatusLight.COLOR_GREEN, BrickStatusLight.PULSE);
			// Move Steering turn on spot on for 1 rotation with power 75 and brake at end
			move.motorsOnForRotations(100, 75, 1, true);
			// Move Steering turn on spot on for 1 rotation with power 75 and brake at end
			move.motorsOnForRotations(-100, 75, 1, true);

			// Wait for Touch Sensor bumped (status 2)
			touch.waitCompareState(TouchSensor.BUMPED);

			// Display image file "Tired middle" at coordinates 0,0 with clear screen before
			Display.image("Tired middle", true, 0, 0);
			// Sound play file "Goodbye" with volume 100 and wait until done (0)
			Sound.playFile("Goodbye", 100, Sound.WAIT);
		}

		log.info("The End");
	}

}
