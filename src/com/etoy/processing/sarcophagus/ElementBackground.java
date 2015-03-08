package com.etoy.processing.sarcophagus;

import processing.core.PGraphics;

public class ElementBackground extends Element {
	
	public ElementBackground() {
		super();
	}
	
	public void setup(Display display) {
		
	}
	
	public DisplayBuffer draw(DisplayBuffer buff) {
		PGraphics g = buff.getDisplay();
		g.background(0);
		return buff;
	}
}
