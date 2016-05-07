package com.philip260897.led;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class LedDriver 
{

	private Color color;
	private List<AS1116> ass = new ArrayList<AS1116>();
	
	public LedDriver()
	{
		LedDictionary.LoadDictionary();
		AS1116 as1 = new AS1116();
		as1.addLed(new Point(1,0), new Rectangle(65+27,41,9,8));
		as1.addLed(new Point(0,0), new Rectangle(65+80,41,9,8));
		as1.addLed(new Point(1,1), new Rectangle(65+0,81,9,8));
		as1.addLed(new Point(0,1), new Rectangle(65+80,81,9,8));
		as1.addLed(new Point(1,2), new Rectangle(65+27,81,9,8));
		as1.addLed(new Point(0,2), new Rectangle(65+107,81,9,8));
		as1.addLed(new Point(1,3), new Rectangle(65+54,81,9,8));
		as1.addLed(new Point(0,3), new Rectangle(65+80,120,9,8));
		as1.addLed(new Point(1,4), new Rectangle(65+27,120,9,8));
		as1.addLed(new Point(0,4), new Rectangle(65+54,160,7,8));
		as1.addLed(new Point(1,5), new Rectangle(65+0,160,9,8));
		as1.addLed(new Point(0,5), new Rectangle(65+80,160,9,8));
		as1.addLed(new Point(1,6), new Rectangle(65+27,160,9,8));
		as1.addLed(new Point(0,6), new Rectangle(65+107,160,9,8));
		as1.addLed(new Point(1,7), new Rectangle(65+27,200,9,8));
		as1.addLed(new Point(0,7), new Rectangle(65+80,200,9,8));
		AS1116 as2 = new AS1116();
		as2.addLed(new Point(1,0), new Rectangle(209+27,41,9,8));
		as2.addLed(new Point(0,0), new Rectangle(209+80,41,9,8));
		as2.addLed(new Point(1,1), new Rectangle(209+0,81,9,8));
		as2.addLed(new Point(0,1), new Rectangle(209+80,81,9,8));
		as2.addLed(new Point(1,2), new Rectangle(209+27,81,9,8));
		as2.addLed(new Point(0,2), new Rectangle(209+107,81,9,8));
		as2.addLed(new Point(1,3), new Rectangle(209+54,81,9,8));
		as2.addLed(new Point(0,3), new Rectangle(209+80,120,9,8));
		as2.addLed(new Point(1,4), new Rectangle(209+27,120,9,8));
		as2.addLed(new Point(0,4), new Rectangle(209+54,160,7,8));
		as2.addLed(new Point(1,5), new Rectangle(209+0,160,9,8));
		as2.addLed(new Point(0,5), new Rectangle(209+80,160,9,8));
		as2.addLed(new Point(1,6), new Rectangle(209+27,160,9,8));
		as2.addLed(new Point(0,6), new Rectangle(209+107,160,9,8));
		as2.addLed(new Point(1,7), new Rectangle(209+27,200,9,8));
		as2.addLed(new Point(0,7), new Rectangle(209+80,200,9,8));
		AS1116 as3 = new AS1116();
		as3.addLed(new Point(1,0), new Rectangle(351+27,41,9,8));
		as3.addLed(new Point(0,0), new Rectangle(351+80,41,9,8));
		as3.addLed(new Point(1,1), new Rectangle(351+0,81,9,8));
		as3.addLed(new Point(0,1), new Rectangle(351+80,81,9,8));
		as3.addLed(new Point(1,2), new Rectangle(351+27,81,9,8));
		as3.addLed(new Point(0,2), new Rectangle(351+107,81,9,8));
		as3.addLed(new Point(1,3), new Rectangle(351+54,81,9,8));
		as3.addLed(new Point(0,3), new Rectangle(351+80,120,9,8));
		as3.addLed(new Point(1,4), new Rectangle(351+27,120,9,8));
		as3.addLed(new Point(0,4), new Rectangle(351+54,160,7,8));
		as3.addLed(new Point(1,5), new Rectangle(351+0,160,9,8));
		as3.addLed(new Point(0,5), new Rectangle(351+80,160,9,8));
		as3.addLed(new Point(1,6), new Rectangle(351+27,160,9,8));
		as3.addLed(new Point(0,6), new Rectangle(351+107,160,9,8));
		as3.addLed(new Point(1,7), new Rectangle(351+27,200,9,8));
		as3.addLed(new Point(0,7), new Rectangle(351+80,200,9,8));
		AS1116 as4 = new AS1116();
		as4.addLed(new Point(1,0), new Rectangle(495+27,41,9,8));
		as4.addLed(new Point(0,0), new Rectangle(495+80,41,9,8));
		as4.addLed(new Point(1,1), new Rectangle(495+0,81,9,8));
		as4.addLed(new Point(0,1), new Rectangle(495+80,81,9,8));
		as4.addLed(new Point(1,2), new Rectangle(495+27,81,9,8));
		as4.addLed(new Point(0,2), new Rectangle(495+107,81,9,8));
		as4.addLed(new Point(1,3), new Rectangle(495+54,81,9,8));
		as4.addLed(new Point(0,3), new Rectangle(495+80,120,9,8));
		as4.addLed(new Point(1,4), new Rectangle(495+27,120,9,8));
		as4.addLed(new Point(0,4), new Rectangle(495+54,160,7,8));
		as4.addLed(new Point(1,5), new Rectangle(495+0,160,9,8));
		as4.addLed(new Point(0,5), new Rectangle(495+80,160,9,8));
		as4.addLed(new Point(1,6), new Rectangle(495+27,160,9,8));
		as4.addLed(new Point(0,6), new Rectangle(495+107,160,9,8));
		as4.addLed(new Point(1,7), new Rectangle(495+27,200,9,8));
		as4.addLed(new Point(0,7), new Rectangle(495+80,200,9,8));
		
		ass.add(as1);
		ass.add(as2);
		ass.add(as3);
		ass.add(as4);
	}
	
	public void writeString(String s)
	{
		clear();
		int max = 4;
		if(s.length() < max)
			max = s.length();
		for(int i = 0; i < max; i++)
		{
			LedDigit digit = LedDictionary.getDigit(Character.toString(s.charAt(i)));
			for(Point p : digit.getPoints())
				ass.get(i).setLedOn(p.x, p.y);
		}
	}
	
	public void clear() {
		for(AS1116 as : ass)
			as.clear();
	}
	
	public void setOnAll() {
		for(AS1116 as : ass)
			for(int x = 0; x < 2; x++)
				for(int y = 0; y < 8; y++)
					as.setLedOn(x, y);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public List<AS1116> getAss() {
		return ass;
	}
}
