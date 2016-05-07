package com.philip260897.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;


public class Client implements Runnable
{
	private Socket clientSocket;
	
	private BufferedWriter oo;
	private BufferedReader oi;
	private int port;
	private String ip;
	private Thread thread;
	
	private CmdReceivedEvent event;
	
	private boolean connected = false;
	
	public Client(String ip, int port)
	{
		this.port = port;
		this.ip = ip;
	}
	
	public void setCmdReceivedEvent(CmdReceivedEvent event) {
		this.event = event;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void connect()
	{
	    try {
	    	clientSocket = new Socket(InetAddress.getByName(ip), port);
		      System.out.println("Client connected" + clientSocket.getInetAddress());
		      oo = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		      oi = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		      
		      connected = true;
		      thread = new Thread(this);
		      thread.start();
		    } 
		    catch (IOException ioe) 
		    {
		      System.out.println(ioe);
		    }
	}
	
	public Socket getClient()
	{
		return clientSocket;
	}
	
	public void close()
	{
		try{
		MainWindow.window.unconnectedState();
		clientSocket.close();
		thread.stop();
		}catch(Exception ex) {ex.printStackTrace();}
	}
	
	public void writeLine(String line)
	{
		if(!clientSocket.isConnected()) return;
		try{
			oo.write(line+"\n");
			oo.flush();
		}catch(Exception ex){ex.printStackTrace(); close();}
	}
	
	public String read() throws Exception
	{
		return oi.readLine();
	}
	
	public void sendStatusRequest(SensorEnum sensor)
	{
		writeLine("STATUS "+sensor.name());
	}
	
	public void sendValueRequest(SensorEnum sensor)
	{
		writeLine("VALUE "+sensor.name());
	}
	
	public void sendInitializeAll()
	{
		writeLine("INIT_ALL");
	}
	
	public void sendLedsFill()
	{
		writeLine("LEDS_FILL");
	}
	
	public void sendLedsClear()
	{
		writeLine("LEDS_CLEAR");
	}
	
	public void sendLedsBrightness(int b)
	{
		writeLine("LEDS_BRIGHTNESS "+b);
	}
	
	public void sendLedsText(String text)
	{
		writeLine("LEDS_WRITE "+text);
	}
	
	public void sendInitializeSensor(SensorEnum sensor)
	{
		writeLine("INIT "+sensor.name());
	}
	
	public void sendLedColor(String color)
	{
		writeLine("LEDS_COLOR "+color);
	}

	@Override
	public void run() 
	{
		try
		{
			while(true)
			{
				String[] line = read().split(" ");
				if(line[0].equals("STATUS"))
				{
					SensorEnum sensor = SensorEnum.valueOf(line[1]);
					SensorStatus status = SensorStatus.valueOf(line[2]);
					if(event != null) event.statusReceivedCommand(sensor, status);
				}
				if(line[0].equals("VALUE"))
				{
					SensorEnum sensor = SensorEnum.valueOf(line[1]);
					if(event != null) event.valueReceivedCommand(sensor, line[2], line[3]);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("[Client] Connection closed!");
			close();
		}
	}
}
