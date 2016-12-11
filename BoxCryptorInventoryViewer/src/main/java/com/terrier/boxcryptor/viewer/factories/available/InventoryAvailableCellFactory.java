package com.terrier.boxcryptor.viewer.factories.available;

import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

/**
 * Factory de la cellule statut
 * @author vzwingma
 *
 */
public class InventoryAvailableCellFactory implements Callback<TreeTableColumn<AbstractBCInventaireStructure,InventoryFileStatutEnum>, TreeTableCell<AbstractBCInventaireStructure,InventoryFileStatutEnum>>{




	/* (non-Javadoc)
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutEnum> call(TreeTableColumn<AbstractBCInventaireStructure, InventoryFileStatutEnum> param) {
		final TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutEnum> cell = new TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutEnum>();

		cell.graphicProperty().bind(new InventoryAvailableCellRenderer(cell));
		
		cell.itemProperty().addListener(new InventoryAvailableMenuItems(cell));
		return cell;
	}
}
