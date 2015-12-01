package com.terrier.boxcryptor.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import com.terrier.boxcryptor.objects.AbstractBCInventaireStructure;
import com.terrier.boxcryptor.objects.BCInventaireFichier;
import com.terrier.boxcryptor.objects.BCInventaireRepertoire;
import com.terrier.boxcryptor.utils.Utils;
import com.terrier.boxcryptor.viewer.objects.InventoryCellValueFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Inventory  Viewer
 * @author vzwingma
 *
 */
public class BoxCryptorInventoryViewer extends Application  {

	/**
	 * Start of inventory viewer
	 * @param cheminNonChiffre
	 */
	public void startViewInventory(String cheminNonChiffre){
		BoxCryptorInventoryViewer.launch(cheminNonChiffre);
	}



	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {

		/**
		 * Prepare inventory items
		 */
		BCInventaireRepertoire inventory = loadFileInventory();
		TreeItem<AbstractBCInventaireStructure> inventoryItems  = viewDirectoryInventory(inventory);

		
		/**
		 * Create table
		 */
		primaryStage.setTitle("Inventory Viewer [" + inventory.get_NomFichierClair()  + "]");        
		TreeTableView<AbstractBCInventaireStructure> treeTableView = new TreeTableView<AbstractBCInventaireStructure>(inventoryItems);
		
		TreeTableColumn<AbstractBCInventaireStructure, String> uncryptedDataColumn = new TreeTableColumn<>("Nom de fichier en clair");
		uncryptedDataColumn.setCellValueFactory(new InventoryCellValueFactory(true));
		
		TreeTableColumn<AbstractBCInventaireStructure, String> cryptedDataColumn = new TreeTableColumn<>("Nom de fichier chiffré");
		cryptedDataColumn.setCellValueFactory(new InventoryCellValueFactory(false));
		
		treeTableView.getColumns().setAll(uncryptedDataColumn, cryptedDataColumn);
	

		/**
		 * Show Inventory
		 */
		StackPane root = new StackPane();
		root.getChildren().add(treeTableView);
		primaryStage.setScene(new Scene(root));
		primaryStage.setMaximized(true);
		primaryStage.show();
	}


	/**
	 * View directory inventaire
	 * @param inventaireRepertoire  inventaireRépertoire
	 */
	private TreeItem<AbstractBCInventaireStructure> viewDirectoryInventory(BCInventaireRepertoire inventaireRepertoire){
		TreeItem<AbstractBCInventaireStructure> repertoireItem = new TreeItem<AbstractBCInventaireStructure> (inventaireRepertoire);
		repertoireItem.setExpanded(true);
		for (BCInventaireFichier inventaireFichier : inventaireRepertoire.getMapInventaireFichiers().values()) {
			repertoireItem.getChildren().add(
					new TreeItem<AbstractBCInventaireStructure>(inventaireFichier));
		}
		for (BCInventaireRepertoire inventaireSsRepertoire : inventaireRepertoire.getMapInventaireSousRepertoires().values()) {
			repertoireItem.getChildren().add(viewDirectoryInventory(inventaireSsRepertoire));
		}
		return repertoireItem;
	}




	/**
	 * Load of inventory file
	 * @throws IOException error during loading
	 */
	private BCInventaireRepertoire loadFileInventory() throws IOException{

		// Retrieve the directory paramter
		String repertoireNonChiffre = null;
		List<String> unnamedParameters = getParameters().getUnnamed();
		for (String unnamed : unnamedParameters){
			repertoireNonChiffre = unnamed;
		}
		if(repertoireNonChiffre != null){
			// This will output the full path where the file will be written to...
			File inventoryFile = new File(repertoireNonChiffre, Utils.INVENTORY_FILENAME);
			if(inventoryFile.exists()){
				System.out.println("Chargement de l'inventaire depuis " + inventoryFile.getCanonicalPath());
				Yaml yml = new Yaml();
				return yml.loadAs(new FileInputStream(inventoryFile), BCInventaireRepertoire.class);
			}
		}
		return null;
	}
}
