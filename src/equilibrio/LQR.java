package equilibrio;


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
	
	private static boolean nowError = false;
	private static boolean preError = false;
	private static double error_cont = 0;

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
			robot.sonido(super.freq, 100);
			Thread.sleep(100);
		    robot.reset();
		    for (int i = 0; i < 20; i++)
		    {
		        g += robot.angle();
		        Thread.sleep(5);
		    }
		    m = g/20;
		    Thread.sleep(100);
		    robot.sonido(super.freq, 100);
		    Thread.sleep(100);
		    robot.sonido(super.freq, 100);
		}
		catch(Exception e) {}
	    return m;
	}


	public void run() 
	{
		angulo = inicializar();
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
			double motorDer = robot.encoder(Motor.DERECHO); // (tacho counts)
			double motorIzq = robot.encoder(Motor.IZQUIERDO); // (tacho counts)
			sum = motorDer + motorIzq;
			val = sum - antPos;
			pos = (pos + val);
			
			vel_ref = Math.max(-50, Math.min(50,robot.getVelocidad()))*0.001;
			pos_ref = pos_ref + vel_ref*dt;
			historico[cont] = val;
			cont = (cont + 1) % N_max;
			vel = (historico[0] + historico[1] + historico[2] + historico[3])/(N_max*dt); // (tacho counts/s)

			
			out = K_angulo*angulo + K_rate*rate + K_pos*(pos + pos_ref) + K_vel*vel; // (% vatios)
			
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
			
			if (out > 100) out = 100;
			if (out < -100) out = -100;
			if (!iniciandose){
				robot.avance(Motor.DERECHO, (int) (out - robot.getDireccion()));
				robot.avance(Motor.IZQUIERDO, (int) (out + robot.getDireccion()));
			}

			try{Thread.sleep(10);} catch(InterruptedException e) {e.printStackTrace();}
			vueltas++;
			if (vueltas == 10) iniciandose = false;

		}

	}

}
