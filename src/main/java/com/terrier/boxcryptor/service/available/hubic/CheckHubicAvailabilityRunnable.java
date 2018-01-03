package com.terrier.boxcryptor.service.available.hubic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.client.methods.HttpGet;
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
        HubicAPICredentials credentials = authToHubic(bearerAuth);
        if(credentials != null){
            
            List<HubicAPIContent> contents = getContentFromBackup(backupContainer, credentials);
            this.threadsAvailability.submit(new CheckHubicAvailabilityFileRunnable(this.inventoryItems, contents, this.threadsAvailability));
        }
        else{
            LOGGER.error("Erreur lors de la connexion à HUBIC");
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
