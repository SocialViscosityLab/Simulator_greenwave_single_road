import java.awt.Color;

import processing.core.*;

public class World {
	Platoon plattonA;
	Platoon plattonB;
	Agent leaderA;
	Agent leaderB;
	Road road;

	PApplet app;
//	int lenght, height;
	
	
	boolean hasPassed=false;

	public World(PApplet app) {
		this.app = app;
//		lenght = app.width;
//		height = app.height;
		plattonA = new Platoon("A");
		plattonB = new Platoon("B");

		// Creates the Leader
		leaderA = new Agent(app,app.width-100, 120, "A0"); // app,x,y,id
		leaderB = new Agent(app,app.width-10, 200, "B0"); // app,x,y,id
		// DesignK
		leaderA.setDesignKSimple(0.2f);
		leaderB.setDesignKSimple(0.2f);
		// Top speed
		leaderA.setTopSpeed(4);
		leaderB.setTopSpeed(3);
		// leader.start();
		leaderA.setTargetSpeed(3f);
		leaderB.setTargetSpeed(3f);
		// add leader to followers group
		plattonA.addLeader(leaderA);
		plattonB.addLeader(leaderB);
		// Crates platoon
		plattonA.populate(app, 15, 5, app.width-110, 0);
		plattonB.populate(app, 15, 7, app.width-110, 200);
		//plattonB.setColor(new Color(125,0,0));
		
		road= new Road(app);

	}

	public void show() {
		plattonA.show(app);
		plattonB.show(app);
		road.show();
		app.noStroke();
		app.fill(255,100);
		app.rect(0, 5, app.mouseX, 10);
		float targetSpeed = PApplet.map(app.mouseX, 0, app.width, 0.0f, 3f);
		app.text("Target Speed: " + PApplet.nf(targetSpeed, 1, 1), app.mouseX + 5, 14);
		app.strokeWeight(0);
		if (leaderA.isGreenWave()){
			
			//This line serves to paints the bikes blue. And so on show just the Platoon division
			//plattonA.inGreenWave(new Color(59,157,255)); 
			
           //This line paints the bikes green and so on show the Green Wave's Behavior
		    plattonA.inGreenWave(new Color(75,255,157));
		    
		    /**
		     * This line defines the case when the green wave is activated or not.
		     * If you want to show just the bikes division and paint them blue, this
		     * parameter must be 0. If you want to show the Green Wave's Behavior, this
		     * value should be 1.
		     */
			plattonA.setCaso(1);
		}else{
			plattonA.setColor(new Color(255,255,255,100));
			plattonA.setCaso(0);
		}
	}
	
	/**
	 * The methods inside mousePressed() refers to the activation of the Green Wave's simulation
	 * if you want to activate the Green Wave, all the methods should be uncommented. 
	 * If you want just show the bikes divisions, the setSpace Method and the declaration of the cont
	 * value should be commented.
	 */
	public void mousePressed(){
		plattonA.setSpace(50);
		leaderA.setCont(95);
		leaderA.activateGreenWave();
	}
}
