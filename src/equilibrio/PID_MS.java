package equilibrio;

import java.util.*;

import lejos.hardware.*;
import lejos.utility.Stopwatch;
import main.Robot;
import main.Robot.Motor;

public class PID_MS extends Equilibrio{

	final static int N_max = 7; // tamaño maximo del array de historico de posiciones
	final static double DEG2RAD = 0.0174533;
	protected final double DT = 0.02;
	Stopwatch reloj = new Stopwatch();
	
	private static Queue<Double> historico = new LinkedList<Double>(Collections.nCopies(N_max, 0.0));
	private static double vel = 0;
	private static double vel_ref = 0;
	private static double pos = 0;
	private static double pos_ref = 0;
	private static double media_angulo = 0;
	private static double rate = 0;
	private static double angulo = 0;
	
	private static double ganancia = 0;
	private static double error_p = 0;
	private static double error_i = 0;
	private static double error_d = 0;
	private static double error_0 = 0;
	private static double out = 0;
	
	private static boolean nowError = false;
	private static boolean preError = false;
	private static double error_cont = 0;
	
	private static int vueltas = 0;
	private static double avance_ant = 0;
	
	final static double Kp = 0.6;
	final static double Ki = 14;
	final static double Kd = 0.005;

	static double K_angulo = 25;
	static double K_rate = 1.3;
	static double K_pos = 350;
	static double K_vel = 75;
	
	
	public PID_MS(Robot robot2) {
		super(robot2);
	}
	
	public double inicializar()
	{
	    double g = 0;
	    double m = 0;
	    robot.reset();
		try {
			Sound.playTone(freq,100);
			Thread.sleep(100);
		    robot.reset();
		    for (int i = 0; i < 20; i++)
		    {
		        g += robot.rate(1);
		        Thread.sleep(5);
		    }
		    m = g/20;
		    Thread.sleep(100);
		    Sound.playTone(freq,100);
		    Thread.sleep(100);
		    Sound.playTone(freq,100);
		}
		catch(Exception e) {}
	    return m;
	}

	public void run()
	{
		media_angulo = inicializar();
		reloj.reset();
		while (!robot.isStop()) 
		{
			double r = robot.rate(5);
			media_angulo = media_angulo*(1-DT*0.2) + DT*0.2*r;
			rate = r - media_angulo; // (deg)
			angulo = angulo + rate*DT; // (deg/s)
			pos_ref = pos_ref + robot.getVelocidad()*DT*0.002;
			
			double valDer = (double)robot.encoder(Robot.Motor.DERECHO);
			double valIzq = (double)robot.encoder(Robot.Motor.IZQUIERDO);
			double val = (valDer + valIzq)/2;
			historico.add(val);
			double val_ant = historico.poll();
			double deg_vel = (val - val_ant)/(N_max*DT);
			vel = deg_vel*robot.getRADIO()*super.DEG2RAD; //(metros/s)
			pos = val*super.DEG2RAD*robot.getRADIO(); //(metros)
			
			ganancia = K_angulo*angulo + K_rate*rate + K_pos*(pos-pos_ref) + K_vel*vel;
			
			error_p = ganancia - 0;
			error_i = error_i + error_p*DT;
			error_d = (error_p - error_0)/DT;
			//error_0 = ganancia;  //TODO??
			
			out = error_p*Kp + error_d*Kd + error_i*Ki;
			
			// TODO GESTION DE ERRORES
			double avance = Math.min(50, Math.max(-50, robot.getAvance()));
			double sync_0 = 0;
			double extra_pwr = 0;
			/*if (avance == 0) //TODO
			{
				if (avance_ant != 0) { sync_0 = valDer - valIzq;}
				extra_pwr = (valDer - valIzq - sync_0)*(0.05); 
			}
			else {extra_pwr = (int)(robot.getAvance()/(0.05));}*/
			avance_ant = avance;
			
			double powerD = out + extra_pwr;
			double powerI = out - extra_pwr;
			
			//robot.print(powerD + " " + powerI);
			
		    robot.avance(Robot.Motor.DERECHO, (int) Math.max(-100, Math.min(powerD*0.021/robot.getRADIO(), 100)));
		    robot.avance(Robot.Motor.IZQUIERDO, (int) Math.max(-100, Math.min(powerI*0.021/robot.getRADIO(), 100)));
		    vueltas++;
		    
			while(reloj.elapsed() < DT*1000.0) {
				try {
					Thread.sleep (1);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
			reloj.reset();
		}
	}

}
