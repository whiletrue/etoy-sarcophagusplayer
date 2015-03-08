package com.etoy.processing.sarcophagus;

import com.etoy.processing.sarcophagus.DisplayTexture;
import processing.core.PApplet;

//virtual screen geometry:
//
//      80     32     80     32
//  +---------+   +---------+     16
//  |    lc   +---+    rc   +---+
//  |    l      b      r      d + 32
//  |    lf   +---+    rf   +---+
//  +---------+   +---------+     16
//
//  l=left, r=right, c=ceiling, f=floor, b=back, d=door

public class Display {
	
	public int magnify;
	
	public int width;
	public int height;
	public int length;
	
	public int frameWidth;
	public int frameHeight;
	
	public int displayWidth;
	public int displayHeight;
	
	protected PApplet applet;
	
	// Blinken-Frame display buffer
	protected DisplayBuffer buffer;
	
	// Image Display Texture
	protected DisplayTexture texture;
	
	
	Display(PApplet applet, int w, int h, int l, int mag) {
		setup(applet, w, h, l, mag);
	}
	

	/**
	* @param	w 		width of 3d box
	* @param 	h 		height of 3d box
	* @param	l 		length of 3d box 
	* @param 	mag 	magnification of display image
	*/
	protected void setup(PApplet a, int w, int h, int l, int mag) {
		width = w;
		height = h;
		length = l;
		magnify=mag;
		frameWidth = (2 * width) + (2 * length);
		frameHeight= (width + height);
		displayWidth = (frameWidth * magnify);
		displayHeight = (frameHeight * magnify);
		
		setApplet(a);
		setBuffer(new DisplayBuffer(applet, frameWidth, frameHeight));
		setTexture(new DisplayTexture(displayWidth, displayHeight));
	}
	
	public void setBuffer(DisplayBuffer buff) {
		buffer = buff;
	}
	
	public DisplayBuffer getBuffer() {
		return buffer;
	}
	
	public void setTexture(DisplayTexture t) {
		texture = t;
	}
	
	public DisplayTexture getTexture() {
		return texture;
	}
	
	public void setApplet(PApplet a) {
		applet = a;
	}
	
	public PApplet getApplet() {
		return applet;
	}
}
