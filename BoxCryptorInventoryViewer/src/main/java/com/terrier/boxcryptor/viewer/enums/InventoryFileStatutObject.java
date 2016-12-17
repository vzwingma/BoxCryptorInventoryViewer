/**
 * 
 */
package com.terrier.boxcryptor.viewer.enums;

import java.nio.file.Path;

/**
 * Objet statut utilisé pour la colonne statut 
 * @author vzwingma
 *
 */
public class InventoryFileStatutObject {

	// Chemin pour pouvoir y accéder
	private Path cheminFichier;
	
	// Statut
	private InventoryFileStatutEnum statut = InventoryFileStatutEnum.INCONNU;

	
	public InventoryFileStatutObject(Path cheminFichier, InventoryFileStatutEnum statut){
		this.cheminFichier = cheminFichier;
		this.statut = statut;
	}
	
	/**
	 * @return the cheminFichier
	 */
	public Path getCheminFichier() {
		return cheminFichier;
	}

	/**
	 * @return the statut
	 */
	public InventoryFileStatutEnum getStatut() {
		return statut;
	}

	/**
	 * @param statut the statut to set
	 */
	public void setStatut(InventoryFileStatutEnum statut) {
		this.statut = statut;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return InventoryFileStatutEnum.DISPONIBLE.equals(this.statut) ? " " : ""; 
	}
	
	/**
	 * @param valeur
	 * @return icon de l'objet
	 */
	public String getIcon(){
		String imagePath = "/images/";
		InventoryFileStatutEnum statut = InventoryFileStatutEnum.INCONNU;
		if(this == null || this.getStatut() == null){
			statut = InventoryFileStatutEnum.INDISPONIBLE;
		}
		else{
			statut = this.getStatut();
		}
		switch (statut) {
		case INCONNU:
			imagePath += "circle_ukn.png";
			break;
		case DISPONIBLE:
			imagePath += "circle_ok.png";
			break;
		case INDISPONIBLE:
			imagePath += "circle_ko.png";
			break;
		default:
			imagePath += "circle_ukn.png";
			break;
		}
		return imagePath;
	}
	
}
