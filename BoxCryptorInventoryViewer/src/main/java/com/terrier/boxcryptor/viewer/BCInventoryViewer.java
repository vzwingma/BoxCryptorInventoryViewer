package com.terrier.boxcryptor.viewer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.utils.AvailabilityListener;
import com.terrier.boxcryptor.utils.AvailabilityNotifier;
import com.terrier.boxcryptor.viewer.enums.InventoryCellColumnEnum;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;
import com.terrier.boxcryptor.viewer.factories.InventoryCellFactory;
import com.terrier.boxcryptor.viewer.factories.InventoryCellValueFactory;
import com.terrier.boxcryptor.viewer.factories.available.InventoryAvailableCellFactory;
import com.terrier.boxcryptor.viewer.factories.available.InventoryAvailableCellValueFactory;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireFichier;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Inventory  Viewer
 * @author vzwingma
 *
 */
public class BCInventoryViewer extends Application implements AvailabilityListener {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BCInventoryViewer.class);


	private final BCInventoryService service = new BCInventoryService();
	

	/**
	 * Start of inventory viewer
	 * @param cheminNonChiffre
	 */
	public void startViewInventory(String cheminNonChiffre){
		BCInventoryViewer.launch(cheminNonChiffre);
	}


	// Rang
	private static final int RG_FLOWPANE = 0;
	private static final int RG_LABEL_RESULTAT = 1;
	private static final int RG_TREE_TABLE_VIEW = 1;
	private static final int RG_LABEL_INFO = 2;
	private FlowPane verticalPane;

	

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		AvailabilityNotifier.register(this);
		/**
		 * Prepare inventory items
		 */
		// Retrieve the directory paramter
		String repertoireNonChiffre = null;
		List<String> unnamedParameters = getParameters().getUnnamed();
		for (String unnamed : unnamedParameters){
			repertoireNonChiffre = unnamed;
		}
		

		this.service.chargeInventaire(repertoireNonChiffre);
		/**
		 * Show GUI
		 */
		showGUI(primaryStage, repertoireNonChiffre);
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
		searchPane.setHgap(5);


		//Search field
		final TextField searchField = new TextField();
		searchField.setPromptText("Rechercher un nom en clair ou chiffrer");
		searchField.setPrefWidth(1800);
		searchField.textProperty().addListener((observable, oldValue, searchValue) -> {
			this.showFilteredTreeItems(searchValue);
		});
		searchPane.getChildren().add(searchField);

		// Result label
		Label resultLabel = new Label();
		resultLabel.setMaxHeight(20);
		searchPane.getChildren().add(resultLabel);
		verticalPane.getChildren().add(searchPane);

		/**
		 * Create table
		 */
		verticalPane.getChildren().add(RG_TREE_TABLE_VIEW, new TreeTableView<AbstractBCInventaireStructure>());
		/**
		 * Search tree items
		 */
		showFilteredTreeItems(null);
		
		
		// Info label
		Label infoLabel = new Label();
		infoLabel.setMaxHeight(20);
		infoLabel.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 100);
		verticalPane.getChildren().add(RG_LABEL_INFO, infoLabel);
		
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

		TreeItem<AbstractBCInventaireStructure> filteredInventoryItems = service.searchInTreeItem(searchValue);

		// nb de résultats
		int nbResultats = countElements(filteredInventoryItems);
		LOGGER.debug(" >> [{}] résultats" , nbResultats);
		
		findComponent(findComponent(RG_FLOWPANE, FlowPane.class), RG_LABEL_RESULTAT, Label.class).setText(nbResultats + " résultat(s)");

		/**
		 * Table de résultats
		 */
		TreeTableView<AbstractBCInventaireStructure> treeTableView = new TreeTableView<AbstractBCInventaireStructure>(filteredInventoryItems);
		
		TreeTableColumn<AbstractBCInventaireStructure, String> uncryptedDataColumn = new TreeTableColumn<>("Nom de fichier en clair");
		uncryptedDataColumn.setPrefWidth((Screen.getPrimary().getVisualBounds().getWidth() - 300)/2);
		// Factory d'affichage
		uncryptedDataColumn.setCellValueFactory(new InventoryCellValueFactory(InventoryCellColumnEnum.NOM_FICHIER_CLAIR));
		// Menu
		uncryptedDataColumn.setCellFactory(new InventoryCellFactory());
		
		
		TreeTableColumn<AbstractBCInventaireStructure, InventoryFileStatutObject> uncryptedStatusColumn = new TreeTableColumn<>("Statut du fichier");
		uncryptedStatusColumn.setCellValueFactory(new InventoryAvailableCellValueFactory(InventoryCellColumnEnum.STATUT_FICHIER_CLAIR));
		uncryptedStatusColumn.setCellFactory(new InventoryAvailableCellFactory());
		
		TreeTableColumn<AbstractBCInventaireStructure, String> cryptedDataColumn = new TreeTableColumn<>("Nom de fichier chiffré");
		cryptedDataColumn.setPrefWidth((Screen.getPrimary().getVisualBounds().getWidth() - 300)/2);
		// Factory d'affichage
		cryptedDataColumn.setCellValueFactory(new InventoryCellValueFactory(InventoryCellColumnEnum.NOM_FICHIER_CHIFFRE));
		// Menu
		cryptedDataColumn.setCellFactory(new InventoryCellFactory());
		
		
		TreeTableColumn<AbstractBCInventaireStructure, InventoryFileStatutObject> cryptedStatusColumn = new TreeTableColumn<>("Statut du fichier");
		cryptedStatusColumn.setCellValueFactory(new InventoryAvailableCellValueFactory(InventoryCellColumnEnum.STATUT_FICHIER_CHIFFRE));
		cryptedStatusColumn.setCellFactory(new InventoryAvailableCellFactory());
		
		
		treeTableView.getColumns().setAll(uncryptedDataColumn, uncryptedStatusColumn, cryptedDataColumn, cryptedStatusColumn);
		// Mise en page du tableau
		treeTableView.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 20);
		treeTableView.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - 120);
		treeTableView.setPadding(new Insets(10, 10, 10, 10));
		verticalPane.getChildren().remove(RG_TREE_TABLE_VIEW);
		verticalPane.getChildren().add(RG_TREE_TABLE_VIEW, treeTableView);
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



	/* (non-Javadoc)
	 * @see com.terrier.boxcryptor.utils.AvailabilityListener#availabilityUpdated()
	 */
	@Override
	public void itemAvailabilityUpdated(int pourcentage) {
		if(findComponent(RG_TREE_TABLE_VIEW, TreeTableView.class) != null){
			LOGGER.debug ("Refresh : {}%", pourcentage);
			findComponent(RG_TREE_TABLE_VIEW, TreeTableView.class).refresh();
		}
//		if(this.verticalPane.getChildren().get(RG_INFO) != null){
//			((Label)this.verticalPane.getChildren().get(RG_INFO)).setText("Génération de la disponibilité locale des fichiers : " + pourcentage + " %");
//		}
	}
	
	
	/**
	 * @param rang rang du noeud
	 * @param classeComponent classe du composant
	 * @return node correspondant au node
	 */
	private <T extends Node> T findComponent(int rang, Class<T> classeComponent){
		return findComponent(this.verticalPane, rang, classeComponent);
	}
	
	/**
	 * @param rang rang du noeud
	 * @param classeComponent classe du composant
	 * @return node correspondant au node
	 */
	@SuppressWarnings("unchecked")
	private <T extends Node> T findComponent(Pane rootPane, int rang, Class<T> classeComponent){
		return (T)rootPane.getChildren().get(rang);
	}
}
