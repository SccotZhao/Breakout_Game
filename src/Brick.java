import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Brick{
	private static final String[] BRICKLIST = {"brick1.gif", "brick2.gif","brick3.gif", "brick4.gif", "brick5.gif","brick6.gif",
			"brick7.gif", "brick8.gif","brick9.gif"};
	private Rectangle rectangle;
	private int numberOfHit;
	private Random random;
	public Brick(double x, double y,double width, double height){
		rectangle = new Rectangle();
		rectangle.setX(x);
		rectangle.setY(y);
		rectangle.setHeight(height);
		rectangle.setWidth(width);
		random = new Random();
		
		Image img = new Image(BRICKLIST[random.nextInt(BRICKLIST.length)]);	
		ImagePattern pattern = new ImagePattern(img);
		
		
		rectangle.setFill(pattern);
		//if numberOfHit == 1, need one hit
		//if numberOfHit == 2, need two hits
		numberOfHit = random.nextInt(2) + 1;
	}
	public Brick(Rectangle r){
		rectangle = r;
		random = new Random();
		numberOfHit = random.nextInt(2) + 1;
	}
	
	public Brick(double x, double y){
		//a random brick from the list
		int n = random.nextInt(9);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BRICKLIST[n]));
		ImageView myBrick = new ImageView(image);
		myBrick.setX(x);
		myBrick.setY(y);
		
	}
	
	public Rectangle getRectangle(){
		return rectangle;
	}
	public int getHit(){
		return numberOfHit;
	}
	public void subHit(){
		numberOfHit--;
	}
}