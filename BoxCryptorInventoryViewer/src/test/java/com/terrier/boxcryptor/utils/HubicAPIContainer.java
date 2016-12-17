/**
 * 
 */
package com.terrier.boxcryptor.utils;

/**
 * @author vzwingma
 *
 */
public class HubicAPIContainer {
	
	int count;

	long bytes;

	String name;

	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HubicAPIContainers [count=").append(count).append(", bytes=").append(bytes).append(", name=")
				.append(name).append("]");
		return builder.toString();
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the bytes
	 */
	public long getBytes() {
		return bytes;
	}

	/**
	 * @param bytes the bytes to set
	 */
	public void setBytes(long bytes) {
		this.bytes = bytes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
