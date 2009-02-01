package org.delta.transport;

import org.delta.circuit.ComponentGraph;

class CircuitTransport {
	DatagramLayer datagramLayer;
	
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
		int j = 0;
		
		
		while(remaningBytes > 0) {
			//create 50 bytes long payload
			byte[] datagramPayload = new byte[50];
			//split array into chunks of length 50.
			if(remainingBytes >= 50) {
				System.arraycopy(byteArray, i, datagramPayload, 0, 50);
				remainingBytes -= 50;
				i += 50;
			}
			else {
				System.arraycopy(byteArray, i, datagramPayload, 0, remainingBytes);
				//rest of byte array is 0.
				remainingBytes = 0;
			}
			//add datagram to send buffer
			datagramLayer.sendDatagram(new Datagram(datagramPayload));
		}
		
		//send datagrams
		datagramLayer.handler();
		
	}
	
	public ComponentGraph receive() {
		
	}
	
	
}