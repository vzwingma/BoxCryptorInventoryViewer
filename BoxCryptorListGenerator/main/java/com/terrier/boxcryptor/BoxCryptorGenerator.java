package com.terrier.boxcryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.yaml.snakeyaml.Yaml;

import com.terrier.boxcryptor.objects.BCInventaireFichier;
import com.terrier.boxcryptor.objects.BCInventaireRepertoire;

public class BoxCryptorGenerator {


	private File repertoireChiffre;
	private File repertoireNonChiffre;

	
	private static final String INVENTORY_FILENAME = "liste_Fichiers_BoxCryptor.yml";
	/**
	 * Classe main
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BoxCryptorGenerator bcx = new BoxCryptorGenerator();

		if(bcx.getPaths(args)){
			bcx.generateInventory();
		}
		else{
			System.err.println("******** ERREUR : Les répertoires sont incorrects ********");
		}

	}

	/**
	 * Génération de l'inventaire
	 * @throws IOException
	 */
	public void generateInventory() throws IOException{

		System.out.println("Début de la génération de l'inventaire");
		// Lecture de l'inventaire
		BCInventaireRepertoire inventaire = getFileInventory();
		// Création de l'inventaire
		generateInventoryForDirectory(inventaire, this.repertoireChiffre, this.repertoireNonChiffre);
		// Ecriture de l'inventaire
		writeInventory(inventaire);
		System.out.println("Fin de la génération de l'inventaire");

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
	 * Génération de la liste pour le répertoire
	 * @param directoryChiffre répertoire chiffré
	 * @param directoryNonChiffre répertoire non chiffré
	 * @param inventoryWriter writer
	 * @param padding padding de répertoire
	 * @throws IOException erreur
	 */
	private void generateInventoryForDirectory(final BCInventaireRepertoire inventaireR, final File repertoireChiffre, final File repertoireNonChiffre) throws IOException{

		// Parcours du répertoire chiffré
		for (File fichierChiffre : repertoireChiffre.listFiles()) {
			// Recherche du répertoire non chiffré
			boolean found = false;
			for (File fichierNonChiffre : repertoireNonChiffre.listFiles()) {
				if(fichierNonChiffre.lastModified() == fichierChiffre.lastModified()){
					found = true;

			//		System.out.println("[" + fichierChiffre.getName() + "] > ["+fichierNonChiffre.getName()+"]");

					if(fichierChiffre.isDirectory() && fichierNonChiffre.isDirectory()){
						BCInventaireRepertoire ssRepertoire = inventaireR.getBCInventaireSousRepertoire(fichierChiffre, fichierNonChiffre);
						generateInventoryForDirectory(ssRepertoire, fichierChiffre, fichierNonChiffre);
						inventaireR.addSSRepertoire(ssRepertoire);
					}
					else{
						BCInventaireFichier fichier = new BCInventaireFichier(fichierChiffre.getName(), fichierNonChiffre.getName());
						inventaireR.addFichier(fichier);
					}
					break;
				}
			}
			if(!found && !fichierChiffre.getName().equals("FolderKey.bch")){
				System.err.println("[" + fichierChiffre.getName() + "] > NON TROUVE");
			}
		}
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
}
