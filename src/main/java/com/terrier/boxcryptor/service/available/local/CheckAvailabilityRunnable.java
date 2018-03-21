/**
 * 
 */
package com.terrier.boxcryptor.service.available.local;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckAvailabilityRunnable.class);
	
	// Items d'inventaires
	private BCInventaireRepertoire inventoryItems;
	
	private String repertoireNonChiffre;

	private ThreadPoolExecutor threadsAvailability;
	/**
	 * @param inventoryItems
	 */
	public CheckAvailabilityRunnable(BCInventaireRepertoire inventoryItems, String repertoireNonChiffre, ThreadPoolExecutor threadsAvailability) {
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
		LOGGER.debug("Check de la disponbilité de {})", 
				this.inventoryItems.getNomFichierClair());
		AvailabilityNotifier.notifyAvailabilityUpdate(100 * (this.threadsAvailability.getPoolSize() - this.threadsAvailability.getActiveCount() + 1) / this.threadsAvailability.getPoolSize());
	}


	/**
	 * Disponibilité en local
	 * @param repertoire répertoire
	 * @param rootRepertoire répertoire parent
	 */
	private void updateAvailability(BCInventaireRepertoire repertoire, String rootRepertoire){
		
		repertoire.setStatutFichierClair(isFileAvailable(rootRepertoire, repertoire.getNomFichierClair()));
		
		for (BCInventaireFichier fichier : repertoire.getMapInventaireFichiers().values()) {
			fichier.setStatutFichierClair(isFileAvailable(rootRepertoire + "/" + repertoire.getNomFichierClair(), fichier.getNomFichierClair()));	
		}
		
		for (BCInventaireRepertoire ssRepertoire : repertoire.getMapInventaireSousRepertoires().values()) {
			this.threadsAvailability.submit(new CheckAvailabilityRunnable(ssRepertoire, rootRepertoire + "/" + repertoire.getNomFichierClair(), this.threadsAvailability));	
		}
	}
	
	
	/**
	 * @param path
	 * @param fileName
	 * @return si le fichier existe
	 */
	private InventoryFileStatutObject isFileAvailable(String path, String fileName){
		InventoryFileStatutObject statutObject = new InventoryFileStatutObject(FileSystems.getDefault().getPath(path, fileName), InventoryFileStatutEnum.INCONNU);
		if(Files.exists(statutObject.getCheminFichier(), LinkOption.NOFOLLOW_LINKS)){
			statutObject.setStatut(InventoryFileStatutEnum.DISPONIBLE);
		}
		else{
			statutObject.setStatut(InventoryFileStatutEnum.INDISPONIBLE);
		}
		return statutObject;
	}
}
