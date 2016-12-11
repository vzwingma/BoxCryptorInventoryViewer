package com.terrier.boxcryptor.viewer.factories.available;

import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
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
public class InventoryAvailableMenuItems implements ChangeListener<InventoryFileStatutEnum> {



	private final TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutEnum> cell;

	/**
	 * renderer d'icone
	 * @param cell
	 */
	public InventoryAvailableMenuItems(final TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutEnum> cell){
		this.cell = cell;
	}
	@Override
	public void changed(ObservableValue<? extends InventoryFileStatutEnum> obs,
			InventoryFileStatutEnum oldValue, InventoryFileStatutEnum newValue) {
		if (newValue != null) {
			final ContextMenu cellMenu = new ContextMenu();
			final MenuItem copiePPItem = new MenuItem("Accéder au fichier");
			copiePPItem.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					// Copie dans le presse papier
					System.out.println(cell.getItem());
				}
			});
			cellMenu.getItems().add(copiePPItem);
			cell.setContextMenu(cellMenu);
		} else {
			cell.setContextMenu(null);
		}
	} 
}