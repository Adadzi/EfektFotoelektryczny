package projekt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;

import javax.swing.event.*;


public class mainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	JPanel panelPrawy, panelLewyDolny, panelPomocniczy, panelMetal;
	static JPanel panelLewy;
	String[] metaleWybor={ "Wybierz metal", "Potas (K)", "Srebro (Ag)", "Miedz (Cu)", "Ołów (Pb)", "Bar (Ba)", "Lit (Li)" };
	JComboBox<String> metaleLista;
	JLabel parametr, wartoscParametru, jednostka, pracaWyjscia, energiaFotoelektronu, napiecieHamowania, doSuwaka, wartoscPracyWyjscia, doSuwakaUh;
	JRadioButton dlugoscFali, czestotliwoscFali;
	JTextField poleNaWartosc;
	JButton start;
	JSlider suwak, suwakUh;
	JMenu jezyk;
	File file;
	JMenuItem polska;
	JMenuItem angielska;
	JMenuItem zapisywanieDanych;
	JMenuItem importDanych;
	JMenuItem wykres;
	String dodlugosci="Długość fali", doczestotliwosci="Częstotliwość fali";
	static public imagePanel fotokomorka;
	int ktoryParametr;
	double c=3*Math.pow(10,8);
	double h=6.63*Math.pow(10,-34);
	double W=0;
	double me = 0.911;
	int wybranyjezyk=0;

	public mainFrame() throws HeadlessException {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(700,500);
		this.setTitle("Animacja efektu fotoelektrycznego");
		panelLewy = new JPanel(new BorderLayout());
		panelLewy.setBackground(Color.white);
		panelPrawy = new JPanel(new GridLayout(10,1));
		panelPrawy.setBackground(Color.lightGray);
		panelPrawy.setSize(300, 400);
		panelLewyDolny = new JPanel(new GridLayout(2,1));
		panelLewyDolny.setBackground(Color.lightGray);
		this.add(panelLewy, BorderLayout.CENTER);
		this.add(panelPrawy, BorderLayout.LINE_END);
		panelLewy.add(panelLewyDolny, BorderLayout.PAGE_END);
		fotokomorka = new imagePanel();
		panelLewy.add(fotokomorka, BorderLayout.CENTER);
		panelLewy.setBackground(Color.white);

		metaleLista = new JComboBox<String>(metaleWybor);
		metaleLista.setSelectedIndex(0);
		metaleLista.addActionListener(this);
		
		parametr = new JLabel("Parametr:");
		
		JRadioButton dlugoscFali = new JRadioButton(dodlugosci);
		dlugoscFali.setActionCommand("długość");
		dlugoscFali.addActionListener(this);
		JRadioButton czestotliwoscFali = new JRadioButton(doczestotliwosci);
		czestotliwoscFali.setActionCommand("częstotliwość");
		czestotliwoscFali.addActionListener(this);
		dlugoscFali.setBackground(Color.lightGray);
		czestotliwoscFali.setBackground(Color.lightGray);
		ButtonGroup group = new ButtonGroup();
		group.add(dlugoscFali);
		group.add(czestotliwoscFali);
		dlugoscFali.setSelected(true);
		
		wartoscParametru = new JLabel("Wartość parametru:");
		
		panelPomocniczy = new JPanel();
		panelPomocniczy.setBackground(Color.lightGray);
		poleNaWartosc = new JTextField(13);
		poleNaWartosc.setActionCommand("wpisywanie wartości");
		poleNaWartosc.addActionListener(this);
		jednostka = new JLabel("[nm]");
		panelPomocniczy.add(poleNaWartosc);
		panelPomocniczy.add(jednostka);
		
		start = new JButton("START");
		start.setBackground(Color.green);
		start.setActionCommand("start");
		start.addActionListener(this);
		
		pracaWyjscia = new JLabel("Praca wyjścia metalu:");
		energiaFotoelektronu = new JLabel("E_k fotoelektronu:");
		napiecieHamowania = new JLabel("Napięcie hamowania:");
		
		suwak = new JSlider(JSlider.HORIZONTAL, 250, 780, 400);
		suwak.setBackground(Color.lightGray);
		suwak.addChangeListener(new SliderChangeListener());
		doSuwaka = new JLabel("Wartość parametru:");
		suwakUh = new JSlider(JSlider.HORIZONTAL, -10, 10, 0);
		suwakUh.setBackground(Color.lightGray);
		suwakUh.addChangeListener(new SliderChangeListener());
		doSuwakaUh = new JLabel("Napięcie hamowania:");
		panelLewyDolny.add(doSuwakaUh);
		panelLewyDolny.add(suwakUh);
		panelLewyDolny.add(doSuwaka);
		panelLewyDolny.add(suwak);
		panelMetal=new JPanel();
		panelMetal.setBackground(Color.lightGray);
		wartoscPracyWyjscia=new JLabel("-");
		
		panelPrawy.add(metaleLista);
		panelPrawy.add(parametr);
		panelPrawy.add(dlugoscFali);
		panelPrawy.add(czestotliwoscFali);
		panelPrawy.add(wartoscParametru);
		panelPrawy.add(panelPomocniczy);
		panelPrawy.add(start);
		panelMetal.add(pracaWyjscia);
		panelMetal.add(wartoscPracyWyjscia);
		panelPrawy.add(panelMetal);
		panelPrawy.add(energiaFotoelektronu);
		panelPrawy.add(napiecieHamowania);

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);
		jezyk = new JMenu("Wersja językowa");
		menuBar.add(jezyk);
		polska = new JMenuItem("polska");
		jezyk.add(polska);
		polska.setActionCommand("polski");
		polska.addActionListener(this);
		angielska = new JMenuItem("angielska");
		jezyk.add(angielska);
		angielska.setActionCommand("angielski");
		angielska.addActionListener(this);
		zapisywanieDanych = new JMenuItem("Zapisz dane");
		menu.add(zapisywanieDanych);
		zapisywanieDanych.setActionCommand("zapisdopliku");
		zapisywanieDanych.addActionListener(this);
		importDanych = new JMenuItem("Importuj dane");
		menu.add(importDanych);
		importDanych.setActionCommand("importzpliku");
		importDanych.addActionListener(this);
		wykres = new JMenuItem("Wykres I(U_h)");
		menu.add(wykres);
		wykres.setActionCommand("wykres");
		wykres.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String source = e.getActionCommand();
		String sourceMetal = (String) metaleLista.getSelectedItem();
		
		switch (source) {
			case "długość":{
				int zSuwaka=suwak.getValue();
				suwak.setMinimum(250);
				suwak.setMaximum(780);
				if(jednostka.getText()=="[THz]") {
					int przeliczanie=(int)(3*Math.pow(10,8)/(zSuwaka*Math.pow(10,12))*Math.pow(10,9)); 
					//System.out.println(przeliczanie);//przeliczenia miedzy jednostkami 
					poleNaWartosc.setText(Integer.toString(przeliczanie));
					suwak.setValue(przeliczanie);	
				}
				ktoryParametr=0;
				jednostka.setText("[nm]");
				energiakinetyczna();
				zmianakolorufotonow();
				break;
			}
			case "częstotliwość":{
				int zSuwaka=suwak.getValue();
				suwak.setMaximum(1200);
				suwak.setMinimum(385);
				if(jednostka.getText()=="[nm]") {
					int przeliczanie=(int)(3*Math.pow(10,8)/(zSuwaka*Math.pow(10,-9))/Math.pow(10,12)); 
					poleNaWartosc.setText(Integer.toString(przeliczanie));
					suwak.setValue(przeliczanie);
				}
				ktoryParametr=1;
				jednostka.setText("[THz]");
				energiakinetyczna();
				zmianakolorufotonow();
				break;
			}
			case "start":{
				String sprawdzanie = start.getText();
				if(sprawdzanie=="START") {
					if(energiaFotoelektronu.getText()!="efekt nie zachodzi") {
						start.setText("STOP");
						start.setBackground(Color.red);
						fotokomorka.Start();
					}
				}
				if(sprawdzanie=="STOP") {
					start.setText("START");
					start.setBackground(Color.green);
					//fotokomorka.StartSimulattion(false);
					fotokomorka.Start();
				}
				break;
			}
			case "wpisywanie wartości":{
				int zPola=Integer.parseInt(poleNaWartosc.getText());
				suwak.setValue(zPola);
				energiakinetyczna();
				zmianakolorufotonow();
				break;
			}
			case "zapisdopliku":{
				JFileChooser fileChooser = new JFileChooser();
		        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooser.getSelectedFile();
		            try {
		            	String dane = poleNaWartosc.getText() + jednostka.getText() + "   " + energiaFotoelektronu.getText() + "   " + "Metal:" + " "
		        		 + (String) metaleLista.getSelectedItem(); ;
		                FileWriter filewriter = new FileWriter(file, true);
		                BufferedWriter buff = new BufferedWriter(filewriter);
		                PrintWriter writer = new PrintWriter(buff);
		                writer.write(dane);
		                writer.flush();
		                writer.close();
		            } catch (FileNotFoundException e2) {
		                e2.printStackTrace();
		            } catch (IOException e1) {
		                e1.printStackTrace();
		            }
		        }       
				break;
			}
			case "importzpliku":{
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Wybierz plik");
				int r=chooser.showOpenDialog(mainFrame.this);
				if(r==JFileChooser.APPROVE_OPTION) {
				int dlugoscfalizpliku=0;
				String metalzpliku="";

				try {
					Scanner scanfile = new Scanner(chooser.getSelectedFile());
					scanfile.nextLine();
					dlugoscfalizpliku=scanfile.nextInt();
					metalzpliku=scanfile.next();
					scanfile.close();
				}
				
				catch(FileNotFoundException e2) {
					e2.printStackTrace();
				}
				suwak.setValue(dlugoscfalizpliku);
				switch (metalzpliku)
				{
					case "Potas":
					{
						wartoscPracyWyjscia.setText("2,29eV");
						metaleLista.setSelectedIndex(1);
						break;
					}
					case "Srebro":
					{
						wartoscPracyWyjscia.setText("4,50eV");
						metaleLista.setSelectedIndex(2);
						break;
					}
					case "Miedz":
					{
						wartoscPracyWyjscia.setText("4,82eV");
						metaleLista.setSelectedIndex(3);
						break;
					}
					case "Ołów":
					{
						wartoscPracyWyjscia.setText("4,25eV");
						metaleLista.setSelectedIndex(4);	
						break;
					}
					case "Bar":
					{
						wartoscPracyWyjscia.setText("2,61eV");
						metaleLista.setSelectedIndex(5);
						break;
					}
					case "Lit":
					{
						wartoscPracyWyjscia.setText("2,90eV");
						metaleLista.setSelectedIndex(6);
						break;
					}
				}
				repaint();
				energiakinetyczna();
				zmianakolorufotonow();
				}
				break;
			}
			case "wykres":{
				
				break;
			}
			case "polski":{
				wybranyjezyk=0;
				this.setTitle("Animacja efektu fotoelektrycznego");
				metaleWybor[0]="Wybierz metal";
				metaleWybor[1]="Potas (K)";
				metaleWybor[2]="Srebro (Ag)";
				metaleWybor[3]="Miedz (Cu)";
				metaleWybor[4]="Ołów (Pb)";
				metaleWybor[5]="Bar (Ba)";
				metaleWybor[6]="Lit (Li)";
				parametr.setText("Parametr:");
				dodlugosci="Długość fali";
				doczestotliwosci="Częstotliwość fali";
				wartoscParametru.setText("Wartość parametru:");
				pracaWyjscia.setText("Praca wyjścia metalu:");
				energiaFotoelektronu.setText("E_k fotoelektronu:");
				napiecieHamowania.setText("Napięcie hamowania:");
				doSuwaka.setText("Wartość parametru:");
				doSuwakaUh.setText("Napięcie hamowania:");
				jezyk.setText("Wersja językowa");
				polska.setText("polska");
				angielska.setText("angielska");
				zapisywanieDanych.setText("Zapisz dane");
				importDanych.setText("Importuj dane");
				wykres.setText("Wykres I(U_h)");
				ponowneUtworzeniePanelu();
				break;
			}
			case "angielski":{
				wybranyjezyk=1;
				this.setTitle("Photoelectric effect animation");
				metaleWybor[0]="Choose a metal";
				metaleWybor[1]="Potassium (K)";
				metaleWybor[2]="Silver (Ag)";
				metaleWybor[3]="Copper (Cu)";
				metaleWybor[4]="Lead (Pb)";
				metaleWybor[5]="Barium (Ba)";
				metaleWybor[6]="Lithium (Li)";
				parametr.setText("Parameter:");
				dodlugosci="Wavelength";
				doczestotliwosci="Wave frequency";
				wartoscParametru.setText("Parameter value:");
				pracaWyjscia.setText("Work function:");
				energiaFotoelektronu.setText("Photoelectron E_k:");
				napiecieHamowania.setText("Stopping voltage:");
				doSuwaka.setText("Parameter value:");
				doSuwakaUh.setText("Stopping voltage:");
				jezyk.setText("Language version");
				polska.setText("Polish");
				angielska.setText("English");
				zapisywanieDanych.setText("Save");
				importDanych.setText("Import");
				wykres.setText("Chart I(U_h)");
				ponowneUtworzeniePanelu();
				break;
			}
		}
		switch (sourceMetal) {
		case "Wybierz metal":{
			wartoscPracyWyjscia.setText("-");
			break;
		}
		case "Potas (K)":{
			wartoscPracyWyjscia.setText("2,29eV");
			W=2.29;
			energiakinetyczna();
			break;
		}
		case "Srebro (Ag)":{
			wartoscPracyWyjscia.setText("4,50eV");
			W=4.5;
			energiakinetyczna();
			break;
		}
		case "Miedz (Cu)":{
			wartoscPracyWyjscia.setText("4,82eV");
			W=4.82;
			energiakinetyczna();
			break;
		}
		case "Ołów (Pb)":{
			wartoscPracyWyjscia.setText("4,25eV");
			W=4.25;
			energiakinetyczna();
			break;
		}
		case "Bar (Ba)":{
			wartoscPracyWyjscia.setText("2,61eV");
			W=2.61;
			energiakinetyczna();
			break;
		}
		case "Lit (Li)":{
			wartoscPracyWyjscia.setText("2,90eV");
			W=2.90;
			energiakinetyczna();
			break;
		}
		case "Choose a metal":{
			wartoscPracyWyjscia.setText("-");
			break;
		}
		case "Potassium (K)":{
			wartoscPracyWyjscia.setText("2,29eV");
			W=2.29;
			energiakinetyczna();
			break;
		}
		case "Silver (Ag)":{
			wartoscPracyWyjscia.setText("4,50eV");
			W=4.5;
			energiakinetyczna();
			break;
		}
		case "Copper (Cu)":{
			wartoscPracyWyjscia.setText("4,82eV");
			W=4.82;
			energiakinetyczna();
			break;
		}
		case "Lead (Pb)":{
			wartoscPracyWyjscia.setText("4,25eV");
			W=4.25;
			energiakinetyczna();
			break;
		}
		case "Barium (Ba)":{
			wartoscPracyWyjscia.setText("2,61eV");
			W=2.61;
			energiakinetyczna();
			break;
		}
		case "Lithium (Li)":{
			wartoscPracyWyjscia.setText("2,90eV");
			W=2.90;
			energiakinetyczna();
			break;
		}
	}
}
	
	public class SliderChangeListener implements ChangeListener{
		
		@Override
		public void stateChanged(ChangeEvent arg0) {
			String ustawione = String.format("%d", suwak.getValue());
			poleNaWartosc.setText(ustawione);
			energiakinetyczna();
			zmianakolorufotonow();
			String napiecie = String.format("%d",suwakUh.getValue());
			if(wybranyjezyk==0)
				napiecieHamowania.setText("Napięcie hamowania:  "+napiecie+"V");
			if(wybranyjezyk==1)
				napiecieHamowania.setText("Stopping voltage:  "+napiecie+"V");
		}
	}
	
	public void energiakinetyczna() {
		double energiakinetyczna=0;
		if(metaleLista.getSelectedIndex()!=0) {
			if(ktoryParametr==0) {
				double energiafotonu=h*c/(suwak.getValue()*Math.pow(10,-9)*1.6*Math.pow(10,-19)); //energia fotonu w eV
				
				if(energiafotonu>W){
					energiakinetyczna=energiafotonu-W;
	
					
					energiakinetyczna = Math.round(energiakinetyczna * 100);
					energiakinetyczna = energiakinetyczna/100;
					fotokomorka.EfektZachodzi=true;
					fotokomorka.V=Math.sqrt(energiakinetyczna*2/me);
					fotokomorka.U=suwakUh.getValue();
					if(wybranyjezyk==0)
						energiaFotoelektronu.setText("E_k fotoelektronu: "+Double.toString(energiakinetyczna)+"eV");
					if(wybranyjezyk==1)
						energiaFotoelektronu.setText("Photoelectron E_k: "+Double.toString(energiakinetyczna)+"eV");
				}
				else {
					fotokomorka.EfektZachodzi=false;
					if(wybranyjezyk==0)
						energiaFotoelektronu.setText("efekt nie zachodzi");
					if(wybranyjezyk==1)
						energiaFotoelektronu.setText("effect does not happen");
				}
				
			}
			if(ktoryParametr==1) {
				double energiafotonu=h*suwak.getValue()*Math.pow(10,12)/(1.6*Math.pow(10,-19)); //energia fotonu w eV
				if(energiafotonu>W) {
					energiakinetyczna=energiafotonu-W;
					energiakinetyczna = Math.round(energiakinetyczna * 100);
					energiakinetyczna = energiakinetyczna/100;
					fotokomorka.EfektZachodzi=true;
					fotokomorka.V=Math.sqrt(energiakinetyczna*2/me);
					fotokomorka.U=suwakUh.getValue();
					if(wybranyjezyk==0)
						energiaFotoelektronu.setText("E_k fotoelektronu: "+Double.toString(energiakinetyczna)+"eV");
					if(wybranyjezyk==1)
						energiaFotoelektronu.setText("Photoelectron E_k: "+Double.toString(energiakinetyczna)+"eV");
			}
			else {
				fotokomorka.EfektZachodzi=false;
				if(wybranyjezyk==0)
					energiaFotoelektronu.setText("efekt nie zachodzi");
				if(wybranyjezyk==1)
					energiaFotoelektronu.setText("effect does not happen");
			}
				
			}
			
	}
}
	public void zmianakolorufotonow() {
		int zSuwaka=suwak.getValue();
		if(zSuwaka<380)
			fotokomorka.kolorfotonow=new Color(147,112,219);
		if(zSuwaka>379&&zSuwaka<436)
			fotokomorka.kolorfotonow=new Color(148,0,211);
		if(zSuwaka>435&&zSuwaka<495)
			fotokomorka.kolorfotonow=Color.blue;
		if(zSuwaka>494&&zSuwaka<566)
			fotokomorka.kolorfotonow=Color.green;
		if(zSuwaka>565&&zSuwaka<589)
			fotokomorka.kolorfotonow=Color.yellow;
		if(zSuwaka>588&&zSuwaka<627)
			fotokomorka.kolorfotonow=Color.orange;
		if(zSuwaka>626&&zSuwaka<781)
			fotokomorka.kolorfotonow=Color.red;
	}

	public void ponowneUtworzeniePanelu() {
		panelPrawy.removeAll();
		metaleLista = new JComboBox<String>(metaleWybor);
		metaleLista.setSelectedIndex(0);
		metaleLista.addActionListener(this);
		JRadioButton dlugoscFali = new JRadioButton(dodlugosci);
		dlugoscFali.setActionCommand("długość");
		dlugoscFali.addActionListener(this);
		JRadioButton czestotliwoscFali = new JRadioButton(doczestotliwosci);
		czestotliwoscFali.setActionCommand("częstotliwość");
		czestotliwoscFali.addActionListener(this);
		dlugoscFali.setBackground(Color.lightGray);
		czestotliwoscFali.setBackground(Color.lightGray);
		ButtonGroup group = new ButtonGroup();
		group.add(dlugoscFali);
		group.add(czestotliwoscFali);
		dlugoscFali.setSelected(true);
		panelPomocniczy = new JPanel();
		panelPomocniczy.setBackground(Color.lightGray);
		poleNaWartosc = new JTextField(13);
		poleNaWartosc.setActionCommand("wpisywanie wartości");
		poleNaWartosc.addActionListener(this);
		jednostka = new JLabel("[nm]");
		panelPomocniczy.add(poleNaWartosc);
		panelPomocniczy.add(jednostka);
		
		start = new JButton("START");
		start.setBackground(Color.green);
		start.setActionCommand("start");
		start.addActionListener(this);
	
		panelPrawy.add(metaleLista);
		panelPrawy.add(parametr);
		panelPrawy.add(dlugoscFali);
		panelPrawy.add(czestotliwoscFali);
		panelPrawy.add(wartoscParametru);
		panelPrawy.add(panelPomocniczy);
		panelPrawy.add(start);
		panelMetal.add(pracaWyjscia);
		panelMetal.add(wartoscPracyWyjscia);
		panelPrawy.add(panelMetal);
		panelPrawy.add(energiaFotoelektronu);
		panelPrawy.add(napiecieHamowania);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mainFrame frame = new mainFrame();
		frame.setVisible(true);
	}
}
