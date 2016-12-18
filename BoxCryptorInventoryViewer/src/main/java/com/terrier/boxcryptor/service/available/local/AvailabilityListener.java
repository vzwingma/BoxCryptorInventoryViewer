/**
 * 
 */
package com.terrier.boxcryptor.service.available.local;

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
	
	/**
	 * notification d'une mise à jour d'update 
	 * @param pourcentage pourcentage d'avancement
	 */
	public void itemOnlineAvailabilityUpdated(int pourcentage);
}
