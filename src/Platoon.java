import java.awt.Color;
import java.util.ArrayList;

import processing.core.*;

public class Platoon {

	private ArrayList<Agent> group;
	private String id;
	private Color color;
	private int caso=0;

	public int getCaso() {
		return caso;
	}

	public void setCaso(int caso) {
		this.caso = caso;
	}

	public Platoon(String id) {
		this.id = id;
		group = new ArrayList<Agent>();
	}

	public void populate(PApplet app, int quant, int space, int x, int y) {
		for (int i = 1; i < quant; i++) {
			Agent follower = new Agent(app,(i * -space) + x, (int) app.random(100, app.height-100), (id + i)); // x,y,id
			//Agent follower = new Agent((i * -space) + x, (i * space) + y, (id + i)); // x,y,id
			// follower.start();
			follower.setLeader(false);
			group.add(follower);
		}
	}

	public void addLeader(Agent leader) {
		group.add(leader);
	}

	public void show(PApplet app) {
		for (Agent a : group) {
			if(!a.isLeader()){
			a.show(app, group,0);
			}else{
				a.show(app, group, caso);
			}
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		for (Agent a: group) {
			a.setColor(color);
		}
	}
	
	public void setSpace(int space){
		for (Agent a:group){
			a.setDesiredSpace(space);
		}
	}
	public void inGreenWave(Color color){
		for (Agent a:group){
			a.inGreenWave(group.get(0), 600, color);
		}
	}
	
}
