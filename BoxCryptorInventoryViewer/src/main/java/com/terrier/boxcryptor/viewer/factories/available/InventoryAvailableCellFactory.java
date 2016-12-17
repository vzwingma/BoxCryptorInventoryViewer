package com.terrier.boxcryptor.viewer.factories.available;

import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

/**
 * Factory de la cellule statut
 * @author vzwingma
 *
 */
public class InventoryAvailableCellFactory implements Callback<TreeTableColumn<AbstractBCInventaireStructure,InventoryFileStatutObject>, TreeTableCell<AbstractBCInventaireStructure,InventoryFileStatutObject>>{




	/* (non-Javadoc)
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutObject> call(TreeTableColumn<AbstractBCInventaireStructure, InventoryFileStatutObject> param) {
		final TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutObject> cell = new TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutObject>();
		cell.textProperty().bind(cell.itemProperty().asString());
		cell.graphicProperty().bind(new InventoryAvailableCellRenderer(cell));
		cell.itemProperty().addListener(new InventoryAvailableMenuItems(cell));
		return cell;
	}
}
