package com.terrier.boxcryptor;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Scanner;

import com.terrier.boxcryptor.generate.BoxCryptorInventoryGenerator;
import com.terrier.boxcryptor.objects.InventoryCommandEnum;
import com.terrier.boxcryptor.viewer.BoxCryptorInventoryViewer;

/**
 * Manager of BoxCryptor Inventory
 * Command line : 
 * 	--generate : to generate inventory
 *  --view : to view inventory by GUI
 * @author vzwingma
 *
 */
public class BoxCryptorInventoryManager {


	/**
	 * Classe main
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		// Forcage en UTF-8 pour les caractères chinois utilisés par BC
		System.setProperty("file.encoding","UTF-8");
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);

		InventoryCommandEnum commande = null;
		if(args.length >= 1){
			String cmd = args[0];
			if(cmd.equalsIgnoreCase("--generate")){
				commande = InventoryCommandEnum.GENERATE;
			}
			else if(cmd.equalsIgnoreCase("--view")){
				commande = InventoryCommandEnum.VIEW;
			}
		}


		if(InventoryCommandEnum.GENERATE.equals(commande)){
			
			String cheminChiffre    = getPaths(args != null && args.length > 1 ? args[1] : null, true);	
			String cheminNonChiffre = getPaths(args != null && args.length > 2 ? args[2] : null, true);
		
			BoxCryptorInventoryGenerator bcx = new BoxCryptorInventoryGenerator();
			bcx.startInventory(cheminChiffre, cheminNonChiffre);
			System.out.println("Fin de la génération de l'inventaire");
		}
		else if(InventoryCommandEnum.VIEW.equals(commande)){
			
			String cheminNonChiffre = getPaths(args != null && args.length > 1 ? args[1] : null, true);
			
			BoxCryptorInventoryViewer bxcV = new BoxCryptorInventoryViewer();
			bxcV.startViewInventory(cheminNonChiffre);
		}
		// Résultat commande inconnue
		else{
			System.out.println("**** USAGE ****");
			System.out.println(" -- generate [Répertoire chiffré (D:)] [Répertoire non chiffré (X:)]");
			System.out.println(" -- view [Répertoire non chiffré (D:)]");
		}
	}

	
	/**
	 * Insertion du chemin au répertoire
	 * @param arg arguments en entrée du main
	 * @param unCryptedDir traitement du répertoire non chiffré
	 * @return Chemin du répertoire
	 */
	private static String getPaths(String arg, boolean unCryptedDir){
		Scanner entree =   new Scanner(System.in, "UTF-8");
		String cheminRepertoireDonnees = "";
		if(arg != null){
			cheminRepertoireDonnees = arg;
		}
		else{
			if(unCryptedDir){
				System.out.println("Entrez le répertoire NON CHIFFRE (X:)");
			}
			else{
				System.out.println("Entrez le répertoire CHIFFRE (D:)");
			}
			cheminRepertoireDonnees = new String(entree.nextLine().getBytes(), Charset.forName("UTF-8"));
		}
		File repertoireDonnees = new File(cheminRepertoireDonnees);
		System.out.println("> Le répertoire (" + repertoireDonnees.isDirectory() + ") " + repertoireDonnees.getName() + " existe : " + repertoireDonnees.exists());

		entree.close();
		if(repertoireDonnees.exists()  && repertoireDonnees.isDirectory()){
			return cheminRepertoireDonnees;
		}
		return null;
	}

}
