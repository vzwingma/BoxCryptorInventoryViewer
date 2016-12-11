package com.terrier.utilities.automation.bundles.boxcryptor.objects;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inventaire
 * @author vzwingma
 *
 */
public abstract class AbstractBCInventaireStructure {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBCInventaireStructure.class);

	private String _nomFichierChiffre;
	private boolean statutFichierChiffre = false;
	private String _nomFichierClair;
	private boolean statutFichierClair = false;

	/**
	 * Constructeur YML
	 */
	public AbstractBCInventaireStructure(){ }
	
	public AbstractBCInventaireStructure(String nomFichierChiffre, String nomFichierClair){
		this._nomFichierChiffre = nomFichierChiffre;
		this._nomFichierClair = nomFichierClair;
	}

	/**
	 * @return la clé
	 */
	public String getCleMap(){
		return getCleMap(this._nomFichierClair);
	}
	
	/**
	 * @param fichierClair
	 * @return clé pour un répertoire
	 */
	public String getCleMap(String nomFichierClair){
		return getHashMD5(nomFichierClair);
	}


	/**
	 * @param nameFileOrDirectory
	 * @return Hash MD5
	 */
	private String getHashMD5(String nameFileOrDirectory){
		try {
			if(nameFileOrDirectory != null){
				String s = nameFileOrDirectory != null ? nameFileOrDirectory : null;
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(s.getBytes(),0,s.length());
				String signature = new BigInteger(1,md5.digest()).toString(16);
				return signature;
			}
			return null;

		} catch (final NoSuchAlgorithmException e) {
			LOGGER.error("Erreur lors du calcul du hash");
			e.printStackTrace();
			return null;
		}
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
	public boolean isStatutFichierChiffre() {
		return statutFichierChiffre;
	}

	/**
	 * @param statutFichierChiffre the statutFichierChiffre to set
	 */
	public void setStatutFichierChiffre(boolean statutFichierChiffre) {
		this.statutFichierChiffre = statutFichierChiffre;
	}

	/**
	 * @return the statutFichierClair
	 */
	public boolean isStatutFichierClair() {
		return statutFichierClair;
	}

	/**
	 * @param statutFichierClair the statutFichierClair to set
	 */
	public void setStatutFichierClair(boolean statutFichierClair) {
		this.statutFichierClair = statutFichierClair;
	}
}
