package com.terrier.boxcryptor.service.available.hubic;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.internal.LinkedTreeMap;
import com.terrier.boxcryptor.service.available.hubic.objects.HubicAPIContent;
import com.terrier.boxcryptor.service.available.hubic.objects.HubicAPICredentials;
import com.terrier.boxcryptor.utils.AbstractHTTPClient;
import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireRepertoire;

public class CheckHubicAvailabilityRunnable extends AbstractHTTPClient implements Runnable {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckHubicAvailabilityRunnable.class);

	private ThreadPoolExecutor threadsAvailability = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	
	// Items d'inventaires
	private BCInventaireRepertoire inventoryItems;

	private String backupContainer;
	
	
	String bearerAuth = "ZdLFdirKHcGJpOZAq5gvITA4anURmz08FxCHxZIDk4VFOW5iD6JvQu9yRiKCJh4B";


	public CheckHubicAvailabilityRunnable(String nomRepertoireSauvegarde, BCInventaireRepertoire inventaireRepertoire){
		this.inventoryItems = inventaireRepertoire;
		this.backupContainer = "HubiC-DeskBackup_" + nomRepertoireSauvegarde;
		LOGGER.info("Recherche de la disponibilité des fichiers dans la sauvegarde {}", this.backupContainer);
	}


	@Override
	public void run() {
		
		getAccessToken("YXBpX2h1YmljXzEzNjYyMDY3MjhVNmZhVXZEU2ZFMWlGSW1vRkFGVUlmRFJiSnl0bGFZMDpnWGZ1M0tVSU8xSzU3alVzVzdWZ0ttTkVoT1dJYkZkeTdyOFoyeEJkWm41SzZTTWtNbW5VNGxRVWNuUnk1RTI2");
		
		
		
		
		
//		HubicAPICredentials credentials = authToHubic(bearerAuth);
//		if(credentials != null){
//			
//			List<HubicAPIContent> contents = getContentFromBackup(backupContainer, credentials);
//			this.threadsAvailability.submit(new CheckHubicAvailabilityFileRunnable(this.inventoryItems, contents, this.threadsAvailability));
//		}
//		else{
//			LOGGER.error("Erreur lors de la connexion à HUBIC");
//		}
	}


	/**
	 * 
	 * @param httpClient
	 * @return authenticate
	 */
	protected void getAccessToken(String basicAuth){

		String code = "1502029346PDcDqQ9WyvDPjQqTbEbygQCqOjSslxKsJU4C4jVzPxJ7XCU5MNrV04U4IqSSPZNO";
		
		
		HttpPost request = new HttpPost(HUBIC_API + "oauth/token");
		request.addHeader("Authorization", basicAuth);
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		try {
			HttpEntity 	entity = new StringEntity("code=1502029346PDcDqQ9WyvDPjQqTbEbygQCqOjSslxKsJU4C4jVzPxJ7XCU5MNrV04U4IqSSPZNO&redirect_uri=https://api.hubic.com/sandbox/&grant_type=authorization_code");
			request.setEntity(entity);
			return executeHTTPRequest(request, HubicAPICredentials.class);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}

	/**
	 * 
	 * @param httpClient
	 * @return authenticate
	 */
	protected HubicAPICredentials authToHubic(String bearerAuth){

		HttpGet request = new HttpGet(HUBIC_API + "account/credentials");
		request.addHeader("Authorization", "Bearer " + bearerAuth);
		return executeHTTPRequest(request, HubicAPICredentials.class);

	}


	/**
	 * @param containerName
	 * @param credentials
	 * @return contents
	 */
	protected List<HubicAPIContent> getContentFromBackup(String containerName, HubicAPICredentials credentials){

		List<HubicAPIContent> listeContents = new ArrayList<>();

		HttpGet request = new HttpGet(credentials.getEndpoint()+"/"+ containerName+ "?format=json");
		request.addHeader("X-Auth-Token", credentials.getToken());
		@SuppressWarnings("unchecked")
		List<LinkedTreeMap<String, String>> containerContent = executeHTTPRequest(request, List.class);
		if(containerContent != null){
			listeContents.addAll(HubicAPIContent.getContents(containerContent));
		}
		return listeContents;
	}


}
