package com.terrier.boxcryptor.viewer.factories.available;

import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableCell;

/**
 * Menu items sur les disponibilités
 * @author vzwingma
 *
 */
public class InventoryAvailableMenuItems implements ChangeListener<InventoryFileStatutObject> {



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
		if (newValue != null ){ //&& newValue.getStatut() != null && newValue.getStatut().equals(InventoryFileStatutEnum.DISPONIBLE)) {
			
			final MenuItem getContentItem = new MenuItem("Accéder au fichier");
			getContentItem.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					// Copie dans le presse papier
					System.out.println(newValue.getCheminFichier());
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