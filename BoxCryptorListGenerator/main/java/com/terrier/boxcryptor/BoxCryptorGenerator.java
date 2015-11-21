package com.terrier.boxcryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.yaml.snakeyaml.Yaml;

import com.terrier.boxcryptor.objects.BCInventaireFichier;
import com.terrier.boxcryptor.objects.BCInventaireRepertoire;
import com.terrier.boxcryptor.objects.BCInventoryItemView;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Générateur d'inventaire Boxcryptor
 * @author vzwingma
 *
 */
public class BoxCryptorGenerator extends Application {

	// Répertoire chiffré
	private File repertoireChiffre;
	// Répertoire non chiffré
	private File repertoireNonChiffre;
	
	private Calendar startTraitement = Calendar.getInstance();
	
	private static final String INVENTORY_FILENAME = "liste_Fichiers_BoxCryptor.yml";
	
	private static BCInventaireRepertoire inventaireNew;
	/**
	 * Classe main
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		System.setProperty("file.encoding","UTF-8");
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);
		
		BoxCryptorGenerator bcx = new BoxCryptorGenerator();

		System.out.println("Début de la génération de l'inventaire");
		if(bcx.getPaths(args)){
			inventaireNew = bcx.generateInventory();
		}
		else{
			System.err.println("******** ERREUR : Les répertoires sont incorrects ********");
		}
		System.out.println("Fin de la génération de l'inventaire");
	//	launch();
	}

	/**
	 * Génération de l'inventaire
	 * @throws Exception 
	 */
	public BCInventaireRepertoire generateInventory() throws Exception{
		
		// Lecture de l'inventaire
		BCInventaireRepertoire inventaire = getFileInventory();
		System.out.println(inventaire.getCleMap());
		printDelayFromBeginning("Read file Inventory");
		
		// Création de l'inventaire
		ExecutorService threadsPool = Executors.newFixedThreadPool(100);
		DirectoryInventoryStreamGeneratorCallable inventory = new DirectoryInventoryStreamGeneratorCallable(
				threadsPool,
				this.repertoireNonChiffre.getName(),
				inventaire,
				this.repertoireChiffre.getAbsolutePath(), this.repertoireNonChiffre.getAbsolutePath());
		BCInventaireRepertoire inventaireNew = inventory.call();
		threadsPool.shutdown();
		printDelayFromBeginning("Generate Inventory");
		
		
		// Ecriture de l'inventaire
		writeInventory(inventaireNew);
		printDelayFromBeginning("Dump Inventory");
		return inventaireNew;
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
		System.out.println("> Le répertoire (" + repertoireNonChiffre.isDirectory() + ") " + repertoireNonChiffre.getName() + " existe " + repertoireNonChiffre.exists());

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
	
	
	/**
	 * @param traitement
	 */
	private void printDelayFromBeginning(String traitement){
		System.out.println("["+traitement+"] > " + (Calendar.getInstance().getTimeInMillis() - startTraitement.getTimeInMillis())  + " ms");
	}

	

	
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Inventory Viewer");        
	
		
		TreeItem<BCInventoryItemView> rootItem  = viewDirectoryInventory(inventaireNew);
		TreeView<BCInventoryItemView> tree = new TreeView<BCInventoryItemView> (rootItem);
		tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<BCInventoryItemView>>()
        {
            @Override
            public void changed(ObservableValue<? extends TreeItem<BCInventoryItemView>> observable, TreeItem<BCInventoryItemView> oldValue, TreeItem<BCInventoryItemView> newValue)
            {
            	
            	newValue.getValue().alternateMessageView();
            	System.out.println("Alternate : " + newValue.getValue().toString());
            }
        });
		
		
		
		StackPane root = new StackPane();
		root.getChildren().add(tree);
		primaryStage.setScene(new Scene(root, 500, 550));
		primaryStage.show();
		
	}
	
	
	/**
	 * View directory inventaire
	 * @param inventaireRepertoire  inventaireRépertoire
	 */
	private TreeItem<BCInventoryItemView> viewDirectoryInventory(BCInventaireRepertoire inventaireRepertoire){
		TreeItem<BCInventoryItemView> repertoireItem = new TreeItem<BCInventoryItemView> (new BCInventoryItemView(inventaireRepertoire.get_NomFichierClair(), inventaireRepertoire.get_NomFichierChiffre()));
		repertoireItem.setExpanded(true);
		for (BCInventaireFichier inventaireFichier : inventaireRepertoire.getMapInventaireFichiers().values()) {
			repertoireItem.getChildren().add(
					new TreeItem<BCInventoryItemView>(new BCInventoryItemView(inventaireFichier.get_NomFichierClair(), inventaireFichier.get_NomFichierChiffre())));
		}
		for (BCInventaireRepertoire inventaireSsRepertoire : inventaireRepertoire.getMapInventaireSousRepertoires().values()) {
			repertoireItem.getChildren().add(viewDirectoryInventory(inventaireSsRepertoire));
		}
		return repertoireItem;
	}
}
