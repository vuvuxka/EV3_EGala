package equilibrio;

import java.util.*;

import lejos.utility.Stopwatch;
import main.Robot;
import main.Robot.Motor;

public class PID extends Equilibrio{

	final static int N_max = 7; // tamaño maximo del array de historico de posiciones
	final static double DEG2RAD = 0.0174533;
	protected final double DT = 0.02;
	Stopwatch reloj = new Stopwatch();
	
	private static Queue<Double> historico = new LinkedList<Double>(Collections.nCopies(N_max, 0.0));
	private static double vel = 0;
	private static double vel_ref = 0;
	private static double pos = 0;
	private static double pos_ant = 0;
	private static double pos_ref = 0;
	private static double media_rate = 0;
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
	
	private static double dir_ant = 0;
	private static double correcion = 0;
	
	final static double Kp = 0.4;
	final static double Ki = 14;
	final static double Kd = 0.005;

	static double K_angulo = 25;
	static double K_rate = 1.3;
	static double K_pos = 350;
	static double K_vel = 75;
	
	
	public PID(Robot robot2) {
		super(robot2);
	}
	
	public void run()
	{
		try {Thread.sleep (100);} catch (InterruptedException e1) {e1.printStackTrace();}
		media_rate = robot.rate(20);
		try {Thread.sleep (100);} catch (InterruptedException e1) {e1.printStackTrace();}
	    robot.sonido(super.freq, 100);
	    try {Thread.sleep (100);} catch (InterruptedException e1) {e1.printStackTrace();}
	    robot.sonido(super.freq, 100);
		reloj.reset();
		while (!robot.isStop()) 
		{
			double r = robot.rate(5);
			media_rate = media_rate*(1-DT*0.2) + DT*0.2*r;
			rate = r - media_rate; // (deg)
			angulo = angulo + rate*DT; // (deg/s)
			vel_ref = Math.max(-50, Math.min(50,robot.getVelocidad()))*0.002;
			pos_ref = pos_ref + vel_ref*DT;
			
			double valDer = (double)robot.encoder(Robot.Motor.DERECHO);
			double valIzq = (double)robot.encoder(Robot.Motor.IZQUIERDO);
			double val = (valDer + valIzq)/2;
			historico.add(val);
			double val_ant = historico.poll();
			double deg_vel = (val - val_ant)/(N_max*DT);
			vel = deg_vel*robot.getRADIO()*super.DEG2RAD; //(metros/s)
			pos = val*super.DEG2RAD*robot.getRADIO(); //(metros)
			pos = pos + (pos_ant-pos)*0.0002;
			pos_ant = pos;
			
			ganancia = K_angulo*angulo + K_rate*rate + K_pos*(pos+pos_ref) + K_vel*vel;
			
			error_p = ganancia;
			error_i = error_i + error_p*DT;
			error_d = (error_p - error_0)/DT;
			error_0 = ganancia;
			
			out = error_p*Kp + error_d*Kd + error_i*Ki;
			
			nowError = (Math.abs(out) > 100);
			if (nowError & preError) error_cont++;
			else error_cont = 0;
			
			if (error_cont > 20)
			{
				robot.stop(Motor.DERECHO);
				robot.stop(Motor.IZQUIERDO);
				robot.sonido(800, 100);
				robot.sonido(600, 100);
				robot.sonido(300, 100);
		        robot.setStop(true);
		        break;
			}
			else  preError = nowError;

			double extra = 0;
			double direccion = Math.max(-50, Math.min(50, robot.getDireccion())); // Limite 50?
			if (direccion == 0)
			{
				if (dir_ant != 0) correcion = valIzq - valDer;
				extra = (valIzq - valDer - correcion);
			}
			else extra = direccion*-0.5;

			dir_ant = direccion;
			double powerD = out + extra;
			double powerI = out - extra;
			
			
		    robot.avance(Robot.Motor.DERECHO, (int) Math.max(-100, Math.min(powerD*0.021/robot.getRADIO(), 100)));
		    robot.avance(Robot.Motor.IZQUIERDO, (int) Math.max(-100, Math.min(powerI*0.021/robot.getRADIO(), 100)));
		    
		    while(reloj.elapsed() < DT*1000.0) 
		    {
				try {Thread.sleep (1);} catch (InterruptedException e1) {e1.printStackTrace();}
			}
			
			reloj.reset();
		}
	}

}
