package vista;

import lejos.hardware.Sound;
import main.Robot;

public class Infrarrojo implements Runnable{
	
	protected Robot robot;

	public Infrarrojo(Robot robot) {
		super();
		this.robot = robot;
	}

	public void run() {
		while(!robot.presionado())
		{
			int distancia = robot.distancia();
			Sound.playTone(440,300);
			try {
				Thread.sleep(distancia*10);
			} catch (InterruptedException e) {}
		}
		
	}
}
