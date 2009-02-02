package org.delta.transport;

import org.delta.circuit.ComponentGraph;

class CircuitTransport {
	DatagramLayer datagramLayer;
	public static int MAX_PAYLOAD = 56; 
	
	public CircuitTransport(DatagramLayer d) {
		this.datagramLayer = d;
	}
	
	public int send(ComponentGraph cgraph) {
		//serialize circuit recursively starting at the ComponentGraph object.
		String serializedObject = cgraph.serialize();
		
		//convert serialization string into byte array. 
		byte[] byteArray = serializedObject.getBytes("US-ASCII");
				
		int remainingBytes = byteArray.length;
		int i = 0;
		
		while(remaningBytes > 0) {
			//create 50 bytes long payload
			byte[] datagramPayload = new byte[MAX_PAYLOAD];
			//split array into chunks of length MAX_PAYLOAD.
			if(remainingBytes >= MAX_PAYLOAD) {
				System.arraycopy(byteArray, i, datagramPayload, 0, MAX_PAYLOAD);
				remainingBytes -= MAX_PAYLOAD;
				i += MAX_PAYLOAD;
			}
			else {
				System.arraycopy(byteArray, i, datagramPayload, 0, remainingBytes);
				//rest of byte array is 0.
				remainingBytes = 0;
			}
			//add datagram to send buffer
			datagramLayer.sendDatagram(new Datagram(datagramPayload));
		}
		
		
	}
	
	public ComponentGraph receive() {
		
	}
	
	
}