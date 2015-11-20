package com.terrier.boxcryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.yaml.snakeyaml.Yaml;

import com.terrier.boxcryptor.objects.BCInventaireRepertoire;

public class BoxCryptorGenerator {


	private File repertoireChiffre;
	private File repertoireNonChiffre;
	private Calendar startTraitement = Calendar.getInstance();
	
	private static final String INVENTORY_FILENAME = "liste_Fichiers_BoxCryptor.yml";
	



	/**
	 * Classe main
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		BoxCryptorGenerator bcx = new BoxCryptorGenerator();

		System.out.println("Début de la génération de l'inventaire");
		if(bcx.getPaths(args)){
			bcx.generateInventory();
		}
		else{
			System.err.println("******** ERREUR : Les répertoires sont incorrects ********");
		}
		System.out.println("Fin de la génération de l'inventaire");
	}

	/**
	 * Génération de l'inventaire
	 * @throws Exception 
	 */
	public void generateInventory() throws Exception{
		
		// Lecture de l'inventaire
		BCInventaireRepertoire inventaire = getFileInventory();
		printDelayFromBeginning("Read file Inventory");
		
		// Création de l'inventaire
		DirectoryInventoryGeneratorCallable inventory = new DirectoryInventoryGeneratorCallable(null, inventaire, this.repertoireChiffre, this.repertoireNonChiffre);
		inventaire = inventory.call();
		printDelayFromBeginning("Generate Inventory");
		
		// Ecriture de l'inventaire
		writeInventory(inventaire);
		printDelayFromBeginning("Dump Inventory");
	}

	//	"D:\\Perso\\eBooks"
	//	"X:\\eBooks"

	/**
	 * Recherche les répertoires chiffrés et en clair
	 * @return chemins
	 */
	public boolean getPaths(String[] args){
		Scanner entree =   new Scanner(System.in, "UTF-8");
		String cheminChiffre = "";
		if(args != null && args.length >= 1){
			cheminChiffre = args[0];
		}
		else{
			System.out.println("Entrez le répertoire CHIFFRE (D:)");
			cheminChiffre = new String(entree.nextLine().getBytes(), Charset.forName("UTF-8"));
		}
		repertoireChiffre = new File(cheminChiffre);
		System.out.println("> Le répertoire (" + repertoireChiffre.isDirectory() + ") " + repertoireChiffre.getName() + " existe " + repertoireChiffre.exists());


		String cheminNonChiffre = "";
		if(args != null && args.length >= 2){
			cheminNonChiffre = args[1];
		}
		else{
			System.out.println("Entrez le répertoire NON CHIFFRE (X:)");
			cheminNonChiffre = new String(entree.nextLine().getBytes(), Charset.forName("UTF-8"));
		}
		repertoireNonChiffre = new File(cheminNonChiffre);
		System.out.println("> Le répertoire (" + repertoireNonChiffre.isDirectory() + ") " + repertoireNonChiffre.getName() + "existe " + repertoireNonChiffre.exists());

		entree.close();
		printDelayFromBeginning("Get Path");
		return repertoireChiffre.exists() && repertoireChiffre.isDirectory() && repertoireNonChiffre.exists()  && repertoireNonChiffre.isDirectory();
	}



	/**
	 * Lecture de l'inventaire existant pour mise à jour
	 * @throws IOException
	 */
	public BCInventaireRepertoire getFileInventory() throws IOException{
		// This will output the full path where the file will be written to...
		File inventoryFile = new File(repertoireNonChiffre, INVENTORY_FILENAME);
		BCInventaireRepertoire repertoire;
		if(inventoryFile.exists()){
			System.out.println("Enregistrement de la liste dans " + inventoryFile.getCanonicalPath());
			
			Yaml yml = new Yaml();
			repertoire = yml.loadAs(new FileInputStream(inventoryFile), BCInventaireRepertoire.class);
			
		}
		else{
			System.out.println("Le fichier "+ inventoryFile.getAbsolutePath()+ " n'existe pas. Création du fichier");
			repertoire  = new BCInventaireRepertoire(repertoireChiffre.getName(), repertoireNonChiffre.getName());
		}
		
		return repertoire;

	}


	
	/**
	 * Ecriture de l'inventaire (dump)
	 * @param inventaireR inventaire
	 * @throws IOException
	 */
	private void writeInventory(final BCInventaireRepertoire inventaireR) throws IOException{
		
		Yaml yml = new Yaml();
		FileWriter inventoryWriter = new FileWriter(new File(this.repertoireNonChiffre.getAbsolutePath(), INVENTORY_FILENAME));
		yml.dump(inventaireR, inventoryWriter);
		inventoryWriter.flush();
		inventoryWriter.close();
		
	}
	
	
	private void printDelayFromBeginning(String traitement){
		System.out.println("["+traitement+"] > " + (Calendar.getInstance().getTimeInMillis() - startTraitement.getTimeInMillis())  + " ms");
	}
}
