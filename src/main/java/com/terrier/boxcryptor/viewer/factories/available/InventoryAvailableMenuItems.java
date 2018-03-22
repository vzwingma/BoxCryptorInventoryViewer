package com.terrier.boxcryptor.viewer.factories.available;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableCell;

/**
 * Menu items sur les disponibilités
 * @author vzwingma
 *
 */
public class InventoryAvailableMenuItems implements ChangeListener<InventoryFileStatutObject> {



	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryAvailableMenuItems.class);

	private final TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutObject> cell;

	/**
	 * renderer d'icone
	 * @param cell
	 */
	public InventoryAvailableMenuItems(final TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutObject> cell){
		this.cell = cell;
	}
	@Override
	public void changed(ObservableValue<? extends InventoryFileStatutObject> obs,
			InventoryFileStatutObject oldValue, InventoryFileStatutObject newValue) {
		if (newValue != null && newValue.getStatut() != null && newValue.getStatut().equals(InventoryFileStatutEnum.DISPONIBLE)) {

			final MenuItem getContentItem = new MenuItem("Accéder au fichier");
			getContentItem.setOnAction(event -> {
				try {
					Runtime rt = Runtime.getRuntime();
					LOGGER.info("Ouverture du fichier [{}]", newValue.getCheminFichier());
					rt.exec("explorer "+newValue.getCheminFichier());
				} catch (IOException e) {
					LOGGER.error("Erreur lors du chargement du fichier [{}]", newValue.getCheminFichier(), e);
				}
			});


			final ContextMenu cellMenu = new ContextMenu();
			cellMenu.getItems().add(getContentItem);
			cell.setContextMenu(cellMenu);
		} else {
			cell.setContextMenu(null);
		}
	} 
}