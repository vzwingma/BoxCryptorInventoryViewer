package com.terrier.boxcryptor.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import com.terrier.boxcryptor.objects.BCInventaireFichier;
import com.terrier.boxcryptor.objects.BCInventaireRepertoire;
import com.terrier.boxcryptor.objects.BCInventoryItemView;
import com.terrier.boxcryptor.utils.Utils;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Inventory  Viewer
 * @author vzwingma
 *
 */
public class BoxCryptorInventoryViewer extends Application  {

	// Répertoire non chiffré
	private String cheminNonChiffre;


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
	@Override
	public void start(Stage primaryStage) throws Exception {

		BCInventaireRepertoire inventory = loadFileInventory();

		/**
		 * Show Inventory
		 */
		primaryStage.setTitle("Inventory Viewer " + inventory.get_NomFichierClair());        

		TreeItem<BCInventoryItemView> rootItem  = viewDirectoryInventory(inventory);
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




	/**
	 * Load of inventory file
	 * @throws IOException error during loading
	 */
	private BCInventaireRepertoire loadFileInventory() throws IOException{

		// Retrieve the directory paramter
		String repertoireNonChiffre = null;
		List<String> unnamedParameters = getParameters().getUnnamed();
		System.out.println ("\nunnamedParameters -");
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
