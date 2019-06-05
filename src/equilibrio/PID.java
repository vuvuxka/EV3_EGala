package equilibrio;

import java.util.*;

import lejos.hardware.*;
import lejos.utility.Stopwatch;
import main.Robot;
import main.Robot.Motor;

public class PID extends Equilibrio{

	final static int N_max = 7; // tamaño maximo del array de historico de posiciones
	protected final double DT = 0.01;
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
	
	final static double Kp = 0.0336;
	final static double Kd = 0.000504;
	final static double Ki = 0.2688;

	final static double K_angulo = 25;
	final static double K_rate = 1.3;
	final static double K_pos = 700;
	final static double K_vel = 150;
	
	
	public PID(Robot robot2) {
		super(robot2);
	}
	
	public double inicializar()
	{
	    double g = 0;
	    double m = 0;
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
		while (true) 
		{
			double r = robot.rate(5);
			rate = r - media_angulo; // (deg)
			media_angulo = media_angulo*0.999 + (0.001*(rate + media_angulo));
			angulo = angulo + rate*DT; // (deg/s)
			
			if(vel_ref < robot.getVelocidad()*10){
				vel_ref = vel_ref + robot.getAceleracion()*10*DT;
			} else if(vel_ref > robot.getVelocidad()*10){
				vel_ref = vel_ref - robot.getAceleracion()*10*DT;
			}
			pos_ref = pos_ref + vel_ref*DT;
			
			double valDer = (double)robot.encoder(Robot.Motor.DERECHO);
			double valIzq = (double)robot.encoder(Robot.Motor.IZQUIERDO);
			double val = valDer + valIzq + (int) pos_ref;
			historico.add(val);
			pos = val*super.DEG2RAD*robot.getRADIO();
			double val_ant = historico.poll();
			double deg_vel = (val - val_ant)/((N_max-1)*DT)*robot.getRADIO()*super.DEG2RAD;
			vel = deg_vel*robot.getRADIO()*super.DEG2RAD;
			
			ganancia = K_angulo*angulo + K_rate*rate + K_pos*pos + K_vel*vel;
			
			error_p = ganancia;
			error_i = error_i + error_p*DT;
			error_d = (error_p - error_0)/DT;
			error_0 = ganancia; 
			
			out = (error_p*Kp + error_d*Kd + error_i*Ki)/robot.getRADIO();
			
			// TODO GESTION DE ERRORES
			
			double sync_0 = 0;
			double extra_pwr = 0;
			if (robot.getAvance() == 0)
			{
				if (avance_ant != 0) { sync_0 = valDer - valIzq;}
				extra_pwr = (valDer - valIzq - sync_0)*(robot.getRADIO()*10); 
			}
			else {extra_pwr = (int)(robot.getAvance()/(robot.getRADIO()*10));}
			avance_ant = robot.getAvance();
			
			double power = out + extra_pwr;
			
		    robot.avance(Robot.Motor.DERECHO, (int) power);
		    robot.avance(Robot.Motor.IZQUIERDO, (int) power);
		    vueltas++;
		    
			while(reloj.elapsed() < DT*1000) {
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
