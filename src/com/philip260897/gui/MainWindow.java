package com.philip260897.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.philip260897.led.AS1116;
import com.philip260897.led.LedDriver;

import javax.swing.event.ChangeEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class MainWindow {

	private JFrame frmMtforceGui;
	private JTextField txtLocalhost;
	private Client client;
	private JComboBox comboBox;
	private JPanel drawingPanel;
	private JComboBox cbColor;
	private JButton btnConnect;
	private JButton btnInitialize;
	private JButton btnFill;
	JSlider slider;
	private List<SensorRect> sensorRects = new ArrayList<SensorRect>();
	private BufferedImage imageBottom;
	private BufferedImage imageTop;
	private BufferedImage imageExtern;
	private LedDriver driver = new LedDriver();
	/**
	 * Launch the application.
	 */
	public static MainWindow window;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    window = new MainWindow();
					window.frmMtforceGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		
		try {
			//Bilder Laden fuer GUI (Bottom Layer, Top Layer, Extern Platine)
			imageBottom = ImageIO.read(MainWindow.class.getResourceAsStream("top2.png"));
			imageTop = ImageIO.read(MainWindow.class.getResourceAsStream("bottom2.png"));
			imageExtern = ImageIO.read(MainWindow.class.getResourceAsStream("extern3.png"));
			//imageBottom = ImageIO.read(new File("C:\\Users\\Philip\\Desktop\\Diplomarbeit_Protokoll\\top2.png"));
			//imageTop = ImageIO.read(new File("C:\\Users\\Philip\\Desktop\\Diplomarbeit_Protokoll\\bottom2.png"));
			//imageExtern = ImageIO.read(new File("C:\\Users\\Philip\\Desktop\\Diplomarbeit_Protokoll\\extern3.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setupSensorRects();			//Virtuele Sensor Objekte erstellen
		initializeWindow();			//Graphische Oberflaeche initialisieren
		initializeDrawingPanel();	//Zeichenflaeche initialisieren
		updateVisibleSensors();		//Je nach ausgwaehltem Layer die richtigen Sensoren anzeigen
		
		unconnectedState();			//Button zustaende in den "Unconnected" zustand bringen
		drawingPanel.repaint();		//Zeichenflaeche aktualisieren
	}
	
	/**
	 * Bestimmte GUI elemente aktivieren oder deaktivieren wenn man nicht verbunden ist
	 */
	public void unconnectedState()
	{
		btnConnect.setEnabled(true);
		btnConnect.setText("Connect");
		btnInitialize.setEnabled(false);
		btnUpdate.setEnabled(false);
		cbColor.setSelectedIndex(0);
		driver.setColor(Color.RED);
		btnFill.setEnabled(false);
		btnC.setEnabled(false);
		slider.setEnabled(false);
		cbColor.setEnabled(false);
		textField.setEnabled(false);
		driver.clear();
		
		//Alle Sensoren zuruecksetzten
		for(SensorRect sensor : sensorRects)
			sensor.reset();
	}
	
	/**
	 * Bestimmte GUI elemente aktiveren oder deaktivieren wenn man verbunden ist
	 */
	public void connectedState()
	{
		btnConnect.setEnabled(true);
		btnConnect.setText("Disconnect");
		btnInitialize.setEnabled(true);
		btnUpdate.setEnabled(true);
		btnFill.setEnabled(true);
		btnC.setEnabled(true);
		slider.setEnabled(true);
		cbColor.setEnabled(true);
		textField.setEnabled(true);
	}

	/**
	 * Inhalt des Fensters initialisieren
	 */
	private void initializeWindow() 
	{
		
		frmMtforceGui = new JFrame();
		frmMtforceGui.setTitle("MTForce GUI");
		frmMtforceGui.setBounds(100, 100, 982, 566);
		frmMtforceGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMtforceGui.getContentPane().setLayout(null);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(btnConnect.getText().equals("Connect")) {
					//btnConnect.setEnabled(false);
					connectToServer();
					if(client.isConnected())
						btnConnect.setText("Disconnect");
				} else {
					disconnect();
					btnConnect.setText("Connect");
				}
			}
		});
		btnConnect.setBounds(128, 436, 108, 23);
		frmMtforceGui.getContentPane().add(btnConnect);
		
		JLabel lblIp = new JLabel("IP:");
		lblIp.setBounds(10, 440, 28, 14);
		frmMtforceGui.getContentPane().add(lblIp);
		
		txtLocalhost = new JTextField();
		txtLocalhost.setText("localhost");
		txtLocalhost.setBounds(32, 437, 86, 20);
		frmMtforceGui.getContentPane().add(txtLocalhost);
		txtLocalhost.setColumns(10);
		
		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateVisibleSensors();
			}
		});
		
		comboBox.addItem("TOP");
		comboBox.addItem("BOTTOM");
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(544, 437, 108, 20);
		frmMtforceGui.getContentPane().add(comboBox);
		
		btnInitialize = new JButton("Initialize");
		btnInitialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(client.isConnected())
					client.sendInitializeAll();
				btnInitialize.setEnabled(false);
				sendUpdateSensorStatusRequest();
			}
		});
		btnInitialize.setBounds(246, 436, 89, 23);
		frmMtforceGui.getContentPane().add(btnInitialize);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(SensorEnum sensor : SensorEnum.values())
					if(client.isConnected())
						client.sendValueRequest(sensor);
			}
		});
		btnUpdate.setBounds(345, 436, 89, 23);
		frmMtforceGui.getContentPane().add(btnUpdate);
	}
	
	/**
	 * Sensoren Koordinaten fuer die GUI definieren und auf welchem layer
	 */
	private void setupSensorRects() {
		SensorRect adcRect = new SensorRect(56,73, 45, 18, SensorEnum.ADC, SensorPlacement.BOTTOM);
		//adcRect.addValue("CHANNEL_1", "2.34V");
		SensorRect dof9Rect = new SensorRect(168, 80, 16, 15, SensorEnum.DOF9, SensorPlacement.BOTTOM);
		SensorRect baroRect = new SensorRect(207, 71, 26, 15, SensorEnum.BAROMETER, SensorPlacement.BOTTOM);
		
		SensorRect lightRect = new SensorRect(33, 244, 25, 20, SensorEnum.LIGHT_SENSOR, SensorPlacement.TOP);
		SensorRect thermRect = new SensorRect(50 + 650, 102, 26, 27, SensorEnum.THERMOMETER, SensorPlacement.EXT);
		SensorRect humRect = new SensorRect(127 + 650, 108, 28, 27, SensorEnum.HUMIDITY_SENSOR, SensorPlacement.EXT);
		SensorRect distRect = new SensorRect(127 + 650, 150, 28, 27, SensorEnum.DISTANCE_SENSOR, SensorPlacement.EXT);
		
		
		sensorRects.add(adcRect);
		sensorRects.add(dof9Rect);
		//sensorRects.add(baroRect);
		sensorRects.add(lightRect);
		sensorRects.add(thermRect);
		//sensorRects.add(humRect);
		sensorRects.add(distRect);
	}
	
	//Padding zum zeichen der objekte auf der zeichenflaeche
	final int paddingLeft = 5;
	final int paddingRight = 5;
	final int paddingTop = 5;
	final int paddingBottom = 5;
	final int lineHeight = 10;
	private JButton btnUpdate;
	
	
	/**
	 * Initialisiert komponenten fuers Drawing panel
	 */
	private void initializeDrawingPanel()
	{
		driver.setColor(Color.blue);
		driver.setOnAll();
		drawingPanel = new JPanel(){
			//Logik zum zeichnen
			@Override
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				
				//Sensor Vierecke zeichnen wenn sichtbar
				synchronized(sensorRects) {
				for(SensorRect sensorRect : sensorRects) {
					if(sensorRect.isVisible()) {
						Color bgColor = Color.gray;
						if(sensorRect.getStatus() == SensorStatus.INITIALIZATION_FAILED) bgColor = Color.RED;
						if(sensorRect.getStatus() == SensorStatus.RUNNING) bgColor = Color.GREEN;
						g.setColor(bgColor);
						g.fillRect(sensorRect.getX(), sensorRect.getY(), sensorRect.getWidth(), sensorRect.getHeight());
					}
				}
				
				//Leds zeichnen
				if(comboBox.getSelectedIndex() == 0) {
					g.setColor(driver.getColor());
					for(AS1116 ass : driver.getAss()) {
						for(Point p : ass.getOnLeds()) {
							Rectangle rect = ass.getLed(p.x, p.y);
							g.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
						}
					}
				}
				
				//Platinenbild zeichnen
				if(imageBottom!=null) {
					g.drawImage(((comboBox.getSelectedIndex() == 1) ? imageBottom : imageTop), 0, 0, null);
					g.drawImage(imageExtern, 650, 0, null);
				}
				
				//Hover info zeichnen
				g.setColor(Color.WHITE);
				for(SensorRect sensorRect : sensorRects) {
					if(sensorRect.isVisible() && sensorRect.isSelected()) {
						if(sensorRect.getSensor() != SensorEnum.LIGHT_SENSOR)
							drawSelection(g,sensorRect.getX()-50,sensorRect.getY()+25, sensorRect);
						else
							drawSelection(g,sensorRect.getX(),sensorRect.getY()+25, sensorRect);
					}
				}
				}
				//Info Spalte zeichnen
				drawValuePane(g);
			}
		};
		drawingPanel.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				synchronized(sensorRects) {
					for(SensorRect rect : sensorRects)
					{
						rect.setSelected(false);
						if(rect.isOver(e.getX(), e.getY())) {
							rect.setSelected(true);
						}
					}
				}
				drawingPanel.repaint();
			}
			
		});
		drawingPanel.setBounds(10, 11, 946, 418);
		frmMtforceGui.getContentPane().add(drawingPanel);
		
		JLabel lblDisplayString = new JLabel("Display String:");
		lblDisplayString.setBounds(682, 440, 99, 14);
		frmMtforceGui.getContentPane().add(lblDisplayString);
		
		textField = new JTextField();
		textField.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent arg0) {
			}
			public void inputMethodTextChanged(InputMethodEvent arg0) {
			}
		});
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				textUpdate(textField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				textUpdate(textField.getText());
			}
		    // implement the methods
		});
		textField.setBounds(791, 437, 165, 20);
		frmMtforceGui.getContentPane().add(textField);
		textField.setColumns(10);
		
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(client != null && client.isConnected())
				client.sendLedsBrightness(slider.getValue());
			}
		});

		slider.setValue(0);
		slider.setMaximum(15);
		slider.setBounds(791, 465, 165, 26);
		frmMtforceGui.getContentPane().add(slider);
		
		JLabel lblBrightness = new JLabel("Brightness:");
		lblBrightness.setBounds(682, 465, 72, 14);
		frmMtforceGui.getContentPane().add(lblBrightness);
		
		lblColor = new JLabel("Color:");
		lblColor.setBounds(682, 490, 46, 14);
		frmMtforceGui.getContentPane().add(lblColor);
		
		cbColor = new JComboBox();
		cbColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch((String)cbColor.getSelectedItem())
				{
					case "RED": driver.setColor(Color.RED); break;
					case "GREEN": driver.setColor(Color.GREEN);break;
					case "BLUE": driver.setColor(Color.BLUE);break;
					case "YELLOW": driver.setColor(Color.YELLOW);break;
					case "MAGENTA": driver.setColor(Color.MAGENTA);break;
					case "CYAN": driver.setColor(Color.CYAN);break;
					case "WHITE": driver.setColor(Color.WHITE);break;
					default: driver.setColor(Color.RED);break;
				}
				if(client != null && client.isConnected()) {
				client.sendLedColor((String)cbColor.getSelectedItem());
				if(!textField.getText().equals("")) {
					client.sendLedsText(textField.getText().toUpperCase());
				}
				else
				{
					client.sendLedsFill();
					driver.setOnAll();
					drawingPanel.repaint();
				}
				}
			}
		});
		cbColor.setBounds(791, 496, 72, 20);
		frmMtforceGui.getContentPane().add(cbColor);
		cbColor.addItem("RED");
		cbColor.addItem("GREEN");
		cbColor.addItem("BLUE");
		cbColor.addItem("YELLOW");
		cbColor.addItem("CYAN");
		cbColor.addItem("MAGENTA");
		cbColor.addItem("WHITE");
		
		btnFill = new JButton("F");
		btnFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.sendLedsFill();
				driver.setOnAll();
				drawingPanel.repaint();
			}
		});
		btnFill.setBounds(867, 495, 46, 23);
		frmMtforceGui.getContentPane().add(btnFill);
		
		btnC = new JButton("C");
		btnC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendLedsClear();
				driver.clear();
				drawingPanel.repaint();
			}
		});
		btnC.setBounds(917, 495, 39, 23);
		frmMtforceGui.getContentPane().add(btnC);
	}

	private void textUpdate(String text)
	{
		driver.writeString(text.toUpperCase());
		drawingPanel.repaint();
		if(!text.equals(""))
			client.sendLedsText(text.toUpperCase());
		else
			client.sendLedsClear();
		//an anzeige schicken
	}
	
	private void updateVisibleSensors()
	{
		synchronized(sensorRects) {
			for(SensorRect s : sensorRects)
			{
				s.setVisible(false);
				if(comboBox.getSelectedIndex() == 0 && s.getPlacement() == SensorPlacement.TOP)
				{
					s.setVisible(true);
				}
				if(comboBox.getSelectedIndex() == 1 && s.getPlacement() == SensorPlacement.BOTTOM)
				{
					s.setVisible(true);
				}
				if(s.getPlacement() == SensorPlacement.EXT)
					s.setVisible(true);
				if(s.getSensor() == SensorEnum.DISTANCE_SENSOR)
					s.setVisible(false);
			}
			if(drawingPanel != null)
				drawingPanel.repaint();
		}
	}
	
	int received = 0, max = 0;
	private JTextField textField;
	private JLabel lblColor;
	private JButton btnC;
	private void connectToServer()
	{
		client = new Client(txtLocalhost.getText(), 25565);
		client.setCmdReceivedEvent(new CmdReceivedEvent(){

			@Override
			public void statusReceivedCommand(SensorEnum sensor, SensorStatus status) {
				// TODO Auto-generated method stub
				System.out.println("Status "+sensor.name()+" "+status.name());
				SensorRect sens = getSensor(sensor);
				if(sens == null) return;
				sens.setStatus(status);
				drawingPanel.repaint();
			}

			@Override
			public void valueReceivedCommand(SensorEnum sensor, String valueType, String value) {
				SensorRect sens = getSensor(sensor);
				if(sens == null) return;
				sens.addValue(valueType, value);
				drawingPanel.repaint();
				
				received++;
				max++;
				//System.out.println(received + " " + max);
				synchronized(sensorRects) {
				if(received == sensorRects.size()) 
				{
					received = 0;
					try 
					{
						Thread.sleep(0);
						for(SensorEnum se : SensorEnum.values())
							client.sendValueRequest(se);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
				}
			}
		});
		client.connect();
		if(client.isConnected())
			connectedState();
	}
	
	public void disconnect()
	{
		client.close();
		unconnectedState();
	}
	
	private void sendUpdateSensorStatusRequest()
	{
		for(SensorEnum sensor : SensorEnum.values())
			client.sendStatusRequest(sensor);
	}
	
	private SensorRect getSensor(SensorEnum sensor) {
		synchronized(sensorRects) {
		for(SensorRect s : sensorRects)
			if(s.getSensor() == sensor)
				return s;
		}
		return null;
	
	}
	
	public void drawValuePane(Graphics g)
	{
		int xLeft = 650; int xRight = 800;
		int yLeft = 210; int yRight = 210;
		int y = yLeft;
		int x = xLeft;
		
		synchronized(sensorRects) {
		
		for(int i = 0; i < sensorRects.size(); i++) {
			
			SensorRect sensorRect = sensorRects.get(i);
			if(sensorRect.getSensor() == SensorEnum.DOF9 | sensorRect.getSensor() == SensorEnum.DISTANCE_SENSOR | sensorRect.getSensor() == SensorEnum.LIGHT_SENSOR) {
				y = yRight;
				x = xRight;
			} else {
				y = yLeft;
				x = xLeft;
			}
			
			
			int width = g.getFontMetrics().stringWidth(sensorRect.getName()) + paddingLeft + paddingRight;
			width = 145;
			//g.drawRect(sensorRect.getX(), sensorRect.getY(), sensorRect.getWidth(), sensorRect.getHeight());
			List<String> strings = new ArrayList<String>();
			synchronized(sensorRect.getValues()) 
			{			
				Map<String,String> map = sensorRect.getValues();
				for (Map.Entry<String, String> entry : map.entrySet())
				{
					String s = entry.getKey()+": "+entry.getValue();
					int w = g.getFontMetrics().stringWidth(s) + paddingLeft + paddingRight;
					//if(w > width) width = w;
				    strings.add(entry.getKey()+": "+entry.getValue());
				}
			}
			
			int currY = paddingTop;
			currY += lineHeight;
	
			currY += paddingBottom;
			currY += paddingTop;
			for(String s : strings)
			{
				currY += lineHeight;
				currY += paddingTop;
			}
			
			//g.fillRect(x, y, width, currY);
			
			g.setColor(Color.ORANGE);
			g.fillRect(x, y,width, lineHeight+paddingBottom+paddingTop);
			g.setColor(Color.CYAN);
			g.fillRect(x, y+lineHeight+paddingBottom+paddingTop, width, currY - (lineHeight+paddingBottom+paddingTop));
			
			currY = paddingTop;
			currY += lineHeight;
	
			g.setColor(Color.BLACK);
			g.drawString(sensorRect.getName(), x+(width-g.getFontMetrics().stringWidth(sensorRect.getName()))/2, y+currY);
			
			currY += paddingBottom;
			if(g != null)
			g.drawLine(x, y+currY, x+width, currY+y);
			
			currY += paddingTop;
			for(String s : strings)
			{
				currY += lineHeight;
				if(g != null)
				g.drawString(s,x+ 5, y+currY);
				currY += paddingTop;
			}
			
			if(g != null)
			g.drawRect(x, y, width, currY);
			
			if(sensorRect.getSensor() == SensorEnum.DOF9 | sensorRect.getSensor() == SensorEnum.DISTANCE_SENSOR | sensorRect.getSensor() == SensorEnum.LIGHT_SENSOR) {
				yRight += currY+5;
			}
			else
			{
				yLeft += currY+5;
			}
		}
		}
	}
	
	private void drawSelection(Graphics g,int x, int y, SensorRect sensorRect)
	{
		int width = g.getFontMetrics().stringWidth(sensorRect.getName()) + paddingLeft + paddingRight;
		
		g.drawRect(sensorRect.getX(), sensorRect.getY(), sensorRect.getWidth(), sensorRect.getHeight());
		
		synchronized(sensorRects) {
		List<String> strings = new ArrayList<String>();
		synchronized(sensorRect.getValues()) 
		{
			Map<String,String> map = sensorRect.getValues();
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				String s = entry.getKey()+": "+entry.getValue();
				int w = g.getFontMetrics().stringWidth(s) + paddingLeft + paddingRight;
				if(w > width) width = w;
			    strings.add(entry.getKey()+": "+entry.getValue());
			}
		}
		
		
		int currY = paddingTop;
		currY += lineHeight;

		currY += paddingBottom;
		currY += paddingTop;
		for(String s : strings)
		{
			currY += lineHeight;
			currY += paddingTop;
		}
		
		//g.fillRect(x, y, width, currY);
		
		g.setColor(Color.ORANGE);
		g.fillRect(x, y,width, lineHeight+paddingBottom+paddingTop);
		g.setColor(Color.CYAN);
		g.fillRect(x, y+lineHeight+paddingBottom+paddingTop, width, currY - (lineHeight+paddingBottom+paddingTop));
		
		currY = paddingTop;
		currY += lineHeight;

		g.setColor(Color.BLACK);
		g.drawString(sensorRect.getName(), x+(width-g.getFontMetrics().stringWidth(sensorRect.getName()))/2, y+currY);
		
		currY += paddingBottom;
		if(g != null)
		g.drawLine(x, y+currY, x+width, currY+y);
		
		currY += paddingTop;
		for(String s : strings)
		{
			currY += lineHeight;
			if(g != null)
			g.drawString(s,x+ 5, y+currY);
			currY += paddingTop;
		}
		
		if(g != null)
		g.drawRect(x, y, width, currY);
		}
	}
}
