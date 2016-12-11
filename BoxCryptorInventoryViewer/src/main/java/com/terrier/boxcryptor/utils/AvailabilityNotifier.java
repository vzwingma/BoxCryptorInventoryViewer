/**
 * 
 */
package com.terrier.boxcryptor.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Notifier de changement de disponibilité
 * @author vzwingma
 *
 */
public abstract class AvailabilityNotifier {

	private static final List<AvailabilityListener> availabilityListeners = new ArrayList<AvailabilityListener>();

	
	public static void register(AvailabilityListener listener){
		availabilityListeners.add(listener);
	}
	
	public static void unregister(AvailabilityListener listener){
		availabilityListeners.remove(listener);
	}
	
	
	public static void notifyAvailabilityUpdate(){
		for (AvailabilityListener availabilityListener : availabilityListeners) {
			availabilityListener.itemAvailabilityUpdated();
		}
	}
}
