/*

Adapted from Serial.java in ejip123 in the JoP Distribution


*/
package com.jopdesign.io;


public abstract class BaseDatagramLayer{
	protected final int PACKET_LEN = 56;//Determined by ShiftRegister in sc_jtag_uart.vhdl
	protected static final byte DATAGRAM_HEADER		= (byte)0xAA;
	protected static final byte DATAGRAM_LEN_HEADER = (byte)0xAE;
	protected static final byte DATAGRAM_END 		= (byte)0x02;
	protected static final byte DATAGRAM_ACK 		= (byte)0xAA;

	protected final Object monitor; //Synchronization

	public BaseDatagramLayer(){
		monitor = new Object();
	}


	//7 to 8 expansion
	protected static void BytesExpand(byte src[], int ofst_src, byte dst[], int ofst_dst) {
		dst[ofst_dst  ] = (byte)(                       src[ofst_src  ]<<0 );
		dst[ofst_dst+1] = (byte)(( src[ofst_src  ]&0xFF)>>>7 | src[ofst_src+1]<<1 );
		dst[ofst_dst+2] = (byte)(( src[ofst_src+1]&0xFF)>>>6 | src[ofst_src+2]<<2 );
		dst[ofst_dst+3] = (byte)(( src[ofst_src+2]&0xFF)>>>5 | src[ofst_src+3]<<3 );
		dst[ofst_dst+4] = (byte)(( src[ofst_src+3]&0xFF)>>>4 | src[ofst_src+4]<<4 );
		dst[ofst_dst+5] = (byte)(( src[ofst_src+4]&0xFF)>>>3 | src[ofst_src+5]<<5 );
		dst[ofst_dst+6] = (byte)(( src[ofst_src+5]&0xFF)>>>2 | src[ofst_src+6]<<6 );
		dst[ofst_dst+7] = (byte)(( src[ofst_src+6]&0xFF)>>>1  );
		for(int i=0;i<8;i++)
			dst[ofst_dst+i] = (byte)((dst[ofst_dst+i]<<1) | 1);
	}
	
	//8 to 7 shrink
	protected static void BytesShrink(byte src[], int ofst_src, byte dst[], int ofst_dst) {
		dst[ofst_dst  ] =(byte)( ((src[ofst_src  ]&0xFF)>>>1) | (((src[ofst_src+1]&0xFF)>>>1)<<7));
		dst[ofst_dst+1] =(byte)( ((src[ofst_src+1]&0xFF)>>>2) | (((src[ofst_src+2]&0xFF)>>>1)<<6));
		dst[ofst_dst+2] =(byte)( ((src[ofst_src+2]&0xFF)>>>3) | (((src[ofst_src+3]&0xFF)>>>1)<<5));
		dst[ofst_dst+3] =(byte)( ((src[ofst_src+3]&0xFF)>>>4) | (((src[ofst_src+4]&0xFF)>>>1)<<4));
		dst[ofst_dst+4] =(byte)( ((src[ofst_src+4]&0xFF)>>>5) | (((src[ofst_src+5]&0xFF)>>>1)<<3));
		dst[ofst_dst+5] =(byte)( ((src[ofst_src+5]&0xFF)>>>6) | (((src[ofst_src+6]&0xFF)>>>1)<<2));
		dst[ofst_dst+6] =(byte)( ((src[ofst_src+6]&0xFF)>>>7) | (((src[ofst_src+7]&0xFF)>>>1)<<1));
	}
	
	

	public static int serialiseIntArray(int array[], int len, byte out[]){
		if (out.length<len*4+4) return -1;
		out[0] = (byte)len;
		out[1] = (byte)(len>>8);
		out[2] = (byte)(len>>16);
		out[3] = (byte)(len>>24);
		for(int i=0;i<len;i++){
			out[i*4+4] = (byte)array[i];
			out[i*4+5] = (byte)(array[i]>>8);
			out[i*4+6] = (byte)(array[i]>>16);
			out[i*4+7] = (byte)(array[i]>>24);
		}		
		return len*4+4;
	}

	public static int[] deserialiseIntArray(byte d[]){
		int len = (d[0]&0xFF)+((d[1]&0xFF)<<8)+((d[2]&0xFF)<<16)+((d[3]&0xFF)<<24);
		int r[] = new int[len];
		for(int i=0;i<len;i++)
			r[i] = (d[i*4+4]&0xFF)+((d[i*4+5]&0xFF)<<8)+((d[i*4+6]&0xFF)<<16)+((d[i*4+7]&0xFF)<<24);
		return r;
	}
}
