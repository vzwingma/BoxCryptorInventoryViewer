/**
 * 
 */
package com.terrier.boxcryptor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.terrier.boxcryptor.service.available.hubic.CheckHubicAvailabilityRunnable;

/**
 * @author vzwingma
 *
 */
public class TestHubicAPI {


	String bearerAuth = "rhLnTaJyMjyFqRDqXwLTuSU1qlKx83JfK0hgVRUPfm023iY6rg2IiOJY1BCnJxZ5";

	@Test
	public void connexionAPI() throws ClientProtocolException, IOException{
		CheckHubicAvailabilityRunnable runn = new CheckHubicAvailabilityRunnable();
		runn.run();
		
//		HttpGet request2 = new HttpGet(result.getEndpoint()+"?format=json");
//		request2.addHeader("X-Auth-Token", result.getToken());
//		HttpResponse response2 = httpClient.execute(request2);
//
//		List<LinkedTreeMap> containers = new Gson().fromJson(new InputStreamReader(response2.getEntity().getContent(), "UTF-8"), List.class);
//
//		System.err.println(response2.getStatusLine() + " : " + containers);
//		for (LinkedTreeMap object : containers) {
//			System.err.println(object.get("name"));
//		}

//
//		String backupContainer = "HubiC-DeskBackup_eBooks";
//
//		HttpGet request3 = new HttpGet(result.getEndpoint()+"/"+ backupContainer+ "?format=json");
//		request3.addHeader("X-Auth-Token", result.getToken());
//		HttpResponse response3 = httpClient.execute(request3);
//		//		String res3 = g(response3.getEntity().getContent());
//		//		System.err.println(response3.getStatusLine() + " : "+ res3);
//		List<LinkedTreeMap> containers3 = new Gson().fromJson(new InputStreamReader(response3.getEntity().getContent(), "UTF-8"), List.class);
//
//	//	System.err.println(response3.getStatusLine() + " : " + containers3);
//		for (LinkedTreeMap object : containers3) {
//			System.err.println(object.get("name"));
//		}
	}





	private String g(InputStream inputStream){
		Scanner s = new Scanner(inputStream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
