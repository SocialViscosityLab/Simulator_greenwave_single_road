import java.awt.Color;

import processing.core.*;

@SuppressWarnings("serial")
public class Executable extends PApplet {

	World world;

	public void setup() {
		size(displayWidth, 400);
		world = new World(this);
	}

	public void draw() {
		background(100);
		world.show();
	}

	// *********** main method
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "Executable" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
	
	public void mousePressed(){
		world.mousePressed();
	}
}
