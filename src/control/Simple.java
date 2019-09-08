package control;

import main.Robot;

public class Simple implements Control {
	Robot robot;

	public Simple(Robot robot) {
		super();
		this.robot = robot;
	}

	public void ejecutar() {
		robot.setVelocidad(20);
		try {
			while(!robot.isStop()) 
			{
				
					if (colorNoNegro() && !robot.isEvitando())
					{
						robot.giro90(Robot.Motor.DERECHO);
					}
			}
		}
		catch(InterruptedException e)
		{
			Thread.currentThread().interrupt();
	        System.err.println("Ejecución interrumpida");
	        robot.setVelocidad(0);
		}

	}

	private boolean colorNoNegro() throws InterruptedException {
		boolean negro = robot.color() == Robot.Colores.NEGRO;
		Thread.sleep(100);
		return !(negro && robot.color() == Robot.Colores.NEGRO) ;
	}
}
