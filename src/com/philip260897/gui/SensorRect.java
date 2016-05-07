package com.philip260897.gui;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class SensorRect 
{
	private Rectangle rect;
	private SensorEnum sensor;
	private SensorPlacement placement;
	private SensorStatus status;
	private Map<String,String> values = new HashMap<String,String>();
	
	private boolean selected = false;
	private boolean visible = false;
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public SensorRect(int x, int y, int width, int height, SensorEnum sensor, SensorPlacement placement)
	{
		this.rect = new Rectangle(x, y, width, height);
		this.sensor = sensor;
		this.placement = placement;
	}
	
	public synchronized void addValue(String valueType, String value) {
		synchronized(values) {
			values.put(valueType, value);
		}
	}
	
	public synchronized void reset() {
		synchronized(values) {
			values.clear();
		}
		status = SensorStatus.NOT_INITIALIZED;
	}
	
	public synchronized Map<String,String> getValues() {
		return values;
	}
	
	public SensorEnum getSensor() {
		return sensor;
	}
	
	public String getName() {
		return sensor.name();
	}
	
	public int getX() {
		return rect.x;
	}
	
	public int getY() {
		return rect.y;
	}
	
	public int getWidth() {
		return rect.width;
	}
	
	public int getHeight() {
		return rect.height;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public SensorPlacement getPlacement() {
		return placement;
	}
	
	public void setStatus(SensorStatus status) {
		this.status = status;
	}
	
	public SensorStatus getStatus() {
		return status;
	}
	
	public boolean isOver(int x, int y) {
		return (x >= this.getX() && x <= this.getX()+this.getWidth() && y >= this.getY() && y <= this.getY() + this.getHeight());
	}
}
