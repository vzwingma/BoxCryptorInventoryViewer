/**
 * 
 */
package com.terrier.boxcryptor.viewer.factories.available;

import com.terrier.boxcryptor.viewer.BCInventoryViewer;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutEnum;
import com.terrier.boxcryptor.viewer.enums.InventoryFileStatutObject;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TreeTableCell;
import javafx.scene.image.ImageView;

/**
 * @author vzwingma
 *
 */
public class InventoryAvailableCellRenderer implements ObservableValue<Node>  {


	private final TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutObject> cell;

	/**
	 * renderer d'icone
	 * @param cell
	 */
	public InventoryAvailableCellRenderer(final TreeTableCell<AbstractBCInventaireStructure, InventoryFileStatutObject> cell){
		this.cell = cell;
	}

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
		InventoryFileStatutObject valeur = cell.itemProperty().get();
		InventoryFileStatutEnum statut = InventoryFileStatutEnum.INCONNU;
		if(valeur == null || valeur.getStatut() == null){
			statut = InventoryFileStatutEnum.NULL;
		}
		else{
			statut = valeur.getStatut();
		}
		switch (statut) {
		case INCONNU:
			imagePath += "circle_ukn.png";
			break;
		case DISPONIBLE:
			imagePath += "circle_ok.png";
			break;
		case INDISPONIBLE:
			imagePath += "circle_ko.png";
			break;
		case NULL:
		default:
			imagePath = null;
			break;
		}

		if(imagePath != null){
			ImageView livePerformIcon = new ImageView(BCInventoryViewer.class.getResource(imagePath).toExternalForm());
			livePerformIcon.setFitHeight(20);
			livePerformIcon.setFitWidth(20);
			return livePerformIcon;
		}
		else{
			return null;
		}
	}

	@Override
	public void addListener(InvalidationListener listener) { }

	@Override
	public void removeListener(InvalidationListener listener) { }
}
