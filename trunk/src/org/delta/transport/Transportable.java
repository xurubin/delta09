package org.delta.transport;

interface Transportable<T> {

/*
 * The aim here is to have that unserialize(a.serialize()) is equivalent to a.  
*/
	
	/*
	 * Returns a serialized string of the form "ClassName(param1, param2, param3, ...)" that the object could be regenerated from.
	 * @return	Serialized object in string form. 
	*/
	public String serialize();
	
	
	/*
	 * Returns an object of type T that has been unserialized from a string that has been encoded with the serialize method.
	 * @param	encodedObject	Object in string form.
	 * @return	Object of type T
	*/
	public static T unserialize(String encodedObject);
	
}