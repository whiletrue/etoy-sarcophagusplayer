package com.etoy.processing.sarcophagus;

import processing.core.*;
import jmcvideo.*;

public class ElementMovie extends Element {
	
	protected JMCMovie movie;
	protected PApplet applet;
	protected PImage frame;
	
	protected Display display;
	
	public ElementMovie() {
		super();
	}
	
	public void setup(Display display) {
		this.display = display;
		setupMovie();
		
		
		//movie.play();
		//movie.setLoopCount(getRepeat());
		
		//movie.loop();
		
	}
	
	public void setupMovie() {
		String movieFile = getProperty("src");
		System.out.println("Setup Movie: src: " + getName());
		
		applet = display.getApplet();
		movie = new JMCMovie(applet, movieFile);
	}
	
	public void start() {
		
		if (!movie.isPlaying()) {
			System.out.println("starting movie" + getName());
			movie.play();
		}
	}
	
	public void stop() {
		
		if (movie.isPlaying()) {
			System.out.println("stopping movie " + getName());
			movie.stop();
			movie.dispose();
			setupMovie();
		}
	}
	
	public DisplayBuffer draw(DisplayBuffer buff) {
		PGraphics g = buff.getDisplay();
		
		if (movie.width > 1) {
			if (duration == 0) {
				//setDuration((int) movie.getDuration());
			}
			
			if (isTransparent()) {
				frame = blackAlpha(movie, new PImage(movie.width,  movie.height, processing.core.PConstants.ARGB));
				g.image(frame, posx, posy, movie.width, movie.height);
			} else {
				g.image(movie, posx, posy, movie.width, movie.height);
			}
			
			
		}
		
		return buff;
	}
	
	
}
