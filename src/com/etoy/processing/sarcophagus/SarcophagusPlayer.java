
package com.etoy.processing.sarcophagus;
//import com.etoy.processing.sarcophagus.*;
import processing.core.*;
import java.util.*;
import com.etoy.processing.sarcophagus.blinkenlights.*;

public class SarcophagusPlayer extends PApplet {

	protected Display display;
	protected ElementSet elements;
	protected DisplayBuffer dbuff;
	protected DisplayTexture tbuff;
	protected Config config;
	
	protected Frame blinkenFrame;
	protected Writer blinkenWriter;
	
	public void setup() {

		config = Config.getInstance();
		
		Playlist pls = new Playlist();
		System.out.println("pls: " + config.getPlaylist());
		pls.load(config.getPlaylist());
		
		display = new Display(this, 32, 32, 80, 4);
		elements = new ElementSet();
		
		Element mv3 = new ElementBackground();
		elements.add(mv3);
		
		for(Iterator<Element> itr=pls.getElements().iterator();itr.hasNext();) {
			Element e = (Element) itr.next();
			e.setup(display);
			elements.add(e);
		}
		
		mv3.setDuration(elements.getDuration());
		System.out.println("Duration: " + elements.getDuration());
		
		/**
		 * Blinkenwriter
		 */
		blinkenWriter = new Writer("10.23.23.23", 2323);
		blinkenFrame = new Frame(display.frameWidth, display.frameHeight);
		
		elements.reset();
		
		size(display.displayWidth, display.displayHeight, processing.core.PConstants.JAVA2D);
		background(50);
		frameRate(15);
		
		//noLoop();
	}
	
	public void draw() {
		// Blinken frame display buffer
		dbuff = display.getBuffer();
		// Texture for displayed image
		tbuff = display.getTexture();
		
		elements.draw(dbuff);
		
		PImage buffImage = dbuff.getImage();
		
		PImage displayImage = tbuff.updateTexture(buffImage, display.magnify);
		image(displayImage, 0, 0);
		
		/**
		 * Write Blinkenframe 
		 */
		
		blinkenFrame.update(buffImage.pixels);
		blinkenWriter.writeFrame(blinkenFrame);
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "com.etoy.processing.sarcophagus.SarcophagusPlayer" });
	}
	
}
