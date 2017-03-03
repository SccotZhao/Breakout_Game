import java.net.URISyntaxException;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**

 * 
 * @author Zhiyong Zhao
 */
public class Breakout extends Application {
	public static final String TITLE = "Zhiyong's Breakout Game";
	public static final String BALL_IMAGE = "ball.gif";
	public static final String PADDLE_IMAGE = "paddle.gif";
	public static final String MUSIC = "music.mp3";
	public static final String MUSIC1 = "Canon.mp3";
	public static final String[] SCENERY = {"scenery1.jpg","scenery2.jpg","scenery3.jpg","scenery4.jpg","scenery4.gif",
			"scenery5.jpg","scenery6.jpg","scenery7.jpg","scenery8.jpg","scenery9.jpg","scenery10.jpg","scenery11.jpg"};
	public static final int SIZE = 630;
	public static final Paint BACKGROUND = Color.LAWNGREEN;
	public static final int FRAMES_PER_SECOND = 10;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 5;
	public static final double GROWTH_RATE = 1.1;
	public static final int BOUNCER_SPEED = 30;
	public static final int WIDTH = 70;
	public static final int HEIGHT = 20;
	public static final double COMPARE = 0.7;

	// some things we need to remember during our game
	private Scene myScene;
	private ImageView myBouncer;
	// the first paddle
	private ImageView myPaddle;
	//the second paddle
	private ImageView myPaddleNew;
	private List<Brick> brickList;
	private List<PowerUp> powerUpList;
	private List<PowerUp> powerUpListShow;
	private double xBefore;
	private double yBefore;
	private Text scoreRecord;
	private Text timeRecord;
	private int score;
	private Timeline animation;
	private double time;
	private AudioClip mediaPlayer;
	//scale of the paddles after the two paddle hit
	private double scale;



	/**
	 * Initialize what will be displayed and how it will be updated.
	 */
	@Override
	public void start (Stage s) throws Exception {
		// attach scene to the stage and display it
		PopUp p = new PopUp(SIZE, SIZE, BACKGROUND);
		Scene scene = p.getScene();
		s.setScene(scene);
		s.setTitle(TITLE);
		s.show();
		
		//play the music when start each level of the game
		Media sound = new Media(getClass().getResource(MUSIC).toURI().toString());
		mediaPlayer = new AudioClip(sound.getSource());
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	    mediaPlayer.play();
	    
	   

	    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    	@Override
	        public void handle(KeyEvent ke) {
	    		if(ke.getCode() == KeyCode.CONTROL){
	    			try {
	    				s.close();
						setupGameAfterSplash(s);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
	    		}
	    		controlGameThroughKey(s, ke);	    		
	        }
	    });    
	}

	public void setupGameAfterSplash(Stage s) throws URISyntaxException {
		Scene scene = setupGame(SIZE, SIZE);
	    		
	    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    	@Override
	        public void handle(KeyEvent ke) {
	    		controlGameThroughKey(s, ke);	    		
	        }
	    });
	    
		s.setScene(scene);
		s.setTitle(TITLE);
		s.show();
		// attach "game loop" to TimeLine to play it
		setAnimation();
	}
	
	//control of the difficulty of the game through input from the keyboard
	public void controlGameThroughKey(Stage s, KeyEvent ke) {
		//The keyCode P means pause
        if(ke.getCode() == KeyCode.P){
        	System.out.println("Key Presses: " + ke.getCode());
        	animation.pause();
        }
        //The keyCode escape means stop
        if (ke.getCode() == KeyCode.ESCAPE) {
            System.out.println("Key Pressed: " + ke.getCode());
            animation.stop();	
        }
        //the keyCode A means play again
        if(ke.getCode() == KeyCode.A){
        	System.out.println("Key Presses: " + ke.getCode());
        	animation.play();
        }
        //the keyCode N means a new game
        if(ke.getCode() == KeyCode.N){
        	System.out.println("Key Presses: " + ke.getCode());
        	animation.stop();	
        	animation.getKeyFrames().clear();
        	restart(s);
        }
        //the keyCode M means a medium level game
        if(ke.getCode() == KeyCode.M){
        	System.out.println("Key Presses: " + ke.getCode());
        	animation.stop();
        	animation.getKeyFrames().clear();
        	restart(s);
        	KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
    				e -> step(2 * SECOND_DELAY));
        	animation.getKeyFrames().clear();
    		animation.getKeyFrames().add(frame);
        	animation.play();
        }
        
        // the keyCode H means a hard game
        if(ke.getCode() == KeyCode.H){
        	System.out.println("Key Presses: " + ke.getCode());
        	animation.stop();
        	animation.getKeyFrames().clear();
        	restart(s);
        	KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
    				e -> step(3 * SECOND_DELAY));
        	animation.getKeyFrames().clear();
    		animation.getKeyFrames().add(frame);
        	animation.play();
        }
        // the keyCode Z means the hardest game
        if(ke.getCode() == KeyCode.Z){
        	System.out.println("Key Presses: " + ke.getCode());
        	animation.stop();
        	
        	restart(s);
        	KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
    				e -> step(4 * SECOND_DELAY));
        	animation.getKeyFrames().clear();
    		animation.getKeyFrames().add(frame);
        	animation.play();
        }

        //control the location of the paddles
        handleKeyInput (ke);
	}

	private void setAnimation() {
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
				e -> step(SECOND_DELAY));
		
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
	
	private void restart(Stage s) {
		Scene popUp = setupGame(SIZE, SIZE);
		s.setScene(popUp);
		s.setTitle(TITLE);
		s.show();
		// attach "game loop" to TimeLine to play it
		try {
			mediaPlayer.stop();
			start(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Create the game's "scene": what shapes will be in the game and their starting properties
	private Scene setupGame (int width, int height) {
		// create one top level collection to organize the things in the scene
		Pane root = new Pane();
		// create a place to see the shapes
		myScene = new Scene(root, width, height);
		
		//generate a random background
		Random random = new Random();
		Image img = new Image(SCENERY[random.nextInt(SCENERY.length)]);	
		ImagePattern pattern = new ImagePattern(img);
		myScene.setFill(pattern);

		// make some shapes and set their properties

		//setup the location of the paddle
		Image paddleImage = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
		myPaddle = new ImageView(paddleImage);
		myPaddle.setX(width/2 -myPaddle.getBoundsInLocal().getWidth()-2);
		myPaddle.setY(height- myPaddle.getBoundsInLocal().getHeight());
		
		myPaddleNew = new ImageView(paddleImage);
		myPaddleNew.setX(width/2 +2);
		myPaddleNew.setY(height-myPaddleNew.getBoundsInLocal().getHeight());

		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		myBouncer = new ImageView(image);
		// x and y represent the top left corner, so center it
		myBouncer.setX(width / 2 - myBouncer.getBoundsInLocal().getWidth() / 2);
		myBouncer.setY(height - myPaddle.getBoundsInLocal().getHeight() 
				- myBouncer.getBoundsInLocal().getHeight()
				-1);
		xBefore = myBouncer.getX() - BOUNCER_SPEED * SECOND_DELAY;
		yBefore = myBouncer.getY() + BOUNCER_SPEED * SECOND_DELAY + 1;
		
		powerUpListShow = new ArrayList<>();

		setupBrickAndPowerup(random);
		
		score = 0;
		scoreRecord = new Text(10, 20, "Score: " + score);
		scoreRecord.setFont(Font.font("Serif",20));
		
		time = 0;
		timeRecord = new Text(10,40, "Time: " + time );
		timeRecord.setFont(Font.font("Serif",20));

		scale = 1.0;

		// order added to the group is the order in which they are drawn
		root.getChildren().add(myBouncer);
		
		//add powerUpList first
		for(PowerUp p : powerUpList){
			root.getChildren().add(p.getRectangle());
		}
		
		//add brickList on the top of the powerUpList
		for(Brick brick : brickList){
			root.getChildren().add(brick.getRectangle());
		}
		
		root.getChildren().add(myPaddle);
		root.getChildren().add(myPaddleNew);
		root.getChildren().add(scoreRecord);
		root.getChildren().add(timeRecord);

		// respond to input
		myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return myScene;
	}

	public void setupBrickAndPowerup(Random random) {
		//add bricks
		brickList = new ArrayList<>();
		
		//add power ups to some bricks
		powerUpList = new ArrayList<>();
		
		//add randomly selected bricks
		for(int i =0, j =2; brickList.size() < 100; i = random.nextInt(9), j = random.nextInt(20) + 2 ){
			Brick brick = new Brick(i*WIDTH,j*HEIGHT,WIDTH,HEIGHT);
			brickList.add(brick);
			double compare = random.nextDouble();
			//if the random number is greater than the constant COMPARE, then we add a power
			if(compare > COMPARE){
				PowerUp p = new PowerUp(i * WIDTH + WIDTH/2 ,j * HEIGHT, 15,15);
				powerUpList.add(p);
			}
			
		}
	}

	// Change properties of shapes to animate them 
	// Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
	private void step (double elapsedTime) {

		boolean hitBrick = hitBrick();
		time += elapsedTime;
		timeRecord.setText("Time: " + (int)time);

		for(PowerUp p : powerUpListShow){
			p.setY(BOUNCER_SPEED, SECOND_DELAY);
		}
		
		nonHit(hitBrick);

		//update the ball after hitting left or right boundary
		hitRightOrLeft();


		//update the ball after hitting bottom or top boundary
		hitBottomOrTop();


		//update the ball after hitting the paddles
		ballCollidePaddle();
		//update the paddles collide with each other
		paddleCollideEachOther();
		//update the score and the powerUpListShow after one powerUp hit one of the paddles
		powerUpCollidePaddle();
	}

	private void powerUpCollidePaddle() {
		for(PowerUp p : powerUpListShow){
			if(p.getRectangle().getBoundsInParent().intersects(myPaddle.getBoundsInParent())){
				//if the powerUpCollide myPaddle then update the score
				score += 100;
				scoreRecord.setText("Score: " + score);
				p.getRectangle().setOpacity(0);
			}
			
			//if the powerUpCollide myPaddleNew then update the scale of the myPaddleNew
			if(p.getRectangle().getBoundsInParent().intersects(myPaddleNew.getBoundsInParent())){
				p.getRectangle().setOpacity(0);
				
				scale += 0.001;
				myPaddleNew.setFitWidth(myPaddleNew.getBoundsInLocal().getWidth() * scale);

			}
		}
		
		
		
	}

	private void paddleCollideEachOther() {
		if(paddlesCollide(myPaddle, myPaddleNew)){
			score -= 10;
			scoreRecord.setText("Score: " + score);
			
			//set the scale of the paddle after the two paddle hit each other
			myPaddle.setScaleX(scale);
			myPaddleNew.setScaleX(scale);
			//each time the paddles hit, the scale will decrease by 0.001
			scale -= 0.001;
			scale = Math.abs(scale);
			myPaddle.setFitWidth(myPaddle.getBoundsInLocal().getWidth() * scale);
			myPaddleNew.setFitWidth(myPaddleNew.getBoundsInLocal().getWidth() * scale);
			
		}
	}

	private void ballCollidePaddle() {
		if(isCollidePaddle(myBouncer, myPaddle) || isCollidePaddle(myBouncer, myPaddleNew)){
			update();
			yBefore = 2 * myBouncer.getY() - yBefore;
		}
	}

	private void hitBottomOrTop() {
		if(isCollideBottom(myBouncer) || isCollideTop(myBouncer)){
			if(isCollideBottom(myBouncer)){
				score -= 500;
				scoreRecord.setText("Score: " + score);
			}
			update();
			yBefore = 2 * myBouncer.getY() - yBefore;
		}
	}

	private void hitRightOrLeft() {
		if(isCollideRight(myBouncer) || isCollideLeft(myBouncer)){
			update();
			xBefore = 2 * myBouncer.getX() - xBefore;
		}
	}

	private void nonHit(boolean hitBrick) {
		if(!isCollideRight(myBouncer) && !isCollideLeft(myBouncer) && !isCollideBottom(myBouncer) && !isCollideTop(myBouncer)  && ! hitBrick ){
			update();

		}
	}

	private boolean hitBrick() {
		boolean hitBrick = false;
		//update the ball after hitting the bricks
		for(Brick brick: brickList ){
			if(isCollideBrick(myBouncer, brick.getRectangle())){
				hitBrick = true;
				if(isRightBrick(myBouncer,brick.getRectangle()) || isLeftBrick(myBouncer, brick.getRectangle())){
					update();
					yBefore = 2 * myBouncer.getY() - yBefore;

					if(brick.getHit() == 2){
						brick.subHit();
						LinearGradient linearGradient = colorChange();
						brick.getRectangle().setFill(linearGradient);
					}else{
						updateAfterOneHit(brick);
						powerUpShow(brick);
					}
				}else{
					update();
					xBefore = 2 * myBouncer.getX() - xBefore;
					if(brick.getHit() == 2){
						brick.subHit();
						LinearGradient linearGradient = colorChange();
						brick.getRectangle().setFill(linearGradient);
					}else{
						updateAfterOneHit(brick);
						powerUpShow(brick);
					}
				}

				break;
			}

		}
		return hitBrick;
	}

	public void powerUpShow(Brick brick) {

		
		for(PowerUp p : powerUpList){
			if(p.getRectangle().getBoundsInParent().intersects(brick.getRectangle().getBoundsInParent())){
				powerUpListShow.add(p);
			}
		}
	}

	private boolean paddlesCollide(ImageView p1, ImageView p2) {
		Rectangle rec1 = new Rectangle(p1.getX(), p1.getY(), p1.getBoundsInLocal().getWidth(), p1.getBoundsInLocal().getHeight());
		Rectangle rec2 = new Rectangle(p2.getX(), p2.getY(), p2.getBoundsInLocal().getWidth(), p2.getBoundsInLocal().getHeight());
		return rec1.getBoundsInParent().intersects(rec2.getBoundsInParent());
	}

	private void updateAfterOneHit(Brick brick) {
		brick.getRectangle().setOpacity(0);
		brickList.remove(brick);
		score += 100;
		scoreRecord.setText("Score: " + score);
	}

	private LinearGradient colorChange() {
		Stop[] stops = new Stop[] { 
				new Stop(0, Color.BLUE), 
				new Stop(1, Color.ORANGERED),
				new Stop(1, Color.YELLOW)};
		LinearGradient linearGradient = new LinearGradient(
				0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		return linearGradient;
	}

	private boolean isLeftBrick(ImageView myBouncer2, Rectangle brick) {
		return 2 * myBouncer2.getX() - xBefore >= brick.getX() - myBouncer2.getBoundsInLocal().getWidth();
	}

	private boolean isRightBrick(ImageView myBouncer2, Rectangle brick) {
		return 2 * myBouncer2.getX() - xBefore <= brick.getX() +brick.getWidth();
	}

	public boolean isCollideBrick(ImageView myBouncer2, Rectangle brick) {
		//the x coordinate of the top of the ball
		double x = 2 * myBouncer2.getX() - xBefore;
		//the y coordinate of the top of the ball
		double y = 2 * myBouncer2.getY() - yBefore;
		//check collision		
		return inside(x,y,brick);
	}

	private boolean inside(double x1, double x2, Rectangle brick) {
		Rectangle ballRectangle = new Rectangle(x1,x2,myBouncer.getBoundsInLocal().getWidth(),myBouncer.getBoundsInLocal().getHeight());

		return (ballRectangle.getBoundsInParent().intersects(brick.getBoundsInParent()));
	}



	private boolean isCollidePaddle(ImageView myBouncer2, ImageView myPaddle2) {
		//the x coordinate of the top of the ball
		double x = 2 * myBouncer2.getX() - xBefore ;
		//the y coordinate of the top of the ball
		double y = 2 * myBouncer2.getY() - yBefore;
		Rectangle ballRectangle = new Rectangle(x,y,myBouncer2.getBoundsInLocal().getWidth(), myBouncer2.getBoundsInLocal().getHeight());
		
		Rectangle paddleRectangle = new Rectangle(myPaddle2.getX(), myPaddle2.getY(), myPaddle2.getBoundsInLocal().getWidth(),
				myPaddle2.getBoundsInLocal().getHeight());
		return ballRectangle.getBoundsInLocal().intersects(paddleRectangle.getBoundsInLocal());
	}

	public void update() {
		double temp1 = myBouncer.getX();
		double temp2 = myBouncer.getY();
		myBouncer.setX(2* temp1 - xBefore);
		myBouncer.setY(2 * temp2 - yBefore);
		xBefore = temp1;
		yBefore = temp2;
	}

	//Check collide right by using two consecutive x values
	public boolean isCollideRight(ImageView myBouncer){
		return 2 * myBouncer.getX() - xBefore >= SIZE - myBouncer.getBoundsInLocal().getWidth();
	}
	//Check collide left by using two consecutive x values
	public boolean isCollideLeft(ImageView myBouncer){
		return 2 * myBouncer.getX() - xBefore <= 0;
	}
	//Check collide Bottom by using two consecutive y values
	public boolean isCollideBottom(ImageView myBouncer){
		return 2 * myBouncer.getY() -yBefore >= SIZE - myBouncer.getBoundsInLocal().getHeight();
		
	}
	//Check collide top by using two consecutive y values
	public boolean isCollideTop(ImageView myBouncer){
		return 2 * myBouncer.getY() - yBefore <= 0;
	}



	// What to do each time a key is pressed
	private void handleKeyInput (KeyEvent e) {
		//key control of the first paddle
		if (e.getCode() == KeyCode.RIGHT) {
			myPaddle.setX(myPaddle.getX() + KEY_INPUT_SPEED);
		}
		else if (e.getCode()== KeyCode.LEFT) {
			myPaddle.setX(myPaddle.getX() - KEY_INPUT_SPEED);
		}
		else if (e.getCode() == KeyCode.UP) {
			myPaddle.setY(myPaddle.getY() - KEY_INPUT_SPEED);
		}
		else if (e.getCode() == KeyCode.DOWN) {
			myPaddle.setY(myPaddle.getY() + KEY_INPUT_SPEED);
		}
		//key control of the second Paddle
		if (e.getCode() == KeyCode.L) {
			myPaddleNew.setX(myPaddleNew.getX() - KEY_INPUT_SPEED);
		}
		else if (e.getCode() == KeyCode.R) {
			myPaddleNew.setX(myPaddleNew.getX() + KEY_INPUT_SPEED);
		}
		else if (e.getCode() == KeyCode.U) {
			myPaddleNew.setY(myPaddleNew.getY() - KEY_INPUT_SPEED);
		}
		else if (e.getCode() == KeyCode.B) {
			myPaddleNew.setY(myPaddleNew.getY() + KEY_INPUT_SPEED);
		}
	}
	
	
	
	// When the mouse is pressed and the coordinate is contained in
	//a brick, then the brick will change size
	private void handleMouseInput (double x, double y) {

		for(Brick brick: brickList){
			if (brick.getRectangle().contains(x, y)) {
				brick.getRectangle().setScaleX(brick.getRectangle().getScaleX() * GROWTH_RATE);
				brick.getRectangle().setScaleY(brick.getRectangle().getScaleY() * GROWTH_RATE);
			}
		}
	}

	/**
	 * Start the program.
	 */
	public static void main (String[] args) {

	    		
		launch(args);
	}
}