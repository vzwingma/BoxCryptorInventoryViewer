/**
 * 
 */
package com.terrier.boxcryptor;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.terrier.boxcryptor.objects.BCInventaireFichier;
import com.terrier.boxcryptor.objects.BCInventaireRepertoire;

/**
 * Callable d'un inventaire d'un répertoire
 * @author vzwingma
 *
 */
public class DirectoryInventoryGeneratorCallable implements Callable<BCInventaireRepertoire> {


	private BCInventaireRepertoire inventaireR;
	private File repertoireChiffre;
	private File repertoiresNonChiffre;
	private Calendar startTraitement;

	// Pool de threads
	private ExecutorService executorPool;
	// Parent
	private DirectoryInventoryGeneratorCallable parent;

	/**
	 * Générable 
	 * @param inventaireR	inventaire d'un répertoire
	 * @param repertoireChiffre 
	 * @param repertoireNonChiffre
	 */
	public DirectoryInventoryGeneratorCallable(final DirectoryInventoryGeneratorCallable parent, final File repertoireChiffre, final File repertoiresNonChiffre){
		//this.inventaireR = inventaireR;
		this.repertoireChiffre = repertoireChiffre;
		this.repertoiresNonChiffre = repertoiresNonChiffre;
		this.inventaireR = new BCInventaireRepertoire(this.repertoireChiffre.getName(), this.repertoiresNonChiffre.getName());
		this.parent = parent;
	}


	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public BCInventaireRepertoire call() throws Exception {

		this.startTraitement = Calendar.getInstance();

		// Premier parcours pour trouver les sous répertoires et lancer les tâches correspondantes
		List<Future<BCInventaireRepertoire>> listeExecSousRepertoires = new ArrayList<Future<BCInventaireRepertoire>>();
		// Parcours du répertoire chiffré
		for (File fichierChiffre : repertoireChiffre.listFiles()) {
			if(fichierChiffre.isDirectory()){
				for (File repertoireNonChiffre : repertoiresNonChiffre.listFiles()) {
					if(repertoireNonChiffre.isDirectory() && fichierChiffre.lastModified() == repertoireNonChiffre.lastModified()){
						listeExecSousRepertoires.add(
								getExecutorPool().submit(
										new DirectoryInventoryGeneratorCallable(this, fichierChiffre, repertoireNonChiffre))
								);						
					}
				}
			}
		}


		// Parcours des fichiers cette fois ci
		for (File fichierChiffre : repertoireChiffre.listFiles()) {
			if(!fichierChiffre.isDirectory()){
				for (File fichierNonChiffre : repertoiresNonChiffre.listFiles()) {
					if(!fichierNonChiffre.isDirectory() && fichierChiffre.lastModified() == fichierNonChiffre.lastModified()){
						inventaireR.addFichier(new BCInventaireFichier(fichierChiffre.getName(), fichierNonChiffre.getName()));					
					}
				}
			}
		}


		// Récupération des résultats des sous répertoires
		for (Future<BCInventaireRepertoire> resultatSousRepertoire : listeExecSousRepertoires) {
			BCInventaireRepertoire ssRepertoire = resultatSousRepertoire.get();
			inventaireR.addSSRepertoire(ssRepertoire);
		}
		printDelayTraitementFromBeginning();

		if(this.parent == null && this.executorPool != null){
			this.executorPool.shutdownNow();
		}
		return inventaireR;
	}


	/**
	 * @return le nom du traitement en cours
	 */
	private String getTraitementName(){
		StringBuilder bf = new StringBuilder();
		if(this.parent != null){
			bf.append(this.parent.getTraitementName()).append("|");
		}
		bf.append(this.repertoiresNonChiffre.getName());
		return bf.toString();
	}
	

	private void printDelayTraitementFromBeginning(){
		System.out.println("THREAD ["+getTraitementName()+"] > " + (Calendar.getInstance().getTimeInMillis() - startTraitement.getTimeInMillis())  + " ms");
	}


	/**
	 * @return le pool d'executor
	 */
	private ExecutorService getExecutorPool(){
		if(this.parent == null){
			if(this.executorPool == null){
				this.executorPool = Executors.newFixedThreadPool(100);
			}
			return this.executorPool;
		}
		else{
			return this.parent.getExecutorPool();
		}
	}
}
