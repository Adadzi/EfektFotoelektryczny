package projekt;

import java.awt.Color;
import java.awt.Graphics2D;


public class foton {

	public int xPos;
	public int yPos;
    public int width;
    public int height;
    public Color color;
    public int Vx;
    public int Vy;
    public imagePanel panel;
    public foton(int xPos, int yPos, int Vx, int Vy, imagePanel panel) {
    	this.panel=panel;
    	this.xPos=xPos;
    	this.yPos=yPos;
    	this.Vx=Vx;
    	this.Vy=Vy;
    }
	
	public void update() {
		if(xPos>144) {
			xPos-=Vx;
			yPos+=Vy;
		}
	}
	public void draw(Graphics2D g2) {
		g2.setColor(panel.kolorfotonow);
		g2.fillOval(xPos, yPos, 10, 10);
	}
}

