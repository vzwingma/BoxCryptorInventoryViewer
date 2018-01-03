package com.terrier.utilities.automation.bundles.boxcryptor.objects;

import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;

/**
 * Inventaire
 * @author vzwingma
 *
 */
public abstract class AbstractBCInventaireStructure {


	private String _nomFichierChiffre;
	private InventoryFileStatutObject statutFichierChiffre = new InventoryFileStatutObject(null, InventoryFileStatutEnum.INCONNU);
	private String _nomFichierClair;
	private InventoryFileStatutObject statutFichierClair = new InventoryFileStatutObject(null, InventoryFileStatutEnum.INCONNU);

	/**
	 * Constructeur YML
	 */
	public AbstractBCInventaireStructure(){ }
	
	public AbstractBCInventaireStructure(String nomFichierChiffre, String nomFichierClair){
		this._nomFichierChiffre = nomFichierChiffre;
		this._nomFichierClair = nomFichierClair;
	}


	/**
	 * @return the nomFichierChiffre
	 */
	public String get_NomFichierChiffre() {
		return _nomFichierChiffre;
	}

	/**
	 * @param nomFichierChiffre the nomFichierChiffre to set
	 */
	public void set_NomFichierChiffre(String nomFichierChiffre) {
		this._nomFichierChiffre = nomFichierChiffre;
	}

	/**
	 * @return the nomFichierClair
	 */
	public String get_NomFichierClair() {
		return _nomFichierClair;
	}

	/**
	 * @param nomFichierClair the nomFichierClair to set
	 */
	public void set_NomFichierClair(String nomFichierClair) {
		this._nomFichierClair = nomFichierClair;
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
