// This entire file is part of my masterpiece.
// Zhiyong Zhao


import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class PowerUp {
	private static final String[] POWERUPLIST = {"money1.jpg"};
	private Rectangle rectangle;
	private Random random;
	
	public PowerUp(double x, double y, double width, double height){
		rectangle = new Rectangle();
		rectangle.setX(x);
		rectangle.setY(y);
		rectangle.setHeight(height);
		rectangle.setWidth(width);
		random = new Random();
		
		Image img = new Image(POWERUPLIST[random.nextInt(POWERUPLIST.length)]);	
		ImagePattern pattern = new ImagePattern(img);
		
		
		rectangle.setFill(pattern);

	}

	public void setY(double speed, double delay){
		rectangle.setY(rectangle.getY() + speed *delay);	
	}
	
	public Rectangle getRectangle(){
		return rectangle;
	}
	public Rectangle getFromBrick(List<PowerUp> list, Brick brick){
		for(PowerUp p : list){
			if(p.getRectangle().getBoundsInParent().intersects(brick.getRectangle().getBoundsInParent())){
				return p.getRectangle();
			}
		}
		return null;
		
	}

}
