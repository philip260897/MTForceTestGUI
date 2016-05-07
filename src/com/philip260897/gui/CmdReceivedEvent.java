package com.philip260897.gui;

public interface CmdReceivedEvent 
{	
	public void statusReceivedCommand(SensorEnum sensor, SensorStatus status);
	
	public void valueReceivedCommand(SensorEnum sensor, String valueType, String value);
}
