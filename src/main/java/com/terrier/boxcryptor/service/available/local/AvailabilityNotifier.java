/**
 * 
 */
package com.terrier.boxcryptor.service.available.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Notifier de changement de disponibilité
 * @author vzwingma
 *
 */
public abstract class AvailabilityNotifier {

	/**
	 * Listement de la disponibilité
	 */
	private static final List<AvailabilityListener> availabilityListeners = new ArrayList<>();

	// Constructeur privé
	private AvailabilityNotifier(){}
	
	/**
	 * Enregistrement des listeners
	 * @param listener
	 */
	public static void register(AvailabilityListener listener){
		availabilityListeners.add(listener);
	}
	
	/**
	 * Unenregistrement des listeners
	 * @param listener
	 */
	public static void unregister(AvailabilityListener listener){
		availabilityListeners.remove(listener);
	}
	
	
	/**
	 * Notification de la disponibilité
	 * @param pourcentageAvancement pourcentage
	 */
	public static void notifyAvailabilityUpdate(int pourcentageAvancement){
		for (AvailabilityListener availabilityListener : availabilityListeners) {
			availabilityListener.itemAvailabilityUpdated(pourcentageAvancement);
		}
	}
	
	/**
	 * Notification de la disponibilité
	 * @param pourcentageAvancement pourcentage
	 */
	public static void notifyOnlineAvailabilityUpdate(int pourcentageAvancement){
		for (AvailabilityListener availabilityListener : availabilityListeners) {
			availabilityListener.itemOnlineAvailabilityUpdated(pourcentageAvancement);
		}
	}
}
