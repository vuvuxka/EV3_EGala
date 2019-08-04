package equilibrio;

import main.Robot;

public abstract class Equilibrio implements Runnable{
	
	protected Robot robot;
	protected final float DEG2RAD = (float) Math.PI/180; // (0,017453 rad/deg)
	protected final int freq = 400;
	
	public Equilibrio(Robot robot2) 
	{
		robot = robot2;
	}
	
	public abstract double inicializar();

	public abstract void run();

}
