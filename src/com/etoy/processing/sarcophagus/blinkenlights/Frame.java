package com.etoy.processing.sarcophagus.blinkenlights;

/**
 * Class to create and handle MCUF frames
 * 
 * 
 * MCUF - microcontroller unit frame protocol
 * The microcontroller unit frame protocol is used to transmit grayscale 
 * or colored Blinkenlights streams using UDP packets. Simply, a UDP packet is sent for every new frame:
 *
 * +-----------------------+
 * | magic                 |
 * | 0x23 0x54   0x26 0x66 |
 * +-----------+-----------+
 * | height    | width     |
 * | 0x00 0x14 | 0x00 0x1A |
 * +-----------+-----------+
 * | channels  | maxval    |
 * | 0x00 0x01 | 0x00 0xFF |
 * +-----------+-----------+
 * | pixels                |
 * | 0xFF 0xCC   0x99 0x66 |
 * | 0x33 0x00   0x00 0x00 |
 * | ...                   |
 * | 0x00 0x00   0x00 0x00 |
 * +-----------------------+
 *
 * magic: fixed value
 * height: the height of the image in pixels
 * width: the width of the image in pixels
 * channels: the number of channels of the image, 1 for grayscales, 3 for RGB
 * maxval: the maximum pixel value used in the pixel data
 * pixels: the value of the channels * width * height pixels
 *  - 0 for off, maxval for on, values between 0 and maxval for grayscales
 *  - from left to right, then from top to bottom (i.e. channels of top left pixel first, pixel right to it next, ...)
 * all values are in network byte order - big endian - highbyte first
 * 
 * @author silvanzurbruegg
 *
 */

public class Frame {
	
	protected byte message[];
	protected int width;
	protected int height;
	
	public Frame() {
	}
	
	
	public Frame(int w, int h) {
		setDimension(w, h);
	}
	
	
	public int getMessageLength() {
		return message.length;
	}
	
	
	public byte[] getMessage() {
		return message;
	}
	
	
	public void setMessage(byte[] m) {
		message = m;
	}
	
	
	public void setDimension(int w, int h) {
		width = w;
		height = h;
		setup(width, height);
	}
	
	
	public void update(int[] pixels) {
		if (width <= 0 || height <= 0) {
			return;
		}
		
		writeHeader();
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				int pos = ((y * width)+x);
				message[12+pos] = (byte) (pixels[pos] & 255);
			}
		}
	}
	
	
	public void setup(int w, int h) {
		message = new byte[12 + (w * h)];
	}
	
	
	protected void writeHeader() {
		if (message.length >= 12) {
			// id
			message[0]=0x23;
			message[1]=0x54;
			message[2]=0x26;
			message[3]=0x66;
			// height
			message[4]=0;
			message[5]=(byte) height;
			// width
			message[6]=0;
			message[7]=(byte) width;
			// channels
			message[8]=0;
			message[9]=1;
			// maxval
			message[10]=0;
			message[11]=(byte) 255;
		}
	}
	
	
	
	
	
}
