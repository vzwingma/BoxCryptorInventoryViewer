package com.terrier.boxcryptor.viewer.factories;

import com.terrier.boxcryptor.viewer.BCInventoryViewer;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * Factory de la cellule statut
 * @author vzwingma
 *
 */
public class InventoryAvailableCellFactory implements Callback<TreeTableColumn<AbstractBCInventaireStructure,Boolean>, TreeTableCell<AbstractBCInventaireStructure,Boolean>>{




	/* (non-Javadoc)
	 * @see javafx.util.Callback#call(java.lang.Object)
	 */
	@Override
	public TreeTableCell<AbstractBCInventaireStructure, Boolean> call(TreeTableColumn<AbstractBCInventaireStructure, Boolean> param) {
		final TreeTableCell<AbstractBCInventaireStructure, Boolean> cell = new TreeTableCell<AbstractBCInventaireStructure, Boolean>();

		cell.graphicProperty().bind(new ObservableValue<Node>() {

			/* (non-Javadoc)
			 * @see javafx.beans.value.ObservableValue#addListener(javafx.beans.value.ChangeListener)
			 */
			@Override
			public void addListener(ChangeListener<? super Node> listener) { 
			}

			/* (non-Javadoc)
			 * @see javafx.beans.value.ObservableValue#removeListener(javafx.beans.value.ChangeListener)
			 */
			@Override
			public void removeListener(ChangeListener<? super Node> listener) { }

			/* (non-Javadoc)
			 * @see javafx.beans.value.ObservableValue#getValue()
			 */
			@Override
			public Node getValue() {
				String imagePath = "/images/";
				Boolean valeur = cell.itemProperty().get();
				if(valeur != null){
					if(valeur.booleanValue()){
						imagePath += "circle_ok.png";
					}
					else{
						imagePath += "circle_ko.png";
					}
				}
				else{
					imagePath += "circle_ukn.png";
					return null;
				}


				ImageView livePerformIcon = new ImageView(BCInventoryViewer.class.getResource(imagePath).toExternalForm());
				livePerformIcon.setFitHeight(20);
				livePerformIcon.setFitWidth(20);
				return livePerformIcon;
			}

			@Override
			public void addListener(InvalidationListener listener) { }

			@Override
			public void removeListener(InvalidationListener listener) { }
		});
		return cell;
	}
}
