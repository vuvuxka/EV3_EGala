package main;

import control.*;
import equilibrio.*;
import vista.Evitador;

public class Main {
	
	public static void main(String[] args) {
		try{
			Robot robot; 
			robot = new Robot("Robot Segway");
			Equilibrio eq = new PID(robot);
			//Equilibrio eq = new LQR(robot);
			Thread tEqu = new Thread(eq);
			Thread tDis = new Thread(new Evitador(robot));
			tEqu.start();
			try {Thread.sleep(2000);} catch(InterruptedException e) {e.printStackTrace();}
			tDis.start();
			
			Control c = new Simple(robot);
			c.ejecutar();
			try{
			tEqu.join();
			tDis.join();
			} catch(InterruptedException e) {e.printStackTrace();}
			robot.quit();
		} 
		catch(ExceptionInInitializerError e1) 
		{
			e1.printStackTrace();
			System.out.println("No se pudo inicializar el robot.\n"
					+ " Compruebe que las conexiones de los sensores son correctas. ");
			return;
		}
	
	}
}
