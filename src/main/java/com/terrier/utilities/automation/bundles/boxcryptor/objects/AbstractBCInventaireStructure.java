package com.terrier.utilities.automation.bundles.boxcryptor.objects;

import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;

/**
 * Inventaire
 * @author vzwingma
 *
 */
public abstract class AbstractBCInventaireStructure {


	private String nomFichierChiffre;
	private InventoryFileStatutObject statutFichierChiffre = new InventoryFileStatutObject(null, InventoryFileStatutEnum.INCONNU);
	private String nomFichierClair;
	private InventoryFileStatutObject statutFichierClair = new InventoryFileStatutObject(null, InventoryFileStatutEnum.INCONNU);

	/**
	 * Constructeur YML
	 */
	public AbstractBCInventaireStructure(){ }
	
	public AbstractBCInventaireStructure(String nomFichierChiffre, String nomFichierClair){
		this.nomFichierChiffre = nomFichierChiffre;
		this.nomFichierClair = nomFichierClair;
	}


	/**
	 * @return the nomFichierChiffre
	 */
	public String getNomFichierChiffre() {
		return nomFichierChiffre;
	}

	/**
	 * @param nomFichierChiffre the nomFichierChiffre to set
	 */
	public void setNomFichierChiffre(String nomFichierChiffre) {
		this.nomFichierChiffre = nomFichierChiffre;
	}

	/**
	 * @return the nomFichierClair
	 */
	public String getNomFichierClair() {
		return nomFichierClair;
	}

	/**
	 * @param nomFichierClair the nomFichierClair to set
	 */
	public void setNomFichierClair(String nomFichierClair) {
		this.nomFichierClair = nomFichierClair;
	}

	/**
	 * @return the statutFichierChiffre
	 */
	public InventoryFileStatutObject getStatutFichierChiffre() {
		return statutFichierChiffre;
	}

	/**
	 * @param statutFichierChiffre the statutFichierChiffre to set
	 */
	public void setStatutFichierChiffre(InventoryFileStatutObject statutFichierChiffre) {
		this.statutFichierChiffre = statutFichierChiffre;
	}

	/**
	 * @return the statutFichierClair
	 */
	public InventoryFileStatutObject getStatutFichierClair() {
		return statutFichierClair;
	}

	/**
	 * @param statutFichierClair the statutFichierClair to set
	 */
	public void setStatutFichierClair(InventoryFileStatutObject statutFichierClair) {
		this.statutFichierClair = statutFichierClair;
	}

}
