package com.terrier.boxcryptor.generate;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.terrier.boxcryptor.objects.BCInventaireRepertoire;
import com.terrier.boxcryptor.utils.Utils;

/**
 * Main class of BoxCryptor Inventory Generator
 * @author vzwingma
 *
 */
public class BCInventoryGenerator {

	// Répertoire chiffré
	private File repertoireChiffre;
	// Répertoire non chiffré
	private File repertoireNonChiffre;

	private Calendar startTraitement = Calendar.getInstance();




	/**
	 * Start inventory
	 * @param args directories parameters
	 * @throws Exception error during generation
	 */
	public void startInventory(String cheminRepertoireChiffre, String cheminRepertoireNonChiffre) throws Exception{
		System.out.println("Début de la génération de l'inventaire");

		repertoireChiffre = new File(cheminRepertoireChiffre);
		repertoireNonChiffre = new File(cheminRepertoireNonChiffre);
		generateInventory();
	}



	/**
	 * Inventory generator
	 * @throws Exception  error during generation
	 */
	private void generateInventory() throws Exception{

		// Lecture de l'inventaire
		BCInventaireRepertoire inventaire = loadFileInventory();

		Utils.printDelayFromBeginning("Read file Inventory", this.startTraitement);

		// Création de l'inventaire
		ExecutorService threadsPool = Executors.newFixedThreadPool(100);
		DirectoryInventoryStreamGeneratorCallable inventory = new DirectoryInventoryStreamGeneratorCallable(
				threadsPool,
				this.repertoireNonChiffre.getName(),
				inventaire,
				this.repertoireChiffre.getAbsolutePath(), this.repertoireNonChiffre.getAbsolutePath());
		BCInventaireRepertoire inventaireNew = inventory.call();
		threadsPool.shutdown();
		Utils.printDelayFromBeginning("Generate Inventory", this.startTraitement);


		// Ecriture de l'inventaire
		Utils.dumpYMLInventory(this.repertoireNonChiffre, inventaireNew);
		Utils.printDelayFromBeginning("Dump Inventory", this.startTraitement);
	}

	/**
	 * Lecture de l'inventaire existant pour mise à jour
	 * @throws IOException
	 */
	public BCInventaireRepertoire loadFileInventory() throws IOException{
		// This will output the full path where the file will be written to...
		File inventoryFile = new File(repertoireNonChiffre, Utils.INVENTORY_FILENAME);
		BCInventaireRepertoire repertoire;
		if(inventoryFile.exists()){
			System.out.println("Enregistrement de la liste dans " + inventoryFile.getCanonicalPath());
			repertoire = Utils.loadYMLInventory(repertoireNonChiffre.getAbsolutePath());
		}
		else{
			System.out.println("Le fichier "+ inventoryFile.getAbsolutePath()+ " n'existe pas. Création du fichier");
			repertoire  = new BCInventaireRepertoire(repertoireChiffre.getName(), repertoireNonChiffre.getName());
		}
		return repertoire;
	}




}
