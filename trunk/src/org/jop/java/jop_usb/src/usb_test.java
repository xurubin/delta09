import com.jopdesign.io.HostDatagramLayer;
import jftd2xx.JFTD2XX;;

public class usb_test {

	public static void upload_test() {
		byte[] data = {0x26, 0x27, 0x26, 
					   (byte) 0x84, 0x00, 0x01, 0x00, 0x01,
					   0x1F};
		try {
			JFTD2XX usb = new JFTD2XX(0);
			usb.setLatencyTimer(5);

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
		
		HostDatagramLayer u = new HostDatagramLayer(-1);
		int c = 0;
		int i = 0;
		int LED;;
		while (true) {
			i++;
			//Compact bitwise representation for LED and switches
			LED = u.readSwitchStates();
			u.sendLEDStates((i&0xF)<<22 | LED);
			
			/*
			c++;
			if (c % 100 == 0)
				System.out.println(System.currentTimeMillis());
			*/
			System.out.printf(" %08x",LED);
			System.out.print('\r');
			
			
			
		}
	}

}
