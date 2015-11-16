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
 * @author vzwingma
 *
 */
public class DirectoryInventoryGeneratorCallable implements Callable<BCInventaireRepertoire> {


	private BCInventaireRepertoire inventaireR;
	private File repertoireChiffre;
	private File repertoiresNonChiffre;
	private Calendar startTraitement;

	private ExecutorService executorInventory = Executors.newFixedThreadPool(10);

	/**
	 * Générable 
	 * @param inventaireR	inventaire d'un répertoire
	 * @param repertoireChiffre 
	 * @param repertoireNonChiffre
	 */
	public DirectoryInventoryGeneratorCallable(final BCInventaireRepertoire inventaireR, final File repertoireChiffre, final File repertoiresNonChiffre){
		this.inventaireR = inventaireR;
		this.repertoireChiffre = repertoireChiffre;
		this.repertoiresNonChiffre = repertoiresNonChiffre;
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
						listeExecSousRepertoires.add(executorInventory.submit(new DirectoryInventoryGeneratorCallable(this.inventaireR, fichierChiffre, repertoireNonChiffre)));						
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


		// Récupération des sous répertoires
		for (Future<BCInventaireRepertoire> resultatSousRepertoire : listeExecSousRepertoires) {
			BCInventaireRepertoire ssRepertoire = resultatSousRepertoire.get();
			inventaireR.addSSRepertoire(ssRepertoire);
		}
		printDelayFromBeginning(repertoiresNonChiffre.getName());
		return inventaireR;
	}



	private void printDelayFromBeginning(String traitement){
		System.out.println("THREAD ["+traitement+"] > " + (Calendar.getInstance().getTimeInMillis() - startTraitement.getTimeInMillis())  + " ms");
	}
}
