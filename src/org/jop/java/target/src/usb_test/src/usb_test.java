
public class usb_test {

	public static void upload_test() {
		byte[] data = {0x26, 0x27, 0x26, 
					   (byte) 0x84, 0x00, 0x01, 0x00, 0x01,
					   0x1F};
		try {
			JFTD2XX usb = new JFTD2XX(0);
			usb.setLatencyTimer(2);

			usb.write(data, 0, 3);
			
			for (int i=0;i<100;i++)
				usb.write(data, 3, 5);
			Thread.sleep(200);
			usb.write(data, 8, 1);
			usb.close();

			System.out.println("Done");
		}catch (Exception e) {
			System.out.println(e);
		}
			
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
