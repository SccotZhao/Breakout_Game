
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Projectile {
	private static final String[] PROJECTILELIST = {"pointspower.gif"};
	private Rectangle rectangle;
	private Random random;
	
	public Projectile(double x, double y, double width, double height){
		rectangle = new Rectangle();
		rectangle.setX(x);
		rectangle.setY(y);
		rectangle.setHeight(height);
		rectangle.setWidth(width);
		random = new Random();
		
		Image img = new Image(PROJECTILELIST[random.nextInt(PROJECTILELIST.length)]);	
		ImagePattern pattern = new ImagePattern(img);
		
		
		rectangle.setFill(pattern);

	}

	public void setY(double speed, double delay){
		rectangle.setY(rectangle.getY() - speed *delay);	
	}
	
	public Rectangle getRectangle(){
		return rectangle;
	}

}