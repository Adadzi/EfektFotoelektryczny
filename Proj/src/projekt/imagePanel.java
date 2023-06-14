package projekt;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.*;
import javax.swing.*;

public class imagePanel extends JPanel implements Runnable{
	private static final long serialVersionUID = 1L;
	public BufferedImage image;
	static ArrayList<foton> fotony = new ArrayList<foton>();
	ArrayList<elektron> elektrony = new ArrayList<elektron>();
	Graphics2D g2d;
	Color kolorfotonow;
	Thread watek;
	int licznikRysowanie = 0;
	int FPS = 60;
	boolean wlaczony=false;
	int licznik = 0;
	int powtorzenie=0;
	int roznica=0;
	double V, U;
	public boolean EfektZachodzi = false;
	public imagePanel() {
		 super();
		 this.setBackground(Color.white);
		 File resource = new File("resource/fotokomorka.jpg");
		 try {
			 image = ImageIO.read(resource);
		 } 
		 catch (IOException e) {
			 System.err.println("Blad odczytu obrazka");
			 e.printStackTrace();
		 }
		 Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
		 setPreferredSize(dimension);
		 StartSimulation();	 
	 }
	public void StartSimulation() {
		watek = new Thread(this);
		watek.start();
	}
	public void Start() {
		wlaczony=!wlaczony;
	}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g2d = (Graphics2D) g;
			int szerokosc = mainFrame.panelLewy.getWidth();
			int wysokosc = mainFrame.panelLewy.getHeight();
			g2d.drawImage(image, (int)(szerokosc/17) , (int)(wysokosc/4.4) , this);

				for(int i=0; i<fotony.size();i++) {
					if(fotony.get(i)!=null&&wlaczony==true) {
						fotony.get(i).draw(g2d);
					}
				}
				for(int j=0; j<elektrony.size();j++) {
					if(elektrony.get(j)!=null&&wlaczony==true) {
						elektrony.get(j).draw(g2d);
					}
				}	
		 }

		@Override
		public void run(){
			double drawInterval = 1000000000/FPS;
			double delta = 0;
			long lastTime = System.nanoTime();
			long currentTime;
			long timer=0;
			
				while(watek != null) {
						currentTime = System.nanoTime();
						delta+=(currentTime-lastTime)/drawInterval;
						timer+=(currentTime-lastTime);
						lastTime = currentTime;
						if(delta>=1) {
							update();
							repaint();
							delta--;
							licznikRysowanie++;
						}
						if(timer>=1000000000) {
							licznikRysowanie=0;
							timer = 0;
						}
					}
		}
	public void update() {
		if(wlaczony==true) {
		licznik++;
		if(licznik==120) {
				if(fotony.size()<10) {
					fotony.add(new foton(268,107,1,1,this));
					fotony.add(new foton(250,113,1,1,this));
					fotony.add(new foton(233,115,1,1,this));
				}
				
				licznik=0;
		}
		
		for(int i=0; i<fotony.size();i++) {
			if(fotony.get(i)!=null) {
				fotony.get(i).update();
				if(fotony.get(i).xPos==144) {
					powtorzenie++;
					fotony.remove(i);
					if(EfektZachodzi==true) {
					elektrony.add(new elektron(144,196+roznica,1,1,this));
					}
					roznica+=18;
				}
			}
		}
		for(int i=0; i<elektrony.size(); i++) {
			if(elektrony.get(i)!=null) {
				elektrony.get(i).update();
				if(elektrony.get(i).xPos>=373 || elektrony.get(i).yPos>=250 || elektrony.get(i).yPos<=155) {
					elektrony.remove(i);
				}
			}
			
		}
		if(powtorzenie==3) {
			powtorzenie=0;
			roznica=0;
		}
		}
		if(wlaczony==false) {
			powtorzenie=0;
			roznica=0;
			licznik=0;
			elektrony.clear();
			fotony.clear();
		}
	}
}