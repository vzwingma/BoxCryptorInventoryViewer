package com.terrier.boxcryptor.service.available.hubic;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.service.available.hubic.objects.HubicAPIContent;
import com.terrier.boxcryptor.service.available.local.AvailabilityNotifier;
import com.terrier.boxcryptor.utils.AbstractHTTPClient;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireFichier;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireRepertoire;

public class CheckHubicAvailabilityFileRunnable extends AbstractHTTPClient implements Runnable {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckHubicAvailabilityFileRunnable.class);

	private ThreadPoolExecutor threadsAvailability;
	
	// Items d'inventaires
	private AbstractBCInventaireStructure inventaireStructure;
	
	private List<HubicAPIContent> contents;



	/**
	 * @param inventaireStructure
	 * @param contents
	 * @param threadsAvailability
	 */
	public CheckHubicAvailabilityFileRunnable(AbstractBCInventaireStructure inventaireStructure, List<HubicAPIContent> contents, ThreadPoolExecutor threadsAvailability){
		this.inventaireStructure = inventaireStructure;
		this.threadsAvailability = threadsAvailability;
		this.contents = contents;
	}


	@Override
	public void run() {
		updateAvailability(this.inventaireStructure, this.contents);
		AvailabilityNotifier.notifyOnlineAvailabilityUpdate(100 * (this.threadsAvailability.getPoolSize() - this.threadsAvailability.getActiveCount() + 1) / this.threadsAvailability.getPoolSize());
	}


	/**
	 * Disponibilité en local
	 * @param inventaireStructure répertoire
	 * @param rootRepertoire répertoire parent
	 */
	private void updateAvailability(AbstractBCInventaireStructure inventaireStructure, List<HubicAPIContent> contents){

		boolean found = rechercheOnlineAvailability(inventaireStructure.get_NomFichierChiffre(), contents);
		LOGGER.info("Recherche de {} ({}) dans HUBIC : {}", inventaireStructure.get_NomFichierChiffre(), inventaireStructure.get_NomFichierClair(), found);
		inventaireStructure.setStatutFichierChiffre(new InventoryFileStatutObject(null, found ? InventoryFileStatutEnum.DISPONIBLE : InventoryFileStatutEnum.INDISPONIBLE));

		if(inventaireStructure instanceof BCInventaireRepertoire){
			BCInventaireRepertoire repertoire = (BCInventaireRepertoire)inventaireStructure;
			for (BCInventaireFichier fichier : repertoire.getMapInventaireFichiers().values()) {
				this.threadsAvailability.submit(new CheckHubicAvailabilityFileRunnable(fichier, contents, threadsAvailability));
			}
			for (BCInventaireRepertoire ssRepertoire : repertoire.getMapInventaireSousRepertoires().values()) {
				this.threadsAvailability.submit(new CheckHubicAvailabilityFileRunnable(ssRepertoire, contents, threadsAvailability));
			}
		}
	}

	/**
	 * @param nomFichierChiffre
	 * @param contents
	 * @return dispo dans HUBIC
	 */
	private boolean rechercheOnlineAvailability(String nomFichierChiffre, List<HubicAPIContent> contents){
		for (HubicAPIContent hubicAPIContent : contents) {
			if(hubicAPIContent.getName() != null && hubicAPIContent.getName().contains(nomFichierChiffre)){
				return true;
			}
		}
		return false;
	}
}
