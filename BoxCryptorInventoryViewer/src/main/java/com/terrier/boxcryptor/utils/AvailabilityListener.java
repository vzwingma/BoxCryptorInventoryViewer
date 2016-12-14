/**
 * 
 */
package com.terrier.boxcryptor.utils;

/**
 * @author vzwingma
 *
 */
public interface AvailabilityListener {
	/**
	 * notification d'une mise à jour d'update 
	 * @param pourcentage pourcentage d'avancement
	 */
	public void itemAvailabilityUpdated(int pourcentage);
}
