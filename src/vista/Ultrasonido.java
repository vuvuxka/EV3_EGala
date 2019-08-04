package vista;

import lejos.hardware.Sound;
import main.Robot;

public class Ultrasonido implements Runnable{
	
	protected Robot robot;

	public Ultrasonido(Robot robot) {
		super();
		this.robot = robot;
	}

	public void run() {
		while(!robot.isStop())
		{
			double distancia = robot.distancia();
			if (distancia < 0.5) Sound.playTone(250,300,5);
			try {
				Thread.sleep((int) (distancia*distancia*1000));
			} catch (InterruptedException e) {}
		}
		
	}
}
