/**
 * 
 */
package com.terrier.boxcryptor.viewer.factories;

import com.terrier.boxcryptor.viewer.enums.InventoryCellColumnEnum;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * Factory of cell of uncrypted file value
 * @author vzwingma
 *
 */
public class InventoryCellValueFactory implements Callback<TreeTableColumn.CellDataFeatures<AbstractBCInventaireStructure,String>, ObservableValue<String>> {

	// Show uncrypted value ?
	private InventoryCellColumnEnum cellType;
	
	/**
	 * Constructor
	 * @param uncryptedValue Show uncrypted value ?
	 */
	public InventoryCellValueFactory(InventoryCellColumnEnum cellType){
		this.cellType = cellType;
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public ObservableValue<String> call(CellDataFeatures<AbstractBCInventaireStructure, String> param) {
		
		ReadOnlyStringWrapper valeurCell = null;
		switch (cellType) {
		case NOM_FICHIER_CLAIR:
			valeurCell = new ReadOnlyStringWrapper(param.getValue().getValue().get_NomFichierClair());
			break;
		case NOM_FICHIER_CHIFFRE:
			valeurCell = new ReadOnlyStringWrapper(param.getValue().getValue().get_NomFichierChiffre());
			break;	
		default:
			valeurCell = new ReadOnlyStringWrapper("???");
		}
		return valeurCell;
	}

}
