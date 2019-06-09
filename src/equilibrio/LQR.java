package equilibrio;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import lejos.hardware.Sound;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import main.Robot;
import main.Robot.Motor;

public class LQR extends Equilibrio {

	final static int N_max = 4; // tamaño maximo del array de historico de posiciones

	private double historico[] = {0,0,0,0};
	private int cont = 0;
	private double vel = 0;
	private double pos = 0;
	private double pos_ref = 0;
	private double rate = 0;
	private double angulo = 0;
	
	private static double out = 0;
	private boolean iniciandose = true;

	final double K_angulo = 15;
	final double K_rate = 0.8;
	final double K_pos = 0.12;///(robot.getRADIO()*super.DEG2RAD);
	final double K_vel = 0.08;///(robot.getRADIO()*super.DEG2RAD);
	/*final double K_angulo = 34.81;
	final double K_rate = 3.11;
	final double K_pos = 0.31;
	final double K_vel = 1.35;*/
	
	Stopwatch reloj = new Stopwatch();
	int vueltas = 0;
	
	public LQR(Robot robot2)
	{
		super(robot2);
	}


	public double inicializar() 
{
	    double g = 0;
	    double m = 0;
		try {
			Sound.playTone(super.freq,100);
			Thread.sleep(100);
		    robot.reset();
		    for (int i = 0; i < 20; i++)
		    {
		        g += robot.rate(1);
		        Thread.sleep(5);
		    }
		    m = g/20;
		    Thread.sleep(100);
		    Sound.playTone(super.freq,100);
		    Thread.sleep(100);
		    Sound.playTone(super.freq,100);
		}
		catch(Exception e) {}
	    return m;
	}


	public void run() 
	{
		angulo = inicializar();
		System.out.println("ini = " + angulo);
		long ant_dt = System.nanoTime();
		//Delay.msDelay(10);
		double sum = 0, val = 0;
		Thread.currentThread().setPriority(10);
		
		while (!robot.isStop()) 
		{
			long ahora = System.nanoTime();
			double dt = (ahora - ant_dt) / 1000000000.0;	// Time step in seconds
			ant_dt = ahora;
			
			rate = robot.rate(1);
			angulo = angulo + (rate * dt);
			
		
			double antPos = sum;
			double motorDer = robot.encoder(Motor.DERECHO);
			double motorIzq = robot.encoder(Motor.IZQUIERDO);
			sum = motorDer + motorIzq;
			val = sum - antPos;
			pos = (pos + val);//*robot.getRADIO()*super.DEG2RAD;
			
			historico[cont] = val;
			cont = (cont + 1) % N_max;
			vel = (historico[0] + historico[1] + historico[2] + historico[3])/(N_max*dt);
			pos -= robot.getVelocidad();
			
			out = K_angulo*angulo + K_rate*rate + K_pos*pos + K_vel*vel;
			//robot.print(dt + "");

			if (out > 100) out = 100;
			if (out < -100) out = -100;
			if (!iniciandose){
				robot.avance(Motor.DERECHO, (int) (out - robot.getDireccion()));
				robot.avance(Motor.IZQUIERDO, (int) (out + robot.getDireccion()));
				//robot.print("avance = " + out);
			}

			Delay.msDelay(10);
			vueltas++;
			if (vueltas == 10) iniciandose = false;

		}

	}

}
