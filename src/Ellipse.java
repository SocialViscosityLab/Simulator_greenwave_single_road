import java.awt.Color;
import java.util.ArrayList;

import processing.core.*;

/**
 * The function of this class is manage the radar's behavior. To achieve this,
 * the radar works creating an animation with code that controls the scale and
 * the opacity of ellipses that come from a position that is passed as parameters
 * in the constructor.
 * @author Amitolottle
 *
 */


public class Ellipse {
private float posX=0;
private float posY=0;
private float scale=0;
private float opacity=120;
 PApplet app;
 
 public Ellipse(PApplet app,float posX, float posY){
	 this.app=app;
	 this.posX=posX;
	 this.posY=posY;
 }
 
 /**
  * The method show, creates a loop using a condition that decrease the opacity of
  * the ellipse whenever this value is higher or equals to zero.
  */
 
 public void show(){
	scale+=1;
	if(opacity>=0){
	opacity-=1;
	}
	 app.stroke(0,255,0,opacity);
		app.strokeWeight(4);
		app.noFill();
	 app.ellipse(posX, posY, 10*scale, 10*scale);
 }

public float getPosX() {
	return posX;
}

public void setPosX(float posX) {
	this.posX = posX;
}

public float getPosY() {
	return posY;
}

public void setPosY(float posY) {
	this.posY = posY;
}

public float getOpacity() {
	return opacity;
}

public void setOpacity(float opacity) {
	this.opacity = opacity;
}


}
