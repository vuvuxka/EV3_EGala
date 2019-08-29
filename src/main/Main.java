package main;

import control.*;
import equilibrio.*;
import vista.Evitador;

public class Main {
	
	public static void main(String[] args) {
		Robot robot;
		try {
			//*
			robot = new Robot("Robot Segway");
			Equilibrio eq = new PID(robot);
			//Equilibrio eq = new LQR(robot);
			Thread tEqu = new Thread(eq);
			Thread tDis = new Thread(new Evitador(robot));
			tEqu.start();
			Thread.sleep(2000);
			tDis.start();
			
			Control c = new Simple(robot);
			c.ejecutar();
			tEqu.join();
			tDis.join();
			robot.quit();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}
