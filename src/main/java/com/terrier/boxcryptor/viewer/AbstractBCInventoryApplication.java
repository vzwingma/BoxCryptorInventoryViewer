/**
 * 
 */
package com.terrier.boxcryptor.viewer;

import com.terrier.boxcryptor.service.available.local.AvailabilityListener;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * @author vzwingma
 *
 */
public abstract class AbstractBCInventoryApplication extends Application implements AvailabilityListener {
	
	
	private Pane rootPane;


	/**
	 * @param rang rang du noeud
	 * @param classeComponent classe du composant
	 * @return node correspondant au node
	 */
	protected <T extends Node> T findComponent(int rang, Class<T> classeComponent){
		return findComponent(getRootPane(), rang, classeComponent);
	}

	/**
	 * @param rang rang du noeud
	 * @param classeComponent classe du composant
	 * @return node correspondant au node
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Node> T findComponent(Pane rootPane, int rang, Class<T> classeComponent){
		return (T)rootPane.getChildren().get(rang);
	}
	
	
	/**
	 * Add node in pane
	 * @param rang
	 * @param node
	 */
	public void refreshNodeInPane(int rang, Node node){
		getRootPane().getChildren().remove(rang);
		addNodeInPane(rang, node);
	}
	
	/**
	 * Add node in pane
	 * @param rang
	 * @param node
	 */
	public void addNodeInPane(int rang, Node node){
		getRootPane().getChildren().add(rang, node);
	}
	
	
	/**
	 * Set root
	 * @param rootPane
	 */
	public void setRootPane(Pane rootPane){
		this.rootPane = rootPane;
	}
	

	
	/**
	 * @return rootPane
	 */
	private Pane getRootPane(){
		return this.rootPane;
	}
}
