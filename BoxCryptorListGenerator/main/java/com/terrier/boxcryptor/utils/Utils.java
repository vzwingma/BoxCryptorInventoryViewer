package com.terrier.boxcryptor.utils;

import java.util.Calendar;

/**
 * Utils class
 * @author vzwingma
 *
 */
public class Utils {
	
	

	public static final String INVENTORY_FILENAME = "liste_Fichiers_BoxCryptor.yml";
	
	
	/**
	 * Print delay from startTraitementCal
	 * @param treatementName  name of treatment
	 * @param startTraitementCal start time of Treatment
	 */
	public static void printDelayFromBeginning(String treatementName, Calendar startTraitementCal){
		System.out.println("["+treatementName+"] > " + (Calendar.getInstance().getTimeInMillis() - startTraitementCal.getTimeInMillis())  + " ms");
	}
}
