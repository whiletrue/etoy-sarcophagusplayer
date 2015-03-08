package com.etoy.processing.sarcophagus;

import processing.core.PApplet;
import processing.core.PGraphics;
import java.util.ArrayList;
import java.util.Random;

public class Flock {
	// The Flock (a list of Boid objects)
	
	protected Random rand;
	
	protected ArrayList<Boid> boids; // An arraylist for all the boids

	public Flock(PApplet ap) {
		
		boids = new ArrayList<Boid>(); // Initialize the arraylist
		rand = new Random();
	}
	  
	public void run(PGraphics canvas) {
		int shines[] = new int[0];
		int numShines = (int) rand.nextInt(5);
		
		if (numShines > 0) {
			shines= new int[numShines]; 
			for(int i=0; i<numShines; i++) {
				shines[i] = (int) rand.nextInt(boids.size());
			}
		}
		
		for (int i = 0; i < boids.size(); i++) {
	      Boid b = (Boid) boids.get(i);  
	      b.run(canvas, boids);  // Passing the entire list of boids to each boid individually
	    }
		
		
		for(int i=0; i<shines.length; i++) {
			Boid b = (Boid) boids.get(i);
			if (b instanceof Boid && !b.isShining) {
				b.setShining(true);
			}
		}
	}

	public void addBoid(Boid b) {
		boids.add(b);
	}

	

}
