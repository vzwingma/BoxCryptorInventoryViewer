package com.terrier.boxcryptor.viewer;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.service.available.local.AvailabilityNotifier;
import com.terrier.boxcryptor.viewer.enums.InventoryCellColumnEnum;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;
import com.terrier.boxcryptor.viewer.factories.InventoryCellFactory;
import com.terrier.boxcryptor.viewer.factories.InventoryCellValueFactory;
import com.terrier.boxcryptor.viewer.factories.available.InventoryAvailableCellFactory;
import com.terrier.boxcryptor.viewer.factories.available.InventoryAvailableCellValueFactory;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireFichier;

import javafx.application.Platform;
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
public class BCInventoryViewer extends AbstractBCInventoryApplication {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BCInventoryViewer.class);


	private final BCInventoryService service = new BCInventoryService();

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
		String[] unnamedParam = null;
		List<String> unnamedParameters = getParameters().getUnnamed();
		for (String unnamed : unnamedParameters){
			unnamedParam = unnamed != null ? unnamed.split("/") : null;
		}
		if(unnamedParam != null){
			this.service.chargeInventaire(unnamedParam[0], unnamedParam[1]);

			String v = this.getClass().getPackage().getImplementationVersion();

			/**
			 * Show GUI
			 */
			primaryStage.setTitle("Inventory Viewer " +v+" [" + unnamedParam[1] + "]");  
			showGUI(primaryStage);
		}
	}




	/**
	 * 
	 * @param primaryStage
	 * @param inventoryItems
	 */
	private void showGUI(Stage primaryStage){
		primaryStage.setMaximized(true);

		//Mise en page
		FlowPane verticalPane = new FlowPane(Orientation.VERTICAL);
		verticalPane.setPadding(new Insets(10, 10, 10, 10));
		verticalPane.setVgap(5);
		verticalPane.setHgap(5);
		setRootPane(verticalPane);

		FlowPane searchPane = new FlowPane(Orientation.HORIZONTAL);
		searchPane.setHgap(5);


		//Search field
		final TextField searchField = new TextField();
		searchField.setPromptText("Rechercher un nom en clair ou chiffrer");
		searchField.setPrefWidth(1700);
		searchField.textProperty().addListener((observable, oldValue, searchValue) -> this.showFilteredTreeItems(searchValue));
		searchPane.getChildren().add(searchField);

		// Result label
		Label resultLabel = new Label();
		resultLabel.setMaxHeight(20);
		searchPane.getChildren().add(resultLabel);
		addNodeInPane(RG_FLOWPANE, searchPane);

		/**
		 * Create table
		 */
		addNodeInPane(RG_TREE_TABLE_VIEW, new TreeTableView<AbstractBCInventaireStructure>());
		/**
		 * Search tree items
		 */
		showFilteredTreeItems(null);


		// Info label
		Label infoLabel = new Label();
		infoLabel.setMaxHeight(20);
		infoLabel.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 100);
		addNodeInPane(RG_LABEL_INFO, infoLabel);

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

		findComponent(findComponent(RG_FLOWPANE, FlowPane.class), RG_LABEL_RESULTAT, Label.class).setText(nbResultats + " résultat(s) au " + dateFormat.format(this.service.getDateInventaire()));

		/**
		 * Table de résultats
		 */
		TreeTableView<AbstractBCInventaireStructure> treeTableView = new TreeTableView<>(filteredInventoryItems);

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
		refreshNodeInPane(RG_TREE_TABLE_VIEW, treeTableView);
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



	private int localPourcentage = 0;
	private int onlinePourentage = 0;

	/* (non-Javadoc)
	 * @see com.terrier.boxcryptor.utils.AvailabilityListener#availabilityUpdated()
	 */
	@Override
	public void itemAvailabilityUpdated(int pourcentage) {
		this.localPourcentage = pourcentage;
		refresh();
	}




	/**
	 * @param pourcentage
	 */
	@Override
	public void itemOnlineAvailabilityUpdated(int pourcentage) {
		this.onlinePourentage = pourcentage;
		refresh();
	}

	/**
	 * Refresh du tableau
	 */
	private void refresh(){
		Platform.runLater(() -> {
				if(findComponent(RG_TREE_TABLE_VIEW, TreeTableView.class) != null){
					LOGGER.trace ("Refresh");
					((TreeTableColumn)findComponent(RG_TREE_TABLE_VIEW, TreeTableView.class).getColumns().get(1)).setVisible(false);
					((TreeTableColumn)findComponent(RG_TREE_TABLE_VIEW, TreeTableView.class).getColumns().get(1)).setVisible(true);
				}
				if(findComponent(RG_LABEL_INFO, Label.class) != null){
					findComponent(RG_LABEL_INFO, Label.class).setText("Génération de la disponibilité locale des fichiers : " + localPourcentage + " %.  Génération de la disponibilité HUBIC des fichiers : " + onlinePourentage + " %");
				}
			});
	}
}
