abstract class Datagram {
	
	byte[] payload = new byte[50];
	
	public Datagram(byte[] contents) {
		this.payload = contents;
	}
}