/**
 * 
 */
package com.terrier.boxcryptor.viewer.enums;

import java.nio.file.Path;

/**
 * @author vzwingma
 *
 */
public class InventoryFileStatutObject {

	
	private Path cheminFichier;
	
	private InventoryFileStatutEnum statut;

	
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
		return this.statut.equals(InventoryFileStatutEnum.DISPONIBLE) ? "Accéder" : ""; 
	}
	
	
	
}
