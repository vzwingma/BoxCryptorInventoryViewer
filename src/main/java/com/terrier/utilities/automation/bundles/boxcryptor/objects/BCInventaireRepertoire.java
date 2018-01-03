package com.terrier.utilities.automation.bundles.boxcryptor.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Class of a directory in inventory
 * @author vzwingma
 *
 */
public class BCInventaireRepertoire extends AbstractBCInventaireStructure {

	/**
	 * Constructeur pour YML
	 */
	public BCInventaireRepertoire(){ }
	
	/**
	 * Repertoire
	 * @param repertoireChiffre
	 * @param repertoireClair
	 */
	public BCInventaireRepertoire(String nomRepertoireChiffre, String nomRepertoireClair) {
		super(nomRepertoireChiffre, nomRepertoireClair);
	}

	/**
	 * Fichier dans le répertoire
	 */
	private Map<String, BCInventaireFichier> mapInventaireFichiers = new HashMap<String, BCInventaireFichier>();
	
	/**
	 * Sous répertoires dans le répertoire
	 */
	private Map<String, BCInventaireRepertoire> mapInventaireSousRepertoires = new HashMap<String, BCInventaireRepertoire>();
	
	private long dateModificationDernierInventaire;

	/**
	 * @return the mapInventaireFichiers
	 */
	public Map<String, BCInventaireFichier> getMapInventaireFichiers() {
		return mapInventaireFichiers;
	}

	/**
	 * @return the mapInventaireSousRepertoires
	 */
	public Map<String, BCInventaireRepertoire> getMapInventaireSousRepertoires() {
		return mapInventaireSousRepertoires;
	}

	/**
	 * @param mapInventaireFichiers the mapInventaireFichiers to set
	 */
	public void setMapInventaireFichiers(Map<String, BCInventaireFichier> mapInventaireFichiers) {
		this.mapInventaireFichiers = mapInventaireFichiers;
	}

	/**
	 * @param mapInventaireSousRepertoires the mapInventaireSousRepertoires to set
	 */
	public void setMapInventaireSousRepertoires(Map<String, BCInventaireRepertoire> mapInventaireSousRepertoires) {
		this.mapInventaireSousRepertoires = mapInventaireSousRepertoires;
	}

	/**
	 * @return the dateModificationDernierInventaire
	 */
	public Long getDateModificationDernierInventaire() {
		return dateModificationDernierInventaire;
	}

	/**
	 * @param dateModificationDernierInventaire the dateModificationDernierInventaire to set
	 */
	public void setDateModificationDernierInventaire(Long dateModificationDernierInventaire) {
		this.dateModificationDernierInventaire = dateModificationDernierInventaire;
	}
	
}
