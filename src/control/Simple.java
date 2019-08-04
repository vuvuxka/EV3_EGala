package control;

import lejos.hardware.Sound;
import main.Robot;

public class Simple implements Control {
	Robot robot;

	public Simple(Robot robot) {
		super();
		this.robot = robot;
	}

	public void ejecutar() throws InterruptedException {
		robot.setPos_relativa(0);
		robot.setDireccion(0);
		Thread.sleep(7000);
		
		Sound.playTone(440,800,30);
		robot.setDireccion(-10);
		Thread.sleep(7000);
		
	}

}
