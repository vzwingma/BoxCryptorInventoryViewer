package com.terrier.boxcryptor.objects;

/**
 * Repr�sente un couple fichier clair/chiffr�
 * @author vzwingma
 *
 */
public class BCInventaireFichier extends AbstractBCInventaireStructure {

	/**
	 * Constructeur YML
	 */
	public BCInventaireFichier(){}
	
	/**
	 * Constructeur
	 * @param nomFichierChiffre fichier chiffr�
	 * @param nomFichierClair fichier en clair
	 */
	public BCInventaireFichier(String nomFichierChiffre, String nomFichierClair) {
		super(nomFichierChiffre, nomFichierClair);
	}
}
