package main;

import equilibrio.*;
import lejos.hardware.Sound;

public class Main {
	
	public static void main(String[] args) {
		Robot robot;
		try {
			//*
			robot = new Robot("Robot Segway");
			Equilibrio eq = new PID_MS(robot);
			//*/
			//GyroBoy eq = new GyroBoy();
			Thread t1 = new Thread(eq);
			t1.start();
			
			robot.setPos_relativa(0);
			robot.setDireccion(0);
			Thread.sleep(7000);
			
			Sound.playTone(440,800);
			robot.setVelocidad(60); //velocidad Maxima
			robot.setDireccion(0);
			
			t1.join();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
