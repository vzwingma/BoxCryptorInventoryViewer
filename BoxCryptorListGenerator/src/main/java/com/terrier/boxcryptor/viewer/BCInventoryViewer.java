package com.terrier.boxcryptor.viewer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.BoxCryptorInventoryManager;
import com.terrier.boxcryptor.objects.AbstractBCInventaireStructure;
import com.terrier.boxcryptor.objects.BCInventaireFichier;
import com.terrier.boxcryptor.objects.BCInventaireRepertoire;
import com.terrier.boxcryptor.utils.BCUtils;
import com.terrier.boxcryptor.viewer.objects.InventoryCellValueFactory;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Inventory  Viewer
 * @author vzwingma
 *
 */
public class BCInventoryViewer extends Application  {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BCInventoryViewer.class);
	
	
	/**
	 * Start of inventory viewer
	 * @param cheminNonChiffre
	 */
	public void startViewInventory(String cheminNonChiffre){
		BCInventoryViewer.launch(cheminNonChiffre);
	}


	/**
	 * Full tree items
	 */
	private TreeItem<AbstractBCInventaireStructure> inventoryItems;

	private FlowPane verticalPane;
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		/**
		 * Prepare inventory items
		 */
		// Retrieve the directory paramter
		String repertoireNonChiffre = null;
		List<String> unnamedParameters = getParameters().getUnnamed();
		for (String unnamed : unnamedParameters){
			repertoireNonChiffre = unnamed;
		}
		BCInventaireRepertoire inventory = BCUtils.loadYMLInventory(repertoireNonChiffre);
		this.inventoryItems  = prepareInventoryTreeItems(inventory);

		/**
		 * Show GUI
		 */
		showGUI(primaryStage, inventory.get_NomFichierClair());
	}

	
	
	
	/**
	 * 
	 * @param primaryStage
	 * @param inventoryItems
	 */
	private void showGUI(Stage primaryStage, String inventoryName){
		
		primaryStage.setTitle("Inventory Viewer [" + inventoryName + "]");        
		primaryStage.setMaximized(true);
		
		//Mise en page
		this.verticalPane = new FlowPane(Orientation.VERTICAL);
		verticalPane.setPadding(new Insets(10, 10, 10, 10));
		verticalPane.setVgap(5);
		verticalPane.setHgap(5);

		//Search field
		final TextField searchField = new TextField("trone");
		searchField.setPromptText("Recherche");
		searchField.textProperty().addListener((observable, oldValue, searchValue) -> {
			this.showFilteredTreeItems(searchValue);
		});
		verticalPane.getChildren().add(searchField);
		/**
		 * Create table
		 */
		TreeTableView<AbstractBCInventaireStructure> treeTableView = new TreeTableView<AbstractBCInventaireStructure>();
		verticalPane.getChildren().add(treeTableView);
		/**
		 * Search tree items
		 */
		showFilteredTreeItems(null);
		/**
		 * Show Inventory
		 */
		StackPane root = new StackPane();
		root.getChildren().add(verticalPane);
		primaryStage.setScene(new Scene(root));

		primaryStage.show();
	}
	
	
	/**
	 * Show filtered tree items
	 * @param searchValue search (if null : full display)
	 */
	@SuppressWarnings("unchecked")
	public void showFilteredTreeItems(String searchValue){
		LOGGER.debug("Recherche de [{}] dans l'inventaire", searchValue);
		
		 TreeItem<AbstractBCInventaireStructure> filteredInventoryItems = searchInTreeItem(inventoryItems, searchValue);
		/**
		 * Create table
		 */
		TreeTableView<AbstractBCInventaireStructure> treeTableView = new TreeTableView<AbstractBCInventaireStructure>(filteredInventoryItems);
		TreeTableColumn<AbstractBCInventaireStructure, String> uncryptedDataColumn = new TreeTableColumn<>("Nom de fichier en clair");
		uncryptedDataColumn.setPrefWidth((Screen.getPrimary().getVisualBounds().getWidth() - 20)/2);
		uncryptedDataColumn.setCellValueFactory(new InventoryCellValueFactory(true));
		TreeTableColumn<AbstractBCInventaireStructure, String> cryptedDataColumn = new TreeTableColumn<>("Nom de fichier chiffr�");
		cryptedDataColumn.setPrefWidth((Screen.getPrimary().getVisualBounds().getWidth() - 20)/2);
		cryptedDataColumn.setCellValueFactory(new InventoryCellValueFactory(false));
		treeTableView.getColumns().setAll(uncryptedDataColumn, cryptedDataColumn);
		// Mise en page du tableau
		treeTableView.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 20);
		treeTableView.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - 100);
		treeTableView.setPadding(new Insets(10, 10, 10, 10));
		verticalPane.getChildren().remove(1);
		verticalPane.getChildren().add(treeTableView);
	}
	

	
	/**
	 * Prepare inventory tree items
	 * @param inventaireRepertoire  inventaireR�pertoire
	 */
	private TreeItem<AbstractBCInventaireStructure> prepareInventoryTreeItems(BCInventaireRepertoire inventaireRepertoire){
		TreeItem<AbstractBCInventaireStructure> repertoireItem = new TreeItem<AbstractBCInventaireStructure> (inventaireRepertoire);
		repertoireItem.setExpanded(true);
		for (BCInventaireFichier inventaireFichier : inventaireRepertoire.getMapInventaireFichiers().values()) {
			repertoireItem.getChildren().add(
					new TreeItem<AbstractBCInventaireStructure>(inventaireFichier));
		}
		for (BCInventaireRepertoire inventaireSsRepertoire : inventaireRepertoire.getMapInventaireSousRepertoires().values()) {
			repertoireItem.getChildren().add(prepareInventoryTreeItems(inventaireSsRepertoire));
		}
		return repertoireItem;
	}
	
	/**
	 * @param treeItem
	 * @param searchValue
	 */
	private TreeItem<AbstractBCInventaireStructure> searchInTreeItem(final TreeItem<AbstractBCInventaireStructure> treeItem, final String searchValue){

		// Add directory
		if(treeItem.getValue() instanceof BCInventaireRepertoire){
			TreeItem<AbstractBCInventaireStructure> newTreeDirectoryItem = new TreeItem<AbstractBCInventaireStructure>();
			newTreeDirectoryItem.setExpanded(true);
			newTreeDirectoryItem.setValue(treeItem.getValue());
			for (TreeItem<AbstractBCInventaireStructure> subtreeItem : treeItem.getChildren()) {
				TreeItem<AbstractBCInventaireStructure> newTreeFileItem = searchInTreeItem(subtreeItem, searchValue);
				if(newTreeFileItem != null){
					newTreeDirectoryItem.getChildren().add(newTreeFileItem);
				}
			}
			if(newTreeDirectoryItem.getChildren().size() > 0
					|| BCUtils.searchTermsInInventory(treeItem.getValue(), searchValue)){			
				return newTreeDirectoryItem;
			}
			else{
				return null;
			}
		}
		else{
			if(BCUtils.searchTermsInInventory(treeItem.getValue(), searchValue)){
				TreeItem<AbstractBCInventaireStructure> newTreeFileItem = new TreeItem<AbstractBCInventaireStructure>();
				newTreeFileItem.setExpanded(true);
				newTreeFileItem.setValue(treeItem.getValue());
				return newTreeFileItem;
			}
			return null;
		}
	}
	
	
}
