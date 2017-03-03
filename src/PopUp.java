// This entire file is part of my masterpiece.
// Zhiyong Zhao

import javafx.scene.Scene;


import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PopUp{
	public static final String[] CHEATKEYS = {"The score and time on the top left corner",
											"the KeyCode.LEFT: move the first paddle to the LEFT side",
											"the KeyCode.RIGHT: move the first paddle to the RIGHT side" ,
											"the KeyCode.UP: move the first paddle to the UP side" ,
											"the KeyCode.BOTTOM: move the first paddle to the BOTTOM side",
											"the KeyCode.L: move the first paddle to the L side",
											"the KeyCode.R: move the first paddle to the R side",
											"the KeyCode.U: move the first paddle to the U side" ,
											"the KeyCode.B: move the first paddle to the B side",
											"The KeyCode P: to pause the game" ,
											"The KeyCode.ESCAPE: to stop the game" ,
											"The KeyCode A: to restart the game from the pause" , 
											"The KeyCode N: to start a new game of the same difficult level but the configuration of the game may be different" ,
											"The KeyCode.M: to start a game of medium level" ,
											"The KeyCode.H: to start a game of  hard level" ,
											"The KeyCode.Z: to start a game of  hardest level",
											"When the mouse is pressed and the coordinate is contained in a brick, then the brick will change size",
											"When paddle one hits the powerup, the score increases by 100",
											"When paddle two hits the powerup, the horizontal scale of paddle two increases by 0.1",
											"When the wo paddles hit each other, the scales of both paddles decrease by 0.1"};
	private Scene scene;
	private Text[] splash;
	public PopUp(int width, int height, Paint background){
		Pane root = new Pane();
		String url ="scenery5.jpg" ;
		Image img = new Image(url);	
		ImagePattern pattern = new ImagePattern(img);
		
		// create a place to see the shapes
		scene = new Scene(root, width, height);
		
		scene.setFill(pattern);

		
		splash = new Text[CHEATKEYS.length + 2];
		splash[0] = new Text(100,30, "Welcome to The Breakout Game");
		splash[0].setFont(Font.font(25));
		splash[0].setFill(Color.DARKVIOLET);
		
		splash[1] = new Text(200,60, "zhiyong.zhao@duke.edu");
		splash[1].setFont(Font.font(15));
		splash[1].setFill(Color.DARKVIOLET);

		//use dropshadow to make some good effects on the text
		DropShadow drop = new DropShadow();
		drop.setOffsetX(10);
		drop.setOffsetY(10);
		drop.setRadius(10);
		splash[0].setEffect(drop);
		//splash[1].setEffect(drop);
		
		int i = 2;
		// add the description of the breakout game
		for(String str : CHEATKEYS){
		splash[i++] = new Text(10,30 *i, "           " + str);
		}
		
		root.getChildren().addAll(splash);
	}
	public Scene getScene(){
		return scene;
	}
}
