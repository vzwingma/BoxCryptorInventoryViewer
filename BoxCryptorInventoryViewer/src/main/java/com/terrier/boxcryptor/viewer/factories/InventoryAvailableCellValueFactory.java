/**
 * 
 */
package com.terrier.boxcryptor.viewer.factories;

import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * Factory of cell of uncrypted file value
 * @author vzwingma
 *
 */
public class InventoryAvailableCellValueFactory implements Callback<TreeTableColumn.CellDataFeatures<AbstractBCInventaireStructure, Boolean>, ObservableValue<Boolean>> {

	// Show uncrypted value ?
	private InventoryCellEnum cellType;
	
	/**
	 * Constructor
	 * @param uncryptedValue Show uncrypted value ?
	 */
	public InventoryAvailableCellValueFactory(InventoryCellEnum cellType){
		this.cellType = cellType;
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public ObservableValue<Boolean> call(CellDataFeatures<AbstractBCInventaireStructure, Boolean> param) {
		
		ReadOnlyBooleanWrapper valeurCell = null;
		switch (cellType) {
		case STATUT_FICHIER_CHIFFRE:
			valeurCell = new ReadOnlyBooleanWrapper(param.getValue().getValue().isStatutFichierChiffre());
			break;
		case STATUT_FICHIER_CLAIR:
			valeurCell = new ReadOnlyBooleanWrapper(param.getValue().getValue().isStatutFichierClair());
			break;			
		default:
			valeurCell = new ReadOnlyBooleanWrapper();
		}
		return valeurCell;
	}

}
