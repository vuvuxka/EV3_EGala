package equilibrio;

import lejos.hardware.Sound;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import main.Robot;
import main.Robot.Motor;

public class LQR extends Equilibrio {

	final static int N_max = 4;

	private double historico[] = {0,0,0,0};
	private int cont = 0;
	private double vel = 0;
	private double pos = 0;
	private double pos_ref = 0;
	private double vel_ref = 0;
	private double rate = 0;
	private double angulo = 0;
	
	private static double out = 0;
	private boolean iniciandose = true;

	final double K_angulo = 21.6;
	final double K_rate = 1.1520;
	final double K_pos = 0.1728;
	final double K_vel = 0.1152;
	
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
			Sound.playTone(super.freq, 100, 30);
			Thread.sleep(100);
		    robot.reset();
		    for (int i = 0; i < 20; i++)
		    {
		        g += robot.rate(1);
		        Thread.sleep(5);
		    }
		    m = g/20;
		    Thread.sleep(100);
		    Sound.playTone(super.freq, 100, 30);
		    Thread.sleep(100);
		    Sound.playTone(super.freq, 100, 30);
		}
		catch(Exception e) {}
	    return m;
	}


	public void run() 
	{
		angulo = inicializar();
		System.out.println("ini = " + angulo);
		long ant_dt = System.nanoTime();
		double sum = 0, val = 0;
		Thread.currentThread().setPriority(10);
		
		while (!robot.isStop()) 
		{
			long ahora = System.nanoTime();
			double dt = (ahora - ant_dt) / 1000000000.0;	// Time step in seconds
			ant_dt = ahora;
			
			rate = robot.rate(1); // (deg)
			angulo = angulo + (rate * dt); // (deg/s)
			
		
			double antPos = sum;
			double motorDer = robot.encoder(Motor.DERECHO); // (rad)
			double motorIzq = robot.encoder(Motor.IZQUIERDO); // (rad)
			sum = motorDer + motorIzq;
			val = sum - antPos;
			pos = (pos + val);
			
			vel_ref = Math.max(-50, Math.min(50,robot.getVelocidad()));
			pos_ref = pos_ref + vel_ref*dt*0.002;
			historico[cont] = val;
			cont = (cont + 1) % N_max;
			vel = (historico[0] + historico[1] + historico[2] + historico[3])/(N_max*dt); // (rad/s)

			
			out = K_angulo*angulo + K_rate*rate + K_pos*(pos + pos_ref) + K_vel*vel;

			if (out > 100) out = 100;
			if (out < -100) out = -100;
			if (!iniciandose){
				robot.avance(Motor.DERECHO, (int) (out - robot.getDireccion()));
				robot.avance(Motor.IZQUIERDO, (int) (out + robot.getDireccion()));
			}

			Delay.msDelay(10);
			vueltas++;
			if (vueltas == 10) iniciandose = false;

		}

	}

}
