/**
 * 
 */
package com.terrier.boxcryptor.utils;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.viewer.BCInventoryViewer;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireFichier;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireRepertoire;

/**
 * @author vzwingma
 *
 */
public class CheckAvailabilityRunnable implements Runnable {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BCInventoryViewer.class);
	
	// Items d'inventaires
	private BCInventaireRepertoire inventoryItems;
	
	private String repertoireNonChiffre;

	private ExecutorService threadsAvailability;
	/**
	 * @param inventoryItems
	 */
	public CheckAvailabilityRunnable(BCInventaireRepertoire inventoryItems, String repertoireNonChiffre, ExecutorService threadsAvailability) {
		super();
		this.inventoryItems = inventoryItems;
		this.repertoireNonChiffre = repertoireNonChiffre;
		this.threadsAvailability = threadsAvailability;
	}




	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public void run() {
		updateAvailability(this.inventoryItems, repertoireNonChiffre);
		LOGGER.debug("Check de la disponbilité de {}", this.inventoryItems.get_NomFichierClair());
		AvailabilityNotifier.notifyAvailabilityUpdate();
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
			this.threadsAvailability.submit(new CheckAvailabilityRunnable(ssRepertoire, rootRepertoire + "/" + repertoire.get_NomFichierClair(), this.threadsAvailability));	
		}
	}
	
	
	/**
	 * @param path
	 * @param fileName
	 * @return si le fichier existe
	 */
	private InventoryFileStatutEnum isFileAvailable(String path, String fileName){
		//LOGGER.debug("Check local availability de {} : {}", fichier, available);
		if(Files.exists(FileSystems.getDefault().getPath(path, fileName), LinkOption.NOFOLLOW_LINKS)){
			return InventoryFileStatutEnum.DISPONIBLE;
		}
		else{
			return InventoryFileStatutEnum.INDISPONIBLE;
		}
	}
}
