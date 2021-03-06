package com.terrier.boxcryptor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.terrier.boxcryptor.utils.yaml.BCInventaireYmlRepresenter;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireRepertoire;

/**
 * Utils class
 * @author vzwingma
 *
 */
public class BCUtils {
	

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BCUtils.class);

	// Inventory filename
	public static final String INVENTORY_FILENAME = "liste_Fichiers_BoxCryptor.yml";
	
	// Regex to split search values
	protected static final String SPLIT_REGEX = "[ _-]";
	
	// Constructeur privé
	private BCUtils(){}
	
	/**
	 * Print delay from startTraitementCal
	 * @param treatementName  name of treatment
	 * @param startTraitementCal start time of Treatment
	 */
	public static void printDelayFromBeginning(String treatementName, Calendar startTraitementCal){
		LOGGER.info("[{}] > {} ms", treatementName, (Calendar.getInstance().getTimeInMillis() - startTraitementCal.getTimeInMillis()));
	}
	
	
	
	/**
	 * Ecriture de l'inventaire (dump)
	 * @param inventaireR inventaire
	 * @throws IOException
	 */
	public static void dumpYMLInventory(final File repertoire, final BCInventaireRepertoire inventaireR) throws IOException{

		Yaml yml = new Yaml(new BCInventaireYmlRepresenter());
		File inventoryFile = new File(repertoire.getAbsolutePath(), BCUtils.INVENTORY_FILENAME);
		if(!inventoryFile.exists()){
			boolean c = inventoryFile.createNewFile();
			LOGGER.info("Le fichier n'existe pas. Création {} du fichier {}", c, inventoryFile.getAbsolutePath());
		}
		
		FileWriter inventoryWriter = new FileWriter(inventoryFile);
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
			File inventoryFile = new File(repertoire, BCUtils.INVENTORY_FILENAME);
			if(inventoryFile.exists()){
				LOGGER.info("Chargement de l'inventaire depuis {}", inventoryFile.getCanonicalPath());
				Yaml yml = new Yaml(new BCInventaireYmlRepresenter());
				return yml.loadAs(new FileInputStream(inventoryFile), BCInventaireRepertoire.class);
			}
		}
		return null;
	}
	
	
	
	/**
	 * Search method of termes in inventory
	 * @param inventoryItem inventory item (directory or file)
	 * @param searchValue search value : 
	 * 	if null return true
	 * 	if spaces are present, searchValue is splitted in multiple terms
	 * return true if all terms are presents
	 */
	public static boolean searchTermsInInventory(AbstractBCInventaireStructure inventoryItem, String searchValue){
		if(searchValue == null || searchValue.isEmpty()){
			return true;
		}
		else{ 
			String[] allSearchValues = searchValue.split(SPLIT_REGEX);
			
			boolean found = true;
			for (String search : allSearchValues) {
				found &= inventoryItem.getNomFichierChiffre().toUpperCase().contains(search.toUpperCase())
						|| inventoryItem.getNomFichierClair().toUpperCase().contains(search.toUpperCase());
			}
			return found;
		}
	}
}
