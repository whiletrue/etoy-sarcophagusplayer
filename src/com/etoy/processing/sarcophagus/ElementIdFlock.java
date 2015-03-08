package com.etoy.processing.sarcophagus;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class ElementIdFlock extends Element {
	protected PApplet applet;
	protected Flock flock;
	protected PFont font;
	
	public ElementIdFlock() {
		super();
	}
	
	
	public void setup(Display display) {
		applet = display.getApplet();
		font = applet.createFont("hooge 05_57", 8, false);
		flock = new Flock(applet);
		for (int i = 0; i < 180; i++) {
			PVector vect = new PVector(32, 24);
		    Boid b = new Boid(vect, (float) 2.0, (float) 0.15);
		    flock.addBoid(b);
		}
	}
	
	
	public DisplayBuffer draw(DisplayBuffer buff) {
		PGraphics g = buff.getDisplay();
		g.textFont(font, 8);
		g.beginDraw();
		flock.run(g);
		g.endDraw();
		return buff;
	}
}
