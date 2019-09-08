package vista;

import main.Robot;
import main.Robot.Motor;

public class Evitador implements Runnable{
	
	protected Robot robot;
	private final double DisCritica = 0.3; 
	//private final double DisOK = 0.4;
	
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
			if (dist < DisCritica) 
			{
				robot.sonido(100, 50);
				robot.sonido(100, 50);
				robot.setEvitando(true);
				try
				{
					if (historico % 6 < 3) robot.giro90(Motor.DERECHO);
					else robot.giro90(Motor.IZQUIERDO);
					robot.metros(20, 0.3);
					if (historico % 6 >= 3) robot.giro90(Motor.DERECHO);
					else robot.giro90(Motor.IZQUIERDO);
				} catch (InterruptedException e) {e.printStackTrace();}
				historico++;
				robot.setEvitando(false);
				robot.setDireccion(dir);
				robot.setVelocidad(vel);
			}
			/*else if(dist > DisCritica && dist < DisOK)
			{
				robot.sonido(100, 50);
				robot.setEvitando(true);
				try
				{
					if (historico % 6 < 3) robot.giro45(Motor.DERECHO);
					else robot.giro45(Motor.IZQUIERDO);
				} catch (InterruptedException e) {e.printStackTrace();}
				historico++;
				robot.setEvitando(false);
				robot.setDireccion(dir);
				robot.setVelocidad(vel);
			}*/
		}
		
	}

}
