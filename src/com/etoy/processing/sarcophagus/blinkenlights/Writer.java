package com.etoy.processing.sarcophagus.blinkenlights;

//import processing.core.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;



public class Writer {
	
	protected DatagramSocket socket;
	protected InetAddress address;
	protected int port;
	
	public Writer(String ip, int port) {
		try {
			System.out.println("address: " + InetAddress.getByName(ip));
			socket = new DatagramSocket();
			address = InetAddress.getByName(ip);
			this.port = port;
		} catch(Exception e) {
			System.out.println("Connect error: " + e.getMessage());
		}
		
	}
	
	
	public void writeFrame(Frame frame) {
		try {
			DatagramPacket packet = new DatagramPacket(frame.getMessage(), frame.getMessageLength(), address, port);
			socket.send(packet);
		} catch(Exception e) {
			System.out.println("Send error: "+e.getMessage());
		}
	}
	
}
