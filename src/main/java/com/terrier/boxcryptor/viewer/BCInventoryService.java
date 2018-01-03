/**
 * 
 */
package com.terrier.boxcryptor.viewer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.terrier.boxcryptor.service.available.hubic.CheckHubicAvailabilityRunnable;
import com.terrier.boxcryptor.service.available.local.CheckAvailabilityRunnable;
import com.terrier.boxcryptor.utils.BCUtils;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireFichier;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireRepertoire;

import javafx.scene.control.TreeItem;

/**
 * Classe de service de l'inventaire
 * @author vzwingma
 *
 */
public class BCInventoryService {

	// Inventaire
	private TreeItem<AbstractBCInventaireStructure> inventoryItems;
	
	private Calendar dateInventory = Calendar.getInstance();
	// Thread
	private ThreadPoolExecutor threadsAvailability = (ThreadPoolExecutor) Executors.newCachedThreadPool();


	//private static final Logger LOGGER = LoggerFactory.getLogger(InventoryAvailableMenuItems.class);
	/**
	 * 
	 * @param repertoireNonChiffre
	 * @return nom du répertoire root
	 * @throws IOException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void chargeInventaire(String lecteur, String repertoireNonChiffre) throws IOException, InterruptedException, ExecutionException{
		BCInventaireRepertoire inventory = BCUtils.loadYMLInventory(lecteur + "/" + repertoireNonChiffre);
		this.dateInventory.setTimeInMillis(inventory.getDateModificationDernierInventaire());
		this.inventoryItems  = getFullInventoryTreeItems(inventory);
		threadsAvailability.submit(new CheckAvailabilityRunnable(inventory, lecteur, threadsAvailability));
		threadsAvailability.submit(new CheckHubicAvailabilityRunnable(repertoireNonChiffre, inventory));
	}



	/**
	 * Prepare inventory tree items
	 * @param inventaireRepertoire  inventaireRépertoire
	 */
	private TreeItem<AbstractBCInventaireStructure> getFullInventoryTreeItems(final BCInventaireRepertoire inventaireRepertoire){
		TreeItem<AbstractBCInventaireStructure> repertoireItem = new TreeItem<AbstractBCInventaireStructure> (inventaireRepertoire);
		repertoireItem.setExpanded(true);
		for (final BCInventaireFichier inventaireFichier : inventaireRepertoire.getMapInventaireFichiers().values()) {
			repertoireItem.getChildren().add(new TreeItem<AbstractBCInventaireStructure>(inventaireFichier));
		}
		for (final BCInventaireRepertoire inventaireSsRepertoire : inventaireRepertoire.getMapInventaireSousRepertoires().values()) {
			repertoireItem.getChildren().add(getFullInventoryTreeItems(inventaireSsRepertoire));
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
	 * Recherche d'une valeur de répertoire ou de fichier dans l'arbre
	 * @param searchValue valeur à chercher
	 * returne le sous arbre correspondant
	 */
	public TreeItem<AbstractBCInventaireStructure> searchInTreeItem(final String searchValue){
		return searchInTreeItem(this.inventoryItems, searchValue);

	}

	
	
	/**
	 * @return date de l'inventaire
	 */
	public Date getDateInventaire(){
		return this.dateInventory.getTime();
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
			// si c'est le  repertoire qui correspond
			if(BCUtils.searchTermsInInventory(treeItem.getValue(), searchValue)){
				for (TreeItem<AbstractBCInventaireStructure> subtreeItem : treeItem.getChildren()) {
					newTreeDirectoryItem.getChildren().add(subtreeItem);
				}
				return newTreeDirectoryItem;
			}
			// recherche des sous repertoires
			else{
				for (TreeItem<AbstractBCInventaireStructure> subtreeItem : treeItem.getChildren()) {
					TreeItem<AbstractBCInventaireStructure> newTreeFileItem = searchInTreeItem(subtreeItem, searchValue);
					if(newTreeFileItem != null){
						newTreeDirectoryItem.getChildren().add(newTreeFileItem);
					}
				}
				if(newTreeDirectoryItem.getChildren().size() > 0){			
					return newTreeDirectoryItem;
				}
				else{
					return null;
				}
			}
		}
		else if(BCUtils.searchTermsInInventory(treeItem.getValue(), searchValue)){
			TreeItem<AbstractBCInventaireStructure> newTreeFileItem = new TreeItem<AbstractBCInventaireStructure>();
			newTreeFileItem.setExpanded(true);
			newTreeFileItem.setValue(treeItem.getValue());
			return newTreeFileItem;
		}
		else{
			return null;
		}
	}

}
