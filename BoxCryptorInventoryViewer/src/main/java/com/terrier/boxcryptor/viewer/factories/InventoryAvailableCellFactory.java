package com.terrier.boxcryptor.viewer.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.boxcryptor.viewer.BCInventoryViewer;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * Factory de la cellule
 * @author vzwingma
 *
 */
public class InventoryAvailableCellFactory implements Callback<TreeTableColumn<AbstractBCInventaireStructure,Boolean>, TreeTableCell<AbstractBCInventaireStructure,Boolean>>{


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryAvailableCellFactory.class);
	
	
	/* (non-Javadoc)
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public TreeTableCell<AbstractBCInventaireStructure, Boolean> call(TreeTableColumn<AbstractBCInventaireStructure, Boolean> param) {
	    final TreeTableCell<AbstractBCInventaireStructure, Boolean> cell = new TreeTableCell<AbstractBCInventaireStructure, Boolean>();
	    
	    String imagePath = "/images/";
	    
	    LOGGER.info(" {}", cell.itemProperty());
	    
	    if(cell.itemProperty().getValue() != null){
	    	if(cell.itemProperty().getValue()){
	    		imagePath += "circle_ok.png";
	    	}
	    	else{
	    		imagePath += "circle_ko.png";
	    	}
	    }
	    else{
	    	imagePath += "circle_ukn.png";
	    }
	    
	    
	    ImageView livePerformIcon = new ImageView(BCInventoryViewer.class.getResource(imagePath).toExternalForm());
	    livePerformIcon.setFitHeight(20);
        livePerformIcon.setFitWidth(20);
	    cell.setGraphic(livePerformIcon);
	    
	    return cell;
	}

}
