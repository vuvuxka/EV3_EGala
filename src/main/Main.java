package main;

import equilibrio.*;

public class Main {
	
	public static void main(String[] args) {
		Robot robot;
		try {
			//*
			robot = new Robot("Robot Segway");
			Equilibrio eq = new PID(robot);
			//*/
			//GyroBoy eq = new GyroBoy();
			Thread t1 = new Thread(eq);
			t1.start();
			
			//TODO comportamiento del bicho
			
			t1.join();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
