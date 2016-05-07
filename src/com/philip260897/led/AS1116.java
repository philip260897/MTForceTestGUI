package com.philip260897.led;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AS1116 
{
	private Map<Point,Rectangle> leds = new HashMap<Point,Rectangle>();
	private List<Point> onLeds = new ArrayList<Point>();
	
	public AS1116()
	{
		
	}
	
	public void addLed(Point point, Rectangle rect)
	{
		leds.put(point, rect);
	}
	
	public void clear() {
		onLeds.clear();
	}
	
	public void setLedOn(int x, int y) {
		onLeds.add(new Point(x, y));
	}
	
	public List<Point> getOnLeds() {
		return onLeds;
	}
	
	public Rectangle getLed(int x, int y)
	{
		for(Map.Entry<Point,Rectangle> entry : leds.entrySet())
		{
			if((int)entry.getKey().getX() == x && (int)entry.getKey().getY() == y)
				return entry.getValue();
		}
		return null;
	}
}
