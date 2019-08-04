package main;


import lejos.hardware.*;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.hardware.sensor.*;
import lejos.robotics.EncoderMotor;
import lejos.robotics.SampleProvider;

// Clase del Robot

public class Robot {
	
	private String name;
	private double pos_relativa;
	private double velocidad;
	private double aceleracion;
	private double avance;
	private double direccion;
	private boolean stop;
	
	final double DIAMETRO = 42; // (milimetros)
	private final double RADIO = DIAMETRO/(2*1000); // (0.021 metros)
	
	/* PUERTOS ASIGNADOS Y SENSORES */
	private final static Port PRESION = SensorPort.S1;
	private final static Port GIROSCOPIO = SensorPort.S2;
	private final static Port COLOR = SensorPort.S3;
	private final static Port ULTRASONIDO = SensorPort.S4;
	private final static Port MOTOR_DERECHO = MotorPort.A;
	private final static Port MOTOR_IZQUIERDO = MotorPort.D;
	
	private final EV3GyroSensor gyroSensor = new EV3GyroSensor(GIROSCOPIO);
	private final SampleProvider gyroRate = gyroSensor.getRateMode();
	private final SampleProvider gyroAng = gyroSensor.getAngleMode();
	private final EV3TouchSensor presionSensor = new EV3TouchSensor(PRESION);
	private final GraphicsLCD pantalla = BrickFinder.getDefault().getGraphicsLCD(); 
	//private static EV3GyroSensor colorSensor = new EV3GyroSensor(COLOR);
	private final EV3UltrasonicSensor ultraSensor = new EV3UltrasonicSensor(ULTRASONIDO);
	private final SampleProvider USreader = ultraSensor.getDistanceMode();
	
	
	private static EncoderMotor mDer = new UnregulatedMotor(MOTOR_DERECHO);
	private static EncoderMotor mIzq = new UnregulatedMotor(MOTOR_IZQUIERDO);
	public enum Motor {DERECHO, IZQUIERDO}

	public Robot(String name) throws InterruptedException {
		this.name = name;
		this.gyroSensor.reset();
		this.setPos_relativa(0);
		this.setVelocidad(0);
		this.setAvance(0);
		this.pantalla.clear();
		this.stop = false;
		mDer.resetTachoCount();
		mIzq.resetTachoCount();
		Button.ESCAPE.addKeyListener(new KeyListener() {
			public void keyPressed(Key k) {
				stop = true;				
			}
			public void keyReleased(Key k) {
				
			}
		      });
	}

	public String toString() 
	{
		return name;
	}

	public int encoder(Motor m)
	{
		switch (m)
		{
			case DERECHO: return mDer.getTachoCount();
			case IZQUIERDO: return mIzq.getTachoCount();
			default: return 0;
		}
		
	}

	public double rate(int vueltas) // Grados
	{
		double media = 0;
		float[] sample = {0};
		for(int i = 0; i < vueltas; i++)
		{
			this.gyroRate.fetchSample(sample, 0);
			media = media + sample[0];
			try{Thread.sleep(2);} catch(Exception e) {}
		}
		return - media/vueltas;
	}
	
	public double angle()
	{
		float[] sample = {0};
		gyroAng.fetchSample(sample, 0);
		return sample[0];
	}

	public void stop(Motor m)
	{
		switch (m)
		{
			case DERECHO: mDer.flt(); 		break;
			case IZQUIERDO: mIzq.flt(); 	break;
			default:  						break;
		}
	}

	public void avance(Motor m, int vel)
	{
		switch (m)
		{
			case DERECHO: mDer.setPower(vel); 	break;
			case IZQUIERDO: mIzq.setPower(vel); break;
			default: 							break;
		}
		
	}
	
	public double distancia()
	{
		float [] sample = new float[USreader.sampleSize()];
		USreader.fetchSample(sample, 0);
	    return sample[0];
	}
	
	public void print(String s)
	{
		//this.pantalla.setFont(Font.getSmallFont());
		System.out.println(s);
	}
	
	public void reset() 
	{
		mDer.resetTachoCount();
		mIzq.resetTachoCount();
		gyroSensor.reset();
	}

	public double getVelocidad()
	{
		return velocidad;
	}

	public void setVelocidad(double velocidad)
	{
		this.velocidad = velocidad;
	}


	public double getRADIO()
	{
		return RADIO;
	}

	public double getDireccion()
	{
		return direccion;
	}

	public void setDireccion(double direccion)
	{
		this.direccion = direccion;
	}

	public double getAvance() {
		return avance;
	}

	public void setAvance(double avance) {
		this.avance = avance;
	}

	public double getAceleracion() {
		return aceleracion;
	}

	public void setAceleracion(double aceleracion) {
		this.aceleracion = aceleracion;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public double getPos_relativa() {
		return pos_relativa;
	}

	public void setPos_relativa(double pos_relativa) {
		this.pos_relativa = pos_relativa;
	}

	public void quit() {
		gyroSensor.close();
		presionSensor.close();
		ultraSensor.close();
	}
	
}
