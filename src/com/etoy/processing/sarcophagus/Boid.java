package com.etoy.processing.sarcophagus;

import processing.core.*;
import java.util.ArrayList;
import java.util.Random;

public class Boid {
	
	
	PVector loc;
	PVector vel;
	Random rand;
	
	protected int shineGray = 0;
	protected int shineCount = 0;
	public boolean isShining = false;
	
	  PVector acc;
	  float r;
	  float maxforce;    // Maximum steering force
	  float maxspeed;    // Maximum speed
	  
	  Boid(PVector l, float ms, float mf) {
		
		acc = new PVector(0,0);
	    rand = new Random();
	    
	    // >= -2 <= 2
	    int p1 = (-2 + rand.nextInt(4));
	    int p2 = (-2 + rand.nextInt(4));
	    vel = new PVector(p1, p2);
	    loc = l.get();
	    r = (float) 0.1;
	    maxspeed = ms;
	    maxforce = mf;
	    
	  }
	  
	  
	  void run(PGraphics canvas, ArrayList<Boid> boids) {
	    flock(boids);
	    update();
	    borders();
	    render(canvas);
	  }

	  // We accumulate a new acceleration each time based on three rules
	  void flock(ArrayList<Boid> boids) {
	    PVector sep = separate(boids);   // Separation
	    PVector ali = align(boids);      // Alignment
	    PVector coh = cohesion(boids);   // Cohesion
	    // Arbitrarily weight these forces
	    sep.mult((float) 1.5);
	    ali.mult((float) 1.0);
	    coh.mult((float) 1.0);
	    // Add the force vectors to acceleration
	    acc.add(sep);
	    acc.add(ali);
	    acc.add(coh);
	  }

	  // Method to update location
	  void update() {
	    // Update velocity
	    vel.add(acc);
	    // Limit speed
	    vel.limit(maxspeed);
	    loc.add(vel);
	    // Reset accelertion to 0 each cycle
	    acc.mult(0);
	  }

	  void seek(PVector target) {
	    acc.add(steer(target,false));
	  }

	  void arrive(PVector target) {
	    acc.add(steer(target,true));
	  }

	  // A method that calculates a steering vector towards a target
	  // Takes a second argument, if true, it slows down as it approaches the target
	  PVector steer(PVector target, boolean slowdown) {
	    PVector steer;  // The steering vector
	    PVector desired = PVector.sub(target,loc);  // A vector pointing from the location to the target
	    float d = desired.mag(); // Distance from the target is the magnitude of the vector
	    // If the distance is greater than 0, calc steering (otherwise return zero vector)
	    if (d > 0) {
	      // Normalize desired
	      desired.normalize();
	      // Two options for desired vector magnitude (1 -- based on distance, 2 -- maxspeed)
	      if ((slowdown) && (d < 100.0)) desired.mult((float) (maxspeed*(d/100.0)) ); // This damping is somewhat arbitrary
	      else desired.mult(maxspeed);
	      // Steering = Desired minus Velocity
	      steer = PVector.sub(desired,vel);
	      steer.limit(maxforce);  // Limit to maximum steering force
	    } 
	    else {
	      steer = new PVector(0,0);
	    }
	    return steer;
	  }

	  void render(PGraphics buff) {
	    // Draw a triangle rotated in the direction of velocity
	    float theta = vel.heading2D() + processing.core.PConstants.PI/2;
	   
	    buff.fill(200);
	    buff.stroke(255);
	    buff.pushMatrix();
	    buff.translate(loc.x,loc.y);
	    buff.rotate(theta);
	    buff.beginShape(processing.core.PConstants.TRIANGLES);
	    buff.vertex(0, -r*2);
	    buff.vertex(-r, r*2);
	    buff.vertex(r, r*2);
	    buff.endShape();
	    buff.popMatrix();
	    shine(buff);
	  }

	  
	  public void setShining(boolean val) {
		  isShining = val;
	  }
	  
	  public void shine(PGraphics buff) {
		  if (isShining) {
			  
			  if (shineCount < 15) {
				  shineGray+=18;
				  shineGray = (shineGray > 255) ? 255 : shineGray;
			  } else if (shineCount > 15 && shineCount < 45) {
				  shineGray=255;
			  } else {
				  shineGray = (shineGray > 18) ? (shineGray-18) :0; 
			  }
			  
			  buff.fill(shineGray);
			  buff.text("F71806059E4A2EC3",loc.x ,loc.y);
			  
			  shineCount+=1;
			  if (shineCount == 60) {
				  setShining(false);
				  shineCount=shineGray=0;
			  }
		  }
		  
	  }
	  
	  // Wraparound
	  void borders() {
	    if (loc.x < -r) loc.x = 224+r;
	    if (loc.y < -r) loc.y = 64+r;
	    if (loc.x > 224+r) loc.x = -r;
	    if (loc.y > 64+r) loc.y = -r;
	  }

	  // Separation
	  // Method checks for nearby boids and steers away
	  PVector separate (ArrayList<Boid> boids) {
	    float desiredseparation = (float) 5.0;
	    PVector steer = new PVector(0,0,0);
	    int count = 0;
	    // For every boid in the system, check if it's too close
	    for (int i = 0 ; i < boids.size(); i++) {
	      Boid other = (Boid) boids.get(i);
	      float d = PVector.dist(loc,other.loc);
	      // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
	      if ((d > 0) && (d < desiredseparation)) {
	        // Calculate vector pointing away from neighbor
	        PVector diff = PVector.sub(loc,other.loc);
	        diff.normalize();
	        diff.div(d);        // Weight by distance
	        steer.add(diff);
	        count++;            // Keep track of how many
	      }
	    }
	    // Average -- divide by how many
	    if (count > 0) {
	      steer.div((float)count);
	    }

	    // As long as the vector is greater than 0
	    if (steer.mag() > 0) {
	      // Implement Reynolds: Steering = Desired - Velocity
	      steer.normalize();
	      steer.mult(maxspeed);
	      steer.sub(vel);
	      steer.limit(maxforce);
	    }
	    return steer;
	  }

	  // Alignment
	  // For every nearby boid in the system, calculate the average velocity
	  PVector align (ArrayList<Boid> boids) {
	    float neighbordist = (float) 10.0;
	    PVector steer = new PVector(0,0,0);
	    int count = 0;
	    for (int i = 0 ; i < boids.size(); i++) {
	      Boid other = (Boid) boids.get(i);
	      float d = PVector.dist(loc,other.loc);
	      if ((d > 0) && (d < neighbordist)) {
	        steer.add(other.vel);
	        count++;
	      }
	    }
	    if (count > 0) {
	      steer.div((float)count);
	    }

	    // As long as the vector is greater than 0
	    if (steer.mag() > 0) {
	      // Implement Reynolds: Steering = Desired - Velocity
	      steer.normalize();
	      steer.mult(maxspeed);
	      steer.sub(vel);
	      steer.limit(maxforce);
	    }
	    return steer;
	  }

	  // Cohesion
	  // For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
	  PVector cohesion (ArrayList<Boid> boids) {
	    float neighbordist = (float) 8.0;
	    PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all locations
	    int count = 0;
	    for (int i = 0 ; i < boids.size(); i++) {
	      Boid other = (Boid) boids.get(i);
	      float d = loc.dist(other.loc);
	      if ((d > 0) && (d < neighbordist)) {
	        sum.add(other.loc); // Add location
	        count++;
	      }
	    }
	    if (count > 0) {
	      sum.div((float)count);
	      return steer(sum,false);  // Steer towards the location
	    }
	    return sum;
	  }

}
