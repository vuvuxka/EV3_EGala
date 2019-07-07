package main;

import equilibrio.*;
import lejos.hardware.Sound;
import vista.Ultrasonido;

public class Main {
	
	public static void main(String[] args) {
		Robot robot;
		try {
			//*
			robot = new Robot("Robot Segway");
			Equilibrio eq = new PID(robot);
			//*/
			//GyroBoy eq = new GyroBoy();
			Thread tEqu = new Thread(eq);
			Thread tDis = new Thread(new Ultrasonido(robot));
			tEqu.start();
			Thread.sleep(2000);
			tDis.start();
			
			robot.setPos_relativa(0);
			robot.setDireccion(0);
			Thread.sleep(7000);
			
			Sound.playTone(440,800,30);
			robot.setVelocidad(50); //velocidad Maxima
			for(int i = 0; i < 5; i++) {robot.setDireccion(i*10); Thread.sleep(500); }
			
			tEqu.join();
			tDis.join();
			robot.quit();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
