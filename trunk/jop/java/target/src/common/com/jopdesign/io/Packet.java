
package com.jopdesign.io;

public class Packet{
	static private int[] BYTE_MASK={0xFF, 0xFF00, 0xFF0000, 0xFF000000};
	static private int MAX_BUF_LEN = 32; //16 DWORDS, must be greater than MAX_PACKET_LEN/4
	static private int MAX_PACKET_LEN = 126;
	private int count;
	public int[] buf;
	private byte iteratedChecksum;
	
	//Packed byte array accessor
	private byte getByte(int index) {
		return(byte) ( (buf[index / 4] & BYTE_MASK[index % 4]) >> ((index % 4)*8)  );
	}
	
	private void setByte(int index, byte data) {
		//if (index / 4 >= MAX_BUF_LEN) return;
		buf[index / 4] &=  ~BYTE_MASK[index % 4];
		buf[index / 4] |= (data<<((index % 4)*8));
	}

	//Integrity checking routines.
	public boolean isValid() {
		return (count > 1) && checkChecksum();
	}
	
	private byte calcChecksum() {
		return iteratedChecksum;
	}
	private boolean checkChecksum() {
		return true;
		//if (count == 0) return false;
		//return getByte[count-1] == calcChecksum();
	}
	
	
	//Packet Data access methods
	public int getCount() {
		return (count > 0)? count - 1 : 0;
	}
	public byte getData(int index) {
		if (index > count - 1)
			return (byte) 0xFF;
		return getByte(index);
	}
	
	//Packet Construction methods
	public void clear() {
		count = 0;iteratedChecksum = 34;
	}
	
	public boolean appendByte_raw(byte data) {
		if (count == MAX_PACKET_LEN) return false;
		setByte(count++, data);
		iteratedChecksum += (data ^ count);
		return true;
	}
	
	public boolean appendChecksum() {
		return appendByte_raw(calcChecksum());
	}
	
	public void copyFrom(Packet p) {
		count = p.count;
		for(int i=0;i<(count+3+1)/4;i++) //Don't forget the checksum
			buf[i] = p.buf[i];
		iteratedChecksum = p.iteratedChecksum;
	}
	public Packet() {
		buf = new int[MAX_BUF_LEN];
		clear();
	}
}