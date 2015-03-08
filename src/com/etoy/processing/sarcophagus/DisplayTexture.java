package com.etoy.processing.sarcophagus;

import processing.core.PImage;

public class DisplayTexture {
	
	protected PImage img;
	
	DisplayTexture() {
		
	}
	
	DisplayTexture(int w, int h) {
		setup(w, h);
	}
	
	public void setup(int w, int h) {
		setImage(new PImage(w, h, PImage.RGB));
	}
	
	public void setImage(PImage i) {
		img = i;
	}
	
	public PImage getImage() {
		return img;
	}
	
	public PImage updateTexture(PImage buff, int magnifier) {
		
		img.loadPixels();
		int x,y,mx,my,pos,magpos;
		for(y=0; y<buff.height; y++) {
			for(x=0; x<buff.width; x++) {
				pos = (y * buff.width) + x;
				for(my=0; my<magnifier; my++) {
					for(mx=0; mx<magnifier; mx++) {
						magpos = ((y * magnifier + my) * img.width) + (x * magnifier + mx);
						img.pixels[magpos] = buff.pixels[pos];
					}
				}
			}
		}
		
		img.updatePixels();
		return img;
		
	}
	
	
	
}
