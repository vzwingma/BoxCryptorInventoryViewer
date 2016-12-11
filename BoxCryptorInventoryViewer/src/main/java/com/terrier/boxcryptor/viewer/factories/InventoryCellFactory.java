package com.terrier.boxcryptor.viewer.factories;

import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

/**
 * Factory de la cellule
 * @author vzwingma
 *
 */
public class InventoryCellFactory implements Callback<TreeTableColumn<AbstractBCInventaireStructure,String>, TreeTableCell<AbstractBCInventaireStructure,String>>{

	
	
	/* (non-Javadoc)
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public TreeTableCell<AbstractBCInventaireStructure, String> call(TreeTableColumn<AbstractBCInventaireStructure, String> param) {
	    final TreeTableCell<AbstractBCInventaireStructure, String> cell = new TreeTableCell<>();
	    cell.textProperty().bind(cell.itemProperty());
	    cell.itemProperty().addListener(new InventoryMenuItems(cell));
	    return cell;
	}

}
