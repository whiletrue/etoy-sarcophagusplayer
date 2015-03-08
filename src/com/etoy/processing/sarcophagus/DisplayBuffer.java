package com.etoy.processing.sarcophagus;

import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import processing.core.PImage;
import processing.core.PApplet;
import processing.core.PConstants.*;

public class DisplayBuffer {
	
	public int width;
	public int height;
	
	public PGraphics display;
	
	protected PApplet applet;
	
	protected int size;
	
	
	DisplayBuffer(PApplet a) {
		
	}
	
	DisplayBuffer(PApplet a, int w, int h) {
		setup(a, w, h);
	}
	
	public void setup(PApplet a, int w, int h) {
		width = w;
		height = h;
		size = (width * height);
		display = a.createGraphics(width, height, processing.core.PConstants.JAVA2D);// new PGraphicsJava2D();
		display.setSize(width, height);
	}
	
	
	public PGraphics getDisplay() {
		return display;
	}
	
	
	public PImage getImage() {
		PImage img = display.get(0, 0, display.width, display.height);
		img.loadPixels();
		return img;
	}
	
	
}
