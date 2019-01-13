/**
 * 
 */
package le2lejosev3.robots.ev3rstorm;

import java.util.logging.Level;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.Display;
import le2lejosev3.pblocks.MoveSteering;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

/**
 * Ev3rStorm Robot Mission 01
 * 
 * @author Roland Blochberger
 */
public class Ev3rStorm01 {

	private static Class<?> clazz = Ev3rStorm01.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	static final Port motorPortB = MotorPort.B; // Large Motor
	static final Port motorPortC = MotorPort.C; // Large Motor

	// move steering block with both large motors
	private static final MoveSteering move = new MoveSteering(motorPortB, motorPortC);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file for all levels
		Setup.log2File(clazz, Level.ALL);
		// setup logging to file
		// Setup.log2File(clazz);
		log.info("Starting ...");

		// Display image file "Neutral" at coordinates 0,0 with clear screen before
		Display.image("Neutral", true, 0, 0);
		// Move Steering straight on for 5 rotations with power 75 and brake at end
		move.motorsOnForRotations(0, 75, 5, true);

		// Display image file "Middle left" at coordinates 0,0 with clear screen before
		Display.image("Middle left", true, 0, 0);
		// Move Steering direction 50 on for 5 rotations with power 75 and brake at end
		move.motorsOnForRotations(50, 75, 5, true);

		// Display image file "Neutral" at coordinates 0,0 with clear screen before
		Display.image("Neutral", true, 0, 0);
		// Move Steering straight on for 5 rotations with power 75 and brake at end
		move.motorsOnForRotations(0, 75, 5, true);

		// Display image file "Middle right" at coordinates 0,0 with clear screen before
		Display.image("Middle right", true, 0, 0);
		// Move Steering direction -50 on for 5 rotations with power 75 and brake at end
		move.motorsOnForRotations(-50, 75, 5, true);

		log.info("The End");
	}

}
