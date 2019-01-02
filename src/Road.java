import java.awt.Color;
import java.util.ArrayList;

import processing.core.*;

/**
 * This class defines the road to show in the World class. The function of this class
 * is mere visualization for the dashed green lines, the outside part of the bike's road
 * and a symbol to define that road.
 * @author Amitolottle
 *
 */
public class Road {
 PApplet app;
 private int xConv=0;
 private PImage symbol;
 private int screens=0;
 private int posXRects=0;
 
 public Road(PApplet app){
	 this.app=app;
	 symbol = app.loadImage("../assets/symbol.png");
		symbol.resize(0, 100);
 }
 
 public void show(){
	 xConv++;
	
	 app.strokeWeight(4);	
		for(int i=0;i<=140;i++){
			int xTempIni = i*40-xConv;
			int xTempEnd = (i*40+20)-xConv;
			app.stroke(75,255,157,100);
		   app.line(xTempIni, 80, xTempEnd, 80);
		   app.line(xTempIni, app.height-80, xTempEnd, app.height-80);
		   }
		
		for(int i=0;i<=10;i++){
			int xTempImage = i*400-xConv;
			app.imageMode(app.CENTER);
			app.tint(255,20);
			app.image(symbol,xTempImage+1200,app.height/2);
		}
		app.noStroke();
		app.fill(new Color(200,200,200,80).getRGB());
		app.rect(posXRects, 0, app.width, 79);
		app.rect(posXRects, app.height-77, app.width, 80);
		
 }
}
