package control;

import main.Robot;
import main.Robot.Imagenes;

public class Simple implements Control {
	Robot robot;

	public Simple(Robot robot) {
		super();
		this.robot = robot;
	}

	public void ejecutar() throws InterruptedException {
		/*final GraphicsLCD lcd = robot.getPantalla();
        try {
            final Image image = Image.createImage(new BufferedInputStream(new FileInputStream("images/todo.jpg")));
            lcd.drawImage(image, 40, 10, 0);
            lcd.refresh();
        }catch (IOException e){
            robot.print("ERROR");
        }*/
		robot.setDireccion(0);
		robot.setVelocidad(10);
		robot.cara(Imagenes.Emocion.NEUTRO);
		Thread.sleep(1000);
		robot.cara(Imagenes.Emocion.ENFADADO);
		Thread.sleep(1000);
		robot.cara(Imagenes.Emocion.ERROR);
		Thread.sleep(10000);
		
	}

}
