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
	

	static public void directHEX1State(int data) {
		Native.wrMem(data, Const.IO_DE2_HEX1);
	}	
	static public void directHEX2State(int data) {
		Native.wrMem(data, Const.IO_DE2_HEX2);
	}	

}