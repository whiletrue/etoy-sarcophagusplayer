package com.etoy.processing.sarcophagus;

import java.util.*;


public class ElementSet {
	
	protected LinkedList<Element> elements;
	protected long start, now, elapsed;
	protected int duration;
	
	
	public ElementSet() {
		elements = new LinkedList<Element>();
		start = 0L;
		duration = 0;
	}
	
	public void resetTime() {
		start = getTimeNow();
	}
	
	public void reset() {
		resetTime();
		setDuration(getDuration());
	}
	
	public void resetTime(long now) {
		start = now;
	}
	
	public long getTimeNow() {
		return (System.currentTimeMillis() / 1000);
	}
	
	public long getTimeElapsed(long now) {
		return (now - start);
	}
	
	public boolean isElapsed(long now) {
		if (now >= duration) {
			return true;
		}
		return false;
	}
	
	public int getDuration() {
		int d = 0;
		for(Iterator<Element> itr = elements.iterator(); itr.hasNext();) {
			Element elem = (Element) itr.next();
			int ed = elem.getTimeEnd();
			if (ed > d) {
				d = ed;
			}
		}
		return d;
	}
	
	public void setDuration(int dur) {
		duration = dur;
	}
	
	public void add(Element el) {
		elements.add(el);
	}
	
	public boolean elementInTime(Element elem, long now) {
		if (now >= elem.getTimeOffset() && now <= elem.getTimeEnd()) {
			return true;
		}
		return false;
	}
	
	public boolean elementIsStart(Element elem, long now) {
		if (now == elem.getTimeOffset()) {
			return true;
		}
		return false;
	}
	
	public boolean elementIsStop(Element elem, long now) {
		if (now == elem.getTimeEnd()) {
			return true;
		}
		return false;
	}
	
	public DisplayBuffer draw(DisplayBuffer buff) {
		now = getTimeNow();
		
		elapsed = getTimeElapsed(now);
		int ed = 0;
		for(Iterator<Element> itr = elements.iterator(); itr.hasNext();) {
			Element elem = (Element) itr.next();
			//System.out.println("draw: " + elem.getName());
			if (!elementInTime(elem, elapsed)) {
				continue;
			}
			
			if (elementIsStart(elem, elapsed)) {
				elem.start();
			}
			
			//System.out.println("ok ..");
			buff = elem.draw(buff);
			ed = elem.getTimeEnd();
			if (ed > duration) {
				setDuration(ed);
			}
			
			if (elementIsStop(elem, elapsed)) {
				elem.stop();
			}
			
		}
		
		if (isElapsed(elapsed)) {
			System.out.println("reset playlist " + elapsed);
			resetTime(now);
		}
		
		return buff;
	}
	
	
}
