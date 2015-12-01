/**
 * 
 */
package com.terrier.boxcryptor.viewer.objects;

import com.terrier.boxcryptor.objects.AbstractBCInventaireStructure;

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
	private boolean uncryptedValue;
	
	/**
	 * Constructor
	 * @param uncryptedValue Show uncrypted value ?
	 */
	public InventoryCellValueFactory(boolean uncryptedValue){
		this.uncryptedValue = uncryptedValue;
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public ObservableValue<String> call(CellDataFeatures<AbstractBCInventaireStructure, String> param) {
		if(uncryptedValue){
			return new ReadOnlyStringWrapper(param.getValue().getValue().get_NomFichierClair());
		}
		else{
			return new ReadOnlyStringWrapper(param.getValue().getValue().get_NomFichierChiffre());
		}
	}

}
