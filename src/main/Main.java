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
			
			//TODO comportamiento del bicho
			robot.setAvance(0);
			robot.setVelocidad(0);
			/*Thread.sleep(7000);
			
			Sound.playTone(440,500);
			robot.setAvance(20);
			
			Sound.playTone(440,500);
			robot.setAvance(-20);*/
			
			t1.join();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
