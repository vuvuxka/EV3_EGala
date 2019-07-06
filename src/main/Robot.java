package main;


import lejos.hardware.*;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.hardware.sensor.*;
import lejos.robotics.EncoderMotor;
import lejos.robotics.SampleProvider;
import lejos.hardware.lcd.Font;

// Clase del Robot

public class Robot {
	
	private String name;
	private double pos_relativa;
	private double velocidad;
	private double aceleracion;
	private double avance;
	private double direccion;
	private boolean stop;
	private int contador;
	
	final double DIAMETRO = 42; // (milimetros)
	private final double RADIO = DIAMETRO/(2*1000); // (metros)
	
	/* PUERTOS ASIGNADOS Y SENSORES */
	private final static Port PRESION = SensorPort.S1;
	private final static Port GIROSCOPIO = SensorPort.S2;
	private final static Port COLOR = SensorPort.S3;
	private final static Port INFRARROJOS = SensorPort.S4;
	private final static Port MOTOR_DERECHO = MotorPort.A;
	private final static Port MOTOR_IZQUIERDO = MotorPort.D;
	private final static int MAX_SPEED = 900;
	
	private final EV3GyroSensor gyroSensor = new EV3GyroSensor(GIROSCOPIO);
	private final SampleProvider gyroReader = gyroSensor.getRateMode();
	private final EV3TouchSensor presionSensor = new EV3TouchSensor(PRESION);
	//private static EV3GyroSensor colorSensor = new EV3GyroSensor(COLOR);
	private final EV3IRSensor infraSensor = new EV3IRSensor(INFRARROJOS);
	GraphicsLCD pantalla = BrickFinder.getDefault().getGraphicsLCD(); 
	
	private static EncoderMotor mDer = new UnregulatedMotor(MOTOR_DERECHO);
	private static EncoderMotor mIzq = new UnregulatedMotor(MOTOR_IZQUIERDO);
	public enum Motor {DERECHO, IZQUIERDO}

	public Robot(String name) throws InterruptedException {
		this.name = name;
		this.gyroSensor.reset();
		this.setPos_relativa(0);
		this.setVelocidad(0);
		this.contador = 0;
		this.pantalla.clear();
		this.stop = false;
		mDer.resetTachoCount();
		mIzq.resetTachoCount();
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

	public double rate(int vueltas)
	{
		double media = 0;
		float[] sample = {0};
		for(int i = 0; i < vueltas; i++)
		{
			this.gyroReader.fetchSample(sample, 0);
			media = media + sample[0];
			try{ Thread.sleep(2);} catch(Exception e) {}
		}
		return - media/vueltas; //TODO Con negativo?
	}
	
	public double angle()
	{
		float[] sample = {0};
		this.gyroSensor.getAngleMode().fetchSample(sample, 0);
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
	
	public boolean presionado()
	{
		SensorMode presion = presionSensor.getTouchMode();
		float sample[] = {0};
		presion.fetchSample(sample, 0);
		return (sample[0] == 1);
	}
	
	public int distancia()
	{
		SampleProvider IRreader = infraSensor.getDistanceMode();
		float [] sample = new float[IRreader.sampleSize()];
		IRreader.fetchSample(sample, 0);
	    return (int) sample[0];
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
		return stop || presionado();
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
	
}
