import java.awt.Color;
import java.util.ArrayList;

import processing.core.*;

/**
 * DEFINITIONS (Reference book: Vehicle Dynamics and Control, Rajamani) **
 * SPACING ERROR: the actual spacing from the preceding vehicle and the desired
 * inter-vehicle spacing. DESIRED INTER-VEHICLE SPACING: the desired distance
 * between the current vehicle and the one in front including the length of the
 * vehicle ahead.It could be chosen as a function of vehicle speed.
 * 
 * @author jsalam
 *
 */
public class Agent {
	private String id;
	private PVector pos;
	private float mySpeed; // the agent's current speed
	private float myAcceleration; // the agent's current acceleration
	private float distance; // the distance elapsed from the origin
	private float step; // the distance to move from current position
	// private float expectedAcc; // the expected acceleration
	private float targetSpeed; // the leader's target. The leader gets its
								// target speed from user input

	// Variables for visualization
	private int screens = 0;
	private Color color;
	private PImage signal; //@amitolottle Variable added to the Wi Fi's signal symbol
	private boolean endingRect=false; //@amitolottle Variable to control the behavior of the passing rectangle at the right side.
	private ArrayList<Ellipse> ellipses; //@amitolottle Variable to show the radar's waves
	private float cont=0; //@amitolottle Variable to manage the adding rate to the ellipses ArrayList
	

	// ** Variables for the CACC

	// acceleration;
	private float topSpeed = 3;
	private float lastAccelerationPlatoon;// Last acceleration calculated with
											// CACC
	public PApplet app;
	private boolean isLeader, greenWave;
	private Agent nearestFrontNode = null;
	private Agent leaderNode = null;

	// 2 . Platoon's parameters (CACC)
	private float alpha1 = 0.5f;
	private float alpha2 = 0.5f;
	private float alpha3 = 0.3f;
	private float alpha4 = 0.1f;
	private float alpha5 = 0.04f;
	private float alphaLag = 0.8f;
	private float length_vehicle_front = 2f;
	private float desiredSpacing = 55f;
	private float desirdIVSpacing = length_vehicle_front + desiredSpacing;
	private float designKSimple = 0.02f;// the lower the value, the slower the
	private float designKAdaptive = 0.1f;

	public Agent(PApplet app, float x, float y, String id) {
		this.app = app;
		pos = new PVector(x, y);
		distance = 0;
		mySpeed = 0;
		step = 0;
		// expectedAcc = 0;
		lastAccelerationPlatoon = 0;
		isLeader = true;
		nearestFrontNode = null;
		this.id = id;
		color = new Color(180, 180, 180);
		
		signal = app.loadImage("../assets/signal.png");
		signal.resize(0, 30);
		
		ellipses = new ArrayList<Ellipse>();
		
	}

	/**
	 * Gets the leader from the list of nodes
	 * 
	 * @param nodes
	 */
	public void getLeader(ArrayList<Agent> nodes) {
		for (int i = 0; i < nodes.size(); i++) {
			Agent temp = nodes.get(i);
			if (temp.isLeader)
				leaderNode = temp;
		}
	}

	/**
	 * Determine which node is in front
	 * 
	 * @param nodes
	 */
	public void getFrontVehicle(ArrayList<Agent> nodes) {
		float distanceToFront = nodes.get(0).pos.x - pos.x;
		// float distanceToFront = pos.dist(nodes.get(0).pos);
		for (int i = 0; i < nodes.size(); i++) {
			Agent temp = nodes.get(i);
			// If temp is not myself
			if (!temp.getAgentId().equals(id)) {
				// If temp is ahead
				if (temp.pos.x - pos.x > 0) {
					// if temp is closer
					if (temp.pos.x - pos.x <= distanceToFront) {
						// if (pos.dist(temp.pos) <= distanceToFront) {
						// distanceToFront = pos.dist(temp.pos);
						distanceToFront = temp.pos.x - pos.x;
						nearestFrontNode = temp;
						// System.out.println(id + " is behind of "+
						// nearestFrontNode.id);
					} else {
						// System.out.println(id + " is ahead of "+ temp.id);
					}
				}
			}
		}
	}

	/**
	 * Simple Cruise Control Algorithm. This control algorithm simply
	 * accelerates the node until it reaches the target speed. It lacks of any
	 * form of adaptation
	 * 
	 * @return
	 */
	public float simpleCC() {
		float acceleration = -designKSimple * (mySpeed - targetSpeed);
		return acceleration;
	}

	/**
	 * Adaptive Cruise Control. Not suitable for automobiles following between 5
	 * and 10 meters
	 * 
	 * This control algorithm takes into account a spacing error between
	 * vehicles and the timeHeadWay (average amount of time between vehicles)
	 * 
	 * See Chapter 6 reference book (Vehicle Dynamics and Control, Rajamani)
	 * 
	 * @return
	 */
	public float adaptiveCC() {
		float acceleration = 0;
		if (nearestFrontNode != null) {
			float speedRelativeFrontVehicle = -nearestFrontNode.getSpeed() + mySpeed;
			// float desiredGap = 10f;
			float spacingError = -distanceFrontToCurrent() + length_vehicle_front + desiredSpacing;
			float timeHeadway = 3f; // usually > 1s;
			acceleration = -1 / (timeHeadway * (speedRelativeFrontVehicle + (designKAdaptive * spacingError)));
		}
		return acceleration;
	}

	/**
	 * Collaborative Adaptive Cruise Control.
	 * 
	 * Based on
	 * "A Simulation Tool for Automated Platooning in Mixed Highway Scenarios"
	 * Segata et al. Proceedings of Mobicom 2012
	 * 
	 * @return
	 */
	public float collaborativeACC() {

		// a. Get information from the nearest node in the front
		float rel_speed_front;
		float spacing_error;
		float nodeFrontAcceleration;

		if (nearestFrontNode != null) {
			// Calculate relative speed to the node in front
			rel_speed_front = getSpeed() - nearestFrontNode.getSpeed();

			// Calculate spacing error
			spacing_error = -distanceFrontToCurrent() + length_vehicle_front + desiredSpacing;

			nodeFrontAcceleration = nearestFrontNode.getAcceleration();

		} else {
			rel_speed_front = 0;
			spacing_error = 0;
			nodeFrontAcceleration = 0;
		}

		// b. Calculate (Acceleration desired) A_des

		// float A_des = alpha1 * (nodeFrontAcceleration + alpha2)
		// * (leaderNode.getAcceleration() - alpha3)
		// * (rel_speed_front - alpha4)
		// * ((getSpeed() - leaderNode.getSpeed()) - alpha5)
		// * spacing_error;

		// WARNING: THIS IS NOT THE EQUATION AS DEFINED BY SEGATA ET AL, IT IS
		// AN ADAPTATION THAT WORKS BETTER IN THIS SIMULATOR. SEE SEGATA'S
		// EQUATION COMMENTED ABOVE
		float A_des = (alpha1 * nodeFrontAcceleration) + (alpha2 * leaderNode.getAcceleration())
				- (alpha3 * rel_speed_front) - (alpha4 * (getSpeed() - leaderNode.getSpeed()))
				- (alpha5 * spacing_error);

		// c. Calculate desired acceleration adding a delay
		float A_des_lag = (alphaLag * A_des) + ((1 - alphaLag) * lastAccelerationPlatoon);
		lastAccelerationPlatoon = A_des_lag;

		return A_des_lag;
	}

	/**
	 * Get data from all the the other agents and act based on data from the
	 * nearestFrontNode
	 * 
	 * @param nodes
	 */
	public void move(PApplet app, ArrayList<Agent> nodes) {
		getLeader(nodes);
		getFrontVehicle(nodes);
		// if leader
		float time = 1f;
		// If leader accelerate with simpleCruiseControl
		if (isLeader) {
			myAcceleration = simpleCC();
			targetSpeed = PApplet.map(app.mouseX, 0, app.width, 0.0f, topSpeed);
			// x = Vi*t + (at2)/2, where time(t) is = 1
			step = (mySpeed * time) + (((myAcceleration * (float) Math.pow(time, 2f))) / 2);
			mySpeed = step;
		} else {
			if (nearestFrontNode.pos.x - pos.x > desirdIVSpacing) {
				// if (pos.x.dist(nearestFrontNode.pos.x) > desirdIVSpacing) {
				// myAcceleration = adaptiveCC();
				// System.out.println(pos.dist(nearestFrontNode.pos));
				myAcceleration = collaborativeACC();
			} else {
				// reverse acceleration
				// myAcceleration = PApplet.map(pos.dist(nearestFrontNode.pos),
				// 0, desirdIVSpacing, -0.01f, -0.035f);
				myAcceleration = PApplet.map(nearestFrontNode.pos.x - pos.x, 0, desirdIVSpacing, -0.01f, -0.035f);
			}
			// x = Vi*t + (at2)/2, where time(t) is = 1
			step = (mySpeed * time) + (((myAcceleration * (float) Math.pow(time, 2f))) / 2);
			mySpeed = step;
		}

		// change position
		pos.x += step;
		distance += step;
	}

	/**
	 * This method presents the animation in the applet
	 * 
	 * @param nodes
	 */
	public void show(PApplet app, ArrayList<Agent> nodes, int caso) {
		screens = PApplet.floor(pos.x / app.width);
		move(app, nodes);
		int mvntVal=(app.width * screens); //@amitolottle new variable to shorten the usage of the function
		String txt="";
		
		/**
		 * @amitolottle This part of the code was modified to fullfill the requeriments
		 * of this simulation. In the next lines, a switch method is used to divide the
		 * way that the platoon is showed and the way that the leader is showed when
		 * a Green Wave is activated. Having this in mind, the "case 0" is for the 
		 * platoon's visualization when the Green Wave is deactivated and the "case 1"
		 * refers to the visualization of the Green Wave.
		 */
		
		
		switch(caso){
		case 0:
			app.noStroke();
			app.fill(color.getRGB());
			app.stroke(color.getRGB());
			app.strokeWeight(1);
			
			//Drawing of the bike's body
			app.noFill();
			app.quad((pos.x-25) - mvntVal, pos.y, (pos.x-13) - mvntVal ,(pos.y-1),(pos.x-5) - mvntVal ,(pos.y-10),(pos.x-18) - mvntVal ,(pos.y-10));
			app.line((pos.x-13) - mvntVal ,(pos.y-1),(pos.x-19) - mvntVal ,(pos.y-14));
			app.line((pos.x-21) - mvntVal ,(pos.y-14),(pos.x-17) - mvntVal ,(pos.y-14));
			app.line((pos.x-2) - mvntVal ,(pos.y),(pos.x-7) - mvntVal,(pos.y-17));
			app.line((pos.x-7) - mvntVal,(pos.y-17),(pos.x-9) - mvntVal,(pos.y-17));
			
			//Drawing of the bike's Wheels
			app.strokeWeight(2);
			app.ellipse((pos.x-2) - mvntVal, pos.y, 10, 10);
			app.ellipse((pos.x-25) - mvntVal, pos.y, 10, 10);
			 txt = "F";
			 
			 //Visualization of a marker for the leader
			 if (isLeader) {
				 txt = "L";
				 if (greenWave){
				 app.ellipse((pos.x-13) - mvntVal, pos.y-2, 40, 40);
				 }
			 }
			break;
			
		case 1:
			
			/**
			 * The following lines creates a loop using the cont Variable to define the
			 * adding ratio of a new Ellipse to the ellipses ArrayList. Then those ellipses
			 * are showed and animated and at the end the ellipses that have their
			 * opacity below the visible value, are removed from the list.
			 */
			
			if (isLeader) {
				if (greenWave){
					txt=" ";
				cont+=1;
				   if(cont>=100){
					   ellipses.add(new Ellipse(app,pos.x - mvntVal,app.height/2));
					   cont=0;
				   }
				   
				    for(int i=0;i<ellipses.size();i++){
					   ellipses.get(i).show();
					   ellipses.get(i).setPosX(pos.x - mvntVal);
					   ellipses.get(i).setPosY(app.height/2);
				   }
				   
				   for(int i=ellipses.size()-1; i>=0;i--){
					   if(ellipses.get(i).getOpacity()<=20){
						   ellipses.remove(i);
					   }
				   }
				   
				   /**
				    * The following lines creates a rectangle that shows the limits defined
				    * for the area of effect from the Green Wave's signal.
				    */
					app.noStroke();
					app.fill(new Color (0,255,0,20).getRGB());
					app.rect(pos.x - mvntVal, 79, -600, 244);
					app.fill(75,255,157);
					app.noStroke();
					app.tint(75,255,157);
					app.imageMode(app.CENTER);
					app.image(signal,(pos.x) - mvntVal, app.height/2);
					
					/**
					 * This lines evaluate if the first bike of the group that refers to the leader
					 * passed trough the applications right side. If that is true, a rectangle
					 * with the width of the previous rect is created and then this new one is moved
					 * to generate the illusion of continuosness in the screen.
					 */
					if(pos.x-mvntVal>=app.width){
						endingRect=true;
					}
					
					if(endingRect){
						float tempWidth=0;
						app.fill(new Color (0,255,0,20).getRGB());
						tempWidth+=(pos.x-mvntVal)+(app.width-600);
						app.rect(tempWidth, 79, 600, 244);
					}
				}
			}
		}
		

		app.stroke(new Color(255,255,255,100).getRGB());
		String spd = PApplet.nf(mySpeed, 1, 1);
		String acc = PApplet.nf(myAcceleration, 1, 3);
		String dis = PApplet.nf(distance, 1, 0);
		//txt = txt + " " + id + "\n s:" + spd + "\n a:" + acc + "\n d:" + dis;
		app.textSize(10);
		app.text(txt+ " " + id, pos.x - mvntVal + 7, pos.y + 5);
	}
	
	public void activateGreenWave(){
		greenWave = !greenWave;
	}

	public float distanceFrontToCurrent() {
		// return pos.dist(nearestFrontNode.pos);
		return nearestFrontNode.pos.x - pos.x;
	}

	public int getScreens() {
		return screens;
	}

	public void setScreens(int screens) {
		this.screens = screens;
	}

	public float getSpeed() {
		return mySpeed;
	}

	public void setSpeed(float speed) {
		this.mySpeed = speed;
	}

	public float getAcceleration() {
		return myAcceleration;
	}

	public void setAcceleration(float acceleration) {
		this.myAcceleration = acceleration;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public boolean isLeader() {
		return isLeader;
	}

	public void setLeader(boolean leader) {
		this.isLeader = leader;
	}

	public void setTargetSpeed(float val) {
		targetSpeed = val;
	}

	public float getTargetSpeed() {
		return targetSpeed;
	}

	public String getAgentId() {
		return id;
	}

	public float getDesignKSimple() {
		return designKSimple;
	}

	public void setDesignKSimple(float designKSimple) {
		this.designKSimple = designKSimple;
	}

	public float getDesignKAdaptive() {
		return designKAdaptive;
	}

	public void setDesignKAdaptive(float designKAdaptive) {
		this.designKAdaptive = designKAdaptive;
	}

	public float getTopSpeed() {
		return topSpeed;
	}

	public void setTopSpeed(float topSpeed) {
		this.topSpeed = topSpeed;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public PVector getPos() {
		return pos;
	}

	public void setDesiredSpace(int val) {
		desiredSpacing = val;
	}
	
	public float getCont() {
		return cont;
	}

	public void setCont(float cont) {
		this.cont = cont;
	}
	
	/**
	 * The following method evaluates a distance between the leader and the platoon
	 * to paint the bikes if they are inside the defined range.
	 * @param leader
	 * @param range
	 * @param color
	 */
	
	public void inGreenWave(Agent leader, int range, Color color){
		if (pos.x > leader.pos.x - range){
			setColor(color);
		}else{
			setColor(new Color(255,255,255,100));
		}
	}

	public boolean isGreenWave() {
		return greenWave;
	}

	public void setGreenWave(boolean greenWave) {
		this.greenWave = greenWave;
	}

}
