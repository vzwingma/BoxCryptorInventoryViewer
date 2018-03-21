package com.terrier.boxcryptor.viewer.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;

/**
 * Menu items sur les disponibilités
 * @author vzwingma
 *
 */
public class InventoryMenuItems implements ChangeListener<String> {




	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryMenuItems.class);


	private final TreeTableCell<AbstractBCInventaireStructure, String> cell;

	/**
	 * renderer d'icone
	 * @param cell
	 */
	public InventoryMenuItems(final TreeTableCell<AbstractBCInventaireStructure, String> cell){
		this.cell = cell;
	}
	@Override
	public void changed(ObservableValue<? extends String> obs,
			String oldValue, String newValue) {
		if (newValue != null) {
			final ContextMenu cellMenu = new ContextMenu();
			final MenuItem copiePPItem = new MenuItem("Copie dans le presse papier");
			copiePPItem.setOnAction(e -> copyToClipboard(cell.getItem()));
			cellMenu.getItems().add(copiePPItem);
			cell.setContextMenu(cellMenu);
		} else {
			cell.setContextMenu(null);
		}
	} 




	/**
	 * Valeur à mettre dans le presse papier
	 * @param value
	 */
	private void copyToClipboard(String value){
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		LOGGER.info("Copie de [{}]", value);

		final ClipboardContent content = new ClipboardContent();
		content.put(DataFormat.PLAIN_TEXT, value);
		clipboard.setContent(content);
	}
}