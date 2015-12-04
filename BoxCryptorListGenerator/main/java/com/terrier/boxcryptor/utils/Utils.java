package com.terrier.boxcryptor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.yaml.snakeyaml.Yaml;

import com.terrier.boxcryptor.objects.BCInventaireRepertoire;

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
	
	
	
	/**
	 * Ecriture de l'inventaire (dump)
	 * @param inventaireR inventaire
	 * @throws IOException
	 */
	public static void dumpYMLInventory(final File repertoire, final BCInventaireRepertoire inventaireR) throws IOException{

		Yaml yml = new Yaml();
		FileWriter inventoryWriter = new FileWriter(new File(repertoire.getAbsolutePath(), Utils.INVENTORY_FILENAME));
		yml.dump(inventaireR, inventoryWriter);
		inventoryWriter.flush();
		inventoryWriter.close();
	}
	/**
	 * Load of inventory file
	 * @throws IOException error during loading
	 */
	public static BCInventaireRepertoire loadYMLInventory(String repertoire) throws IOException{
		if(repertoire != null){
			// This will output the full path where the file will be written to...
			File inventoryFile = new File(repertoire, Utils.INVENTORY_FILENAME);
			if(inventoryFile.exists()){
				System.out.println("Chargement de l'inventaire depuis " + inventoryFile.getCanonicalPath());
				Yaml yml = new Yaml();
				return yml.loadAs(new FileInputStream(inventoryFile), BCInventaireRepertoire.class);
			}
		}
		return null;
	}
}
