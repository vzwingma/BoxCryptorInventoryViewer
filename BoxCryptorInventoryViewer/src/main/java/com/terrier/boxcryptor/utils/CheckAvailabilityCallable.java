/**
 * 
 */
package com.terrier.boxcryptor.utils;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.viewer.BCInventoryViewer;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireFichier;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireRepertoire;

/**
 * @author vzwingma
 *
 */
public class CheckAvailabilityCallable implements Callable<BCInventaireRepertoire> {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BCInventoryViewer.class);
	
	// Items d'inventaires
	private BCInventaireRepertoire inventoryItems;
	
	private String repertoireNonChiffre;

	/**
	 * @param inventoryItems
	 */
	public CheckAvailabilityCallable(BCInventaireRepertoire inventoryItems, String repertoireNonChiffre) {
		super();
		this.inventoryItems = inventoryItems;
		this.repertoireNonChiffre = repertoireNonChiffre;
	}




	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public BCInventaireRepertoire call() throws Exception {
		updateAvailability(this.inventoryItems, repertoireNonChiffre);
		return this.inventoryItems;
	}


	/**
	 * Disponibilité en local
	 * @param repertoire répertoire
	 * @param rootRepertoire répertoire parent
	 */
	private void updateAvailability(BCInventaireRepertoire repertoire, String rootRepertoire){
		
		repertoire.setStatutFichierClair(isFileAvailable(rootRepertoire, repertoire.get_NomFichierClair()));
		
		for (BCInventaireFichier fichier : repertoire.getMapInventaireFichiers().values()) {
			fichier.setStatutFichierClair(isFileAvailable(rootRepertoire + "/" + repertoire.get_NomFichierClair(), fichier.get_NomFichierClair()));	
		}
		
		for (BCInventaireRepertoire ssRepertoire : repertoire.getMapInventaireSousRepertoires().values()) {
			updateAvailability(ssRepertoire, rootRepertoire + "/" + repertoire.get_NomFichierClair());	
		}
	}
	
	
	/**
	 * @param path
	 * @param fileName
	 * @return si le fichier existe
	 */
	private boolean isFileAvailable(String path, String fileName){
		Path fichier = FileSystems.getDefault().getPath(path, fileName);
		boolean available = Files.exists(fichier);
		LOGGER.debug("Check local availability de {} : {}", fichier, available);
		return available;
	}
}
