package com.terrier.boxcryptor.service.available.hubic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.terrier.boxcryptor.utils.HubicAPICredentials;

public class CheckHubicAvailabilityRunnable implements Runnable {


	public static final String HUBIC_API = "https://api.hubic.com/1.0/";


	String bearerAuth = "rhLnTaJyMjyFqRDqXwLTuSU1qlKx83JfK0hgVRUPfm023iY6rg2IiOJY1BCnJxZ5";

	@Override
	public void run() {

		HubicAPICredentials credentials = authToHubic(bearerAuth);

		String backupContainer = "HubiC-DeskBackup_eBooks";
		getContentFromBackup(backupContainer, credentials);
	}


	/**
	 * 
	 * @param httpClient
	 * @return authenticate
	 */
	private HubicAPICredentials authToHubic(String bearerAuth){
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(HUBIC_API + "account/credentials");
		request.addHeader("Authorization", "Bearer " + bearerAuth);
		try {
			HttpResponse response = httpClient.execute(request);
			return new Gson().fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), HubicAPICredentials.class);
		} catch (IOException e) {
			return null;
		}
	}


	private void getContentFromBackup(String containerName, HubicAPICredentials credentials){

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(credentials.getEndpoint()+"/"+ containerName+ "?format=json");
		try {
			request.addHeader("X-Auth-Token", credentials.getToken());
			HttpResponse response = httpClient.execute(request);
			List<LinkedTreeMap> containers3 = new Gson().fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), List.class);
		} catch (IOException e) {
			
		}
		//		//		String res3 = g(response3.getEntity().getContent());
		//		//		System.err.println(response3.getStatusLine() + " : "+ res3);
		//		
		//
		//	//	System.err.println(response3.getStatusLine() + " : " + containers3);
		//		for (LinkedTreeMap object : containers3) {
		//			System.err.println(object.get("name"));
		//		}
	}
}
