package projekt;

import java.awt.Color;
import java.awt.Graphics2D;


public class elektron {
	public int xPos;
	public int yPos;
	public int width;
	public int height;
	public Color color;
	public int Vx;
	public int Vy;
	public imagePanel panel;
	int i = 0;
	public elektron(int xPos, int yPos, int Vx, int Vy, imagePanel panel) {
		this.panel=panel;
		this.xPos=xPos;
		this.yPos=yPos;
		this.Vx=Vx;
		this.Vy=Vy;
	}
	
	public void update() {
		i++;
		if(xPos<373) {
			xPos+=Vx+panel.V;
			yPos-=panel.U/100*i;
		}
		
	}
	public void draw(Graphics2D g2) {
		g2.setColor(Color.blue);
		g2.fillOval(xPos, yPos, 10, 10);
	}
}

