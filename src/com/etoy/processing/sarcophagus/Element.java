package com.etoy.processing.sarcophagus;

import processing.core.PImage;
import java.util.Hashtable;


abstract public class Element {
	
	public int posx;
	public int posy;
	
	public int zindex;
	
	public int timeOffset;
	public int duration;
	public int repeat			= 1;
	public boolean transparent	= false;
	public String name;
	
	protected Hashtable<String, String> properties;
	
	Element() {
		setPosition(0, 0);
		setTimeOffset(0);
		setDuration(0);
		setRepeat(1);
		setProperties(new Hashtable<String, String>());
	}
	
	abstract public void setup(Display display);
	
	abstract public DisplayBuffer draw(DisplayBuffer buff);
	
	public void start() {
	}
	
	public void stop() {
	}
	
	public void setProperties(Hashtable<String,String> props) {
		properties = props;
	}
	
	public String getProperty(String key) {
		return properties.get(key);
	}
	
	public void setProperty(String key, String value) {
		if (value != null) {
			properties.put(key, value);
		}
	}
	
	public void setPosition(int x, int y) {
		setPosX(x);
		setPosY(y);
	}
	
	public int getTimeEnd() {
		return (timeOffset + getDuration());
	}
	
	public void setPosX(int x) {
		posx = x;
	}
	
	public void setPosY(int y) {
		posy = y;
	}
	
	public int getDuration() {
		return (repeat * duration);
	}
	
	public void setDuration(int d) {
		duration = d;
	}
	
	public void setTimeOffset(int offs) {
		timeOffset = offs;
	}
	
	public int getTimeOffset() {
		return timeOffset;
	}
	
	public void setRepeat(int rep) {
		repeat = rep;
	}
	
	public int getRepeat() {
		return repeat;
	}
	
	public void setZIndex(int index) {
		zindex = index;
	}
	
	public int getZIndex() {
		return zindex;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	
	public void setTransparent(boolean val) {
		transparent = val;
	}
	
	public boolean isTransparent() {
		return transparent;
	}
	
	public PImage blackAlpha(PImage img, PImage dest) {
		int pos = 0;
		dest.loadPixels();
		
		for(int y= 0; y < img.height; y++) {
			for(int x = 0; x < img.width; x++) {
				pos = (y * img.width) + x;
				// 
				if ((img.pixels[pos] & 0xffffff) <= 0x00000a) {
					dest.pixels[pos] = 0x00ffffff;
				} else {
					dest.pixels[pos] = img.pixels[pos];
				}
			}
		}
		dest.updatePixels();
		return dest;
	}
}
