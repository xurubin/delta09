/*
	Interface Class for DE2 Peripherals
	
*/

package com.jopdesign.io;
import com.jopdesign.sys.*;

public final class DE2Peripheral extends HardwareObject {
	static final int[] BitMask = {0x1,       0x2,       0x4,       0x8,
								  0x10,      0x20,      0x40,      0x80,
								  0x100,     0x200,     0x400,     0x800,
								  0x1000,    0x2000,    0x4000,    0x8000,  
								  0x10000,   0x20000,   0x40000,   0x80000,
								  0x100000,  0x200000,  0x400000,  0x800000,
								  0x1000000, 0x2000000, 0x4000000, 0x8000000,
								  0x10000000,0x20000000,0x40000000,0x80000000 };
	
	static public int directSwitchState() {
		int Switch;
		return Native.rdMem(Const.IO_DE2_SWITCH);
	}

	/**
		Valid Switch index value: 0..17
	*/
	static public boolean getSwitchState(int index) {
		int SwitchState = Native.rdMem(Const.IO_DE2_SWITCH);
		return ((SwitchState & BitMask[index]) != 0);
	}

	/**
		Valid Key index value: 0..3
	*/
	static public boolean getKeyState(int index) {
		int SwitchState = Native.rdMem(Const.IO_DE2_SWITCH);
		return ((SwitchState & BitMask[index+18]) != 0);
	}
	
	static public void directLEDState(int data) {
		Native.wrMem(data, Const.IO_DE2_LED);
	}	
	/**
		Valid LED_Red index value: 0..17
	*/
	static public boolean getLEDRState(int index) {
		int LEDSState = Native.rdMem(Const.IO_DE2_LED);
		return ((LEDSState & BitMask[index]) != 0);
	}

	/**
		Valid LED_Green index value: 0..7
	*/
	static public boolean getLEDGState(int index) {
		int LEDSState = Native.rdMem(Const.IO_DE2_LED);
		return ((LEDSState & BitMask[index+18]) != 0);
	}
	

	/**
		Valid LED_Red index value: 0..17
	*/
	static public void setLEDRState(int index, boolean On) {
		int LEDState = Native.rdMem(Const.IO_DE2_LED);
		if (On)
			LEDState |= BitMask[index];
		else
			LEDState &= (~BitMask[index]);
		Native.wrMem(LEDState, Const.IO_DE2_LED);
	}

	/**
		Valid LED_Green index value: 0..7
	*/
	static public void setLEDGState(int index, boolean On) {
		int LEDState = Native.rdMem(Const.IO_DE2_LED);
		if (On)
			LEDState |= BitMask[index+18];
		else
			LEDState &= (~BitMask[index+18]);
		Native.wrMem(LEDState, Const.IO_DE2_LED);
	}
	
	static private int intToHexPattern(int Digit){
		switch(Digit) {
			case  0: return 0x3F;//011 1111
			case  1: return 0x06;//000 0110
			case  2: return 0x5B;//101 1011
			case  3: return 0x4F;//100 1111
			case  4: return 0x66;//110 0110
			case  5: return 0x6D;//110 1101
			case  6: return 0x7D;//111 1101
			case  7: return 0x07;//000 0111
			case  8: return 0x7F;//111 1111
			case  9: return 0x6F;//110 1111
			case 10: return 0x77;//111 0111
			case 11: return 0x7C;//111 1100
			case 12: return 0x39;//011 1001
			case 13: return 0x5E;//101 1110
			case 14: return 0x79;//111 1001
			case 15: return 0x71;//111 0001
			default: return 0x7F;//111 1111
		}
	}
	
	static private int intToHexSeg(int Num, boolean hex){
	  if (!hex)
		return (intToHexPattern( (Num/1000) % 10 ) << 21) |
			   (intToHexPattern( (Num/100) % 10 ) << 14) |
			   (intToHexPattern( (Num/10) % 10 ) << 7) |
			   (intToHexPattern(  Num % 10 ));
	   else
		return (intToHexPattern( (Num>>12) & 0xF ) << 21) |
			   (intToHexPattern( (Num>>8) & 0xF ) << 14) |
			   (intToHexPattern( (Num>>4) & 0xF ) << 7) |
			   (intToHexPattern(  Num & 0xF ));
	}
	
	static public void setHEX1Display(int Num, boolean hex) {
		Native.wrMem(intToHexSeg(Num, hex), Const.IO_DE2_HEX1);
	}
	static public void setHEX2Display(int Num, boolean hex) {
		Native.wrMem(intToHexSeg(Num, hex), Const.IO_DE2_HEX2);
	}
	static public void directHEX1State(int data) {
		Native.wrMem(data, Const.IO_DE2_HEX1);
	}	
	static public void directHEX2State(int data) {
		Native.wrMem(data, Const.IO_DE2_HEX2);
	}	

}