package com.terrier.boxcryptor.viewer;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.utils.BCUtils;
import com.terrier.boxcryptor.viewer.factories.InventoryCellFactory;
import com.terrier.boxcryptor.viewer.factories.InventoryCellValueFactory;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireFichier;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireRepertoire;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

	private Label resultLabel;

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


		FlowPane searchPane = new FlowPane(Orientation.HORIZONTAL);
		//searchPane.setPadding(new Insets(10, 10, 10, 10));
		searchPane.setHgap(5);


		//Search field
		final TextField searchField = new TextField();
		searchField.setPromptText("Rechercher un nom en clair ou chiffrer");
		searchField.setPrefWidth(800);
		searchField.textProperty().addListener((observable, oldValue, searchValue) -> {
			this.showFilteredTreeItems(searchValue);
		});
		searchPane.getChildren().add(searchField);


		this.resultLabel = new Label();
		this.resultLabel.setMaxHeight(20);
		searchPane.getChildren().add(this.resultLabel);
		verticalPane.getChildren().add(searchPane);
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

		// nb de résultats
		int nbResultats = countElements(filteredInventoryItems);
		LOGGER.debug(" >> [{}] résultats" , nbResultats);
		this.resultLabel.setText(nbResultats + " résultat(s)");

		/**
		 * Create table
		 */
		TreeTableView<AbstractBCInventaireStructure> treeTableView = new TreeTableView<AbstractBCInventaireStructure>(filteredInventoryItems);
		TreeTableColumn<AbstractBCInventaireStructure, String> uncryptedDataColumn = new TreeTableColumn<>("Nom de fichier en clair");
		uncryptedDataColumn.setPrefWidth((Screen.getPrimary().getVisualBounds().getWidth() - 20)/2);
		// Affichage
		uncryptedDataColumn.setCellValueFactory(new InventoryCellValueFactory(true));
		// Menu
		uncryptedDataColumn.setCellFactory(new InventoryCellFactory());
		TreeTableColumn<AbstractBCInventaireStructure, String> cryptedDataColumn = new TreeTableColumn<>("Nom de fichier chiffré");
		cryptedDataColumn.setPrefWidth((Screen.getPrimary().getVisualBounds().getWidth() - 20)/2);
		// Affichage
		cryptedDataColumn.setCellValueFactory(new InventoryCellValueFactory(false));
		// Menu
		cryptedDataColumn.setCellFactory(new InventoryCellFactory());
		treeTableView.getColumns().setAll(uncryptedDataColumn, cryptedDataColumn);
		// Mise en page du tableau
		treeTableView.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 20);
		treeTableView.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - 100);
		treeTableView.setPadding(new Insets(10, 10, 10, 10));
		verticalPane.getChildren().remove(1);
		verticalPane.getChildren().add(treeTableView);
	}

	/**
	 * @param tree
	 * @return le nombre du résultat
	 */
	private int countElements(TreeItem<AbstractBCInventaireStructure> tree){
		int nbElements = 0;
		if(tree != null){
			if(tree.getValue() != null && tree.getValue() instanceof BCInventaireFichier){
				nbElements ++;
			}
			if(tree.getChildren() != null){
				for (TreeItem<AbstractBCInventaireStructure> subStree : tree.getChildren()) {
					nbElements += countElements(subStree);
				}
			}
		}
		return nbElements;
	}

	/**
	 * Prepare inventory tree items
	 * @param inventaireRepertoire  inventaireRépertoire
	 */
	private TreeItem<AbstractBCInventaireStructure> prepareInventoryTreeItems(final BCInventaireRepertoire inventaireRepertoire){
		TreeItem<AbstractBCInventaireStructure> repertoireItem = new TreeItem<AbstractBCInventaireStructure> (inventaireRepertoire);
		repertoireItem.setExpanded(true);
		for (final BCInventaireFichier inventaireFichier : inventaireRepertoire.getMapInventaireFichiers().values()) {
			repertoireItem.getChildren().add(
					new TreeItem<AbstractBCInventaireStructure>(inventaireFichier));
		}
		for (final BCInventaireRepertoire inventaireSsRepertoire : inventaireRepertoire.getMapInventaireSousRepertoires().values()) {
			repertoireItem.getChildren().add(prepareInventoryTreeItems(inventaireSsRepertoire));
		}

		// Sort
		repertoireItem.getChildren().sort(new Comparator<TreeItem<AbstractBCInventaireStructure>>() {

			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(TreeItem<AbstractBCInventaireStructure> o1, TreeItem<AbstractBCInventaireStructure> o2) {
				return o1.getValue().get_NomFichierClair().compareToIgnoreCase(o2.getValue().get_NomFichierClair());
			}
		});

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
