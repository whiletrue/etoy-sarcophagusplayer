package com.etoy.processing.sarcophagus;

import processing.core.PGraphics;
import processing.core.PImage;
import java.util.Random;

public class ElementGameOfLife extends Element {

	protected int[] oldframe,newframe;
	protected int width,height;
	protected int cnt=0;
	
	
	public ElementGameOfLife() {
		
	}
	
	
	public void setup(Display display) {
		width = display.frameWidth;
		height= display.frameHeight;
		oldframe = new int[width * height];
		newframe = new int[width * height];
		
		System.out.println("gol: " + width+ "/"+height + " -> " + (width * height));
		initialize();
	}
		
	
	public DisplayBuffer draw(DisplayBuffer buff) {
		
		int pos,ncount=0;
		PImage img = new PImage(buff.width, buff.height, processing.core.PConstants.ARGB);
		
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				pos = (y * width) + x;
				ncount = getLivingNeighbours(pos);
				
				if (isLiving(pos)) {
					newframe[pos] = (ncount == 2 || ncount == 3) ? 1 : 0;
					//System.out.println("pos: " + pos + " n: " + ncount);
				} else {
					newframe[pos] = (ncount == 3) ? 1 : 0;
					if (ncount == 3) {
					//	System.out.println("make living: " + pos);
					}
				}
				
				img.pixels[pos] = (newframe[pos] == 1) ? 0xffffffff : 0x00ffffff;
			
			}
		}
		
		System.arraycopy(newframe, 0, oldframe, 0, oldframe.length);
		
		PGraphics g = buff.getDisplay();
		g.image(img, 0, 0);
		
		cnt++;
		return buff;
	}
	
	
	public void initialize() {
		/*
		oldframe[width + 3] = 1;
		oldframe[2*width+3] = 1;
		oldframe[3*width+3] = 1;
		*/
		// ~ 5% initial population
		
		int num = ((width * height) / 100) * 10;
		int max = (width * height) -1;
		Random generator = new Random();
		for(int i=0; i<num; i++) {
			int rand = generator.nextInt(max);
			oldframe[rand] = 1;
		}
		
	}
	
	
	public int getLivingNeighbours(int index) {
		int ncount=0;
		// up
		int nu = getPrevNeighborVert(index);
		if (isLiving(nu)) {
			ncount++;
		}
		// upleft
		if (isLiving(getPrevNeighborHoriz(nu))) {
			ncount++;
		}
		// upright
		if (isLiving(getNextNeighborHoriz(nu))) {
			ncount++;
		}
		// down
		int nd = getNextNeighborVert(index);
		if (isLiving(nd)) {
			ncount++;
		}
		// downleft
		if (isLiving(getPrevNeighborHoriz(nd))) {
			ncount++;
		}
		// downright
		if (isLiving(getNextNeighborHoriz(nd))) {
			ncount++;
		}
		// left
		if (isLiving(getPrevNeighborHoriz(index))) {
			ncount++;
		}
		// right
		if (isLiving(getNextNeighborHoriz(index))) {
			ncount++;
		}
		return ncount;
	}
	
	public boolean isLiving(int index) {
		if (index < 0) {
			return false;
		}
		return (oldframe[index] == 1);
	}
	
	public int getNextNeighborVert(int index) {
		int pos = index + width;
		if (pos >= (width * height)) {
			pos = (index % width);
		}
		return pos;
  	}
	
	public int getPrevNeighborVert(int index) {
		int pos = index - width;
		if (pos < 0) {
			pos = ((width*height)) + pos;
		}
		return pos;
	}
	
	
	public int getNextNeighborHoriz(int index) {
		int num = (index % width);
		if (num == (width -1)) {
			double lines = Math.floor(index / width);
			return (int) lines + 1;
		}
		return index + 1;
	}
	
	
	public int getPrevNeighborHoriz(int index) {
		int num = (index % width);
		if (num == 0) {
			double lines = Math.floor(index / width);
			return (int) lines + (width -1);
		}
		return index -1;
	}
	
	
	
}
