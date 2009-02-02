
//package JFTD2XX;

import java.io.IOException;

/** Java FTD2XX class */
public class JFTD2XX {

	/* Device status */
	public static final int
		FT_OK = 0,
		FT_INVALID_HANDLE = 1,
		FT_DEVICE_NOT_FOUND = 2,
		FT_DEVICE_NOT_OPENED = 3,
		FT_IO_ERROR = 4,
		FT_INSUFFICIENT_RESOURCES = 5,
		FT_INVALID_PARAMETER = 6,
		FT_INVALID_BAUD_RATE = 7,
		FT_DEVICE_NOT_OPENED_FOR_ERASE = 8,
		FT_DEVICE_NOT_OPENED_FOR_WRITE = 9,
		FT_FAILED_TO_WRITE_DEVICE = 10,
		FT_EEPROM_READ_FAILED = 11,
		FT_EEPROM_WRITE_FAILED = 12,
		FT_EEPROM_ERASE_FAILED = 13,
		FT_EEPROM_NOT_PRESENT = 14,
		FT_EEPROM_NOT_PROGRAMMED = 15,
		FT_INVALID_ARGS = 16,
		FT_OTHER_ERROR = 17;


	/* FTD2XX API */

	/** Open device by number and associate it to this JFTD2XX object
		@param deviceNumber device enumeration
	*/
	public native void open(int deviceNumber) throws IOException;
	/** Close device
	*/
	public native void close() throws IOException;

	/** Read bytes from device
		@param bytes array to store read bytes
		@param offset begin index
		@param length amount of bytes desired
		@return number of bytes actually read
	*/
	public native int read(byte[] bytes, int offset, int length) throws IOException;
	/** Write bytes to device
		@param bytes array with bytes to be sent
		@param offset begin index
		@param length amount of bytes desired
		@return number of bytes actually written
	*/
	public native int write(byte[] bytes, int offset, int length) throws IOException;

	/** Get queue status
		@return number of characters in the receive queue
	*/
	public native int getQueueStatus() throws IOException;

	/** Set device latency timer
		@param time timer value in milliseconds (2-255)
	*/
	public native void setLatencyTimer(int time) throws IOException;


	/** Internal FT_HANDLE */
	protected int handle = -1;

	static {
		System.loadLibrary("jftd2xx");
	}

	/** Create a new unopened JFTD2XX object */
	public JFTD2XX() {
	}

	/** Create a new JFTD2XX object and open device by number */
	public JFTD2XX(int deviceNumber) throws IOException {
		open(deviceNumber);
	}

	protected void finalize()
	throws Throwable {
		try {
			// if (handle != 0) close();
			close();
		}
		finally {
			super.finalize();
		}
	}



}
