package com.terrier.boxcryptor.viewer.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.viewer.BCInventoryViewer;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.util.Callback;

/**
 * Factory de la cellule
 * @author vzwingma
 *
 */
public class InventoryCellFactory implements Callback<TreeTableColumn<AbstractBCInventaireStructure,String>, TreeTableCell<AbstractBCInventaireStructure,String>>{


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BCInventoryViewer.class);
	
	
	/* (non-Javadoc)
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public TreeTableCell<AbstractBCInventaireStructure, String> call(
			TreeTableColumn<AbstractBCInventaireStructure, String> param) {
	    final TreeTableCell<AbstractBCInventaireStructure, String> cell = new TreeTableCell<>();
	    
	    
	    cell.textProperty().bind(cell.itemProperty());
	    cell.itemProperty().addListener(new ChangeListener<String>() {
	      @Override
	      public void changed(ObservableValue<? extends String> obs,
	          String oldValue, String newValue) {
	        if (newValue != null) {
	          final ContextMenu cellMenu = new ContextMenu();
	          final MenuItem copiePPItem = new MenuItem("Copie dans le presse papier");
	          copiePPItem.setOnAction(new EventHandler<ActionEvent>(){
	            @Override
	            public void handle(ActionEvent event) {
	            	// Copie dans le presse papier
		        	copyToClipboard(cell.getItem());
	            }
	          });
	          cellMenu.getItems().add(copiePPItem);
	          cell.setContextMenu(cellMenu);
	        } else {
	          cell.setContextMenu(null);
	        }
	      } 
	    });
	    return cell;
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
