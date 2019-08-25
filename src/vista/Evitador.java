package vista;

import lejos.hardware.Sound;
import main.Robot;

public class Evitador implements Runnable{
	
	protected Robot robot;
	private final double DisCritica = 0.2; 
	private final double DisOK = 0.4;
	
	private int SupergiroD = 20;
	private int SupergiroI = -20;
	private int GiroD = 10;
	private int GiroI = -10;
	private int Recto = 0;
	private double Rapido = 40;
	private double VelFront = 20;
	private double VelAtras = -10;
	private boolean ultDer = true;
	public int historico = 0;


	public Evitador(Robot robot) {
		super();
		this.robot = robot;
	}

	
	public void run() {
		while(!robot.isStop())
		{
			double dist = robot.distancia();
			double dir = robot.getDireccion();
			double vel = robot.getVelocidad();
			try{
				if (dist < DisCritica) 
				{
					 Sound.twoBeeps();
	
					 if(historico / 3 == 0) robot.setDireccion(dir + SupergiroD);
					 else robot.setDireccion(dir + SupergiroI);
					 historico = (historico+1)%6;
					 robot.setVelocidad(VelAtras);
					 
					 Thread.sleep(2000);
					 robot.setDireccion(dir);
					 robot.setVelocidad(vel);
				}
				else if(dist > DisCritica && dist < DisOK)
				{
					Sound.beep();
					if(historico / 3 == 0) robot.setDireccion(dir + GiroD);
					 else robot.setDireccion(dir + GiroI);
					 historico = (historico+1)%6;
					robot.setVelocidad(VelFront);
					
					 Thread.sleep(2000);
					 robot.setDireccion(dir);
					 robot.setVelocidad(vel);
					
				}
				else if (dist > DisOK) {
					/*robot.setDireccion(Recto);
					/*robot.setVelocidad(Rapido);*/
				}
			} catch(Exception e) {}
		}
		
	}

}
