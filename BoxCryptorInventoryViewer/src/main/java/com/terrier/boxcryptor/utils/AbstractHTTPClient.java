/**
 * 
 */
package com.terrier.boxcryptor.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Client HTTP
 * @author vzwingma
 *
 */
public abstract class AbstractHTTPClient {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHTTPClient.class);


	public static final String HUBIC_API = "https://api.hubic.com/1.0/";

	/**
	 * chargerment de la réponse
	 * @param request requête
	 * @param classEntityResponse classe de la réponse
	 * @return classe parsée de la réponse à la requête. 
	 */
	public <T> T executeHTTPRequest(HttpRequestBase request, Class<T> classEntityResponse){
		HttpResponse response = executeHTTPRequest(request);
		if(response != null){
			String rawResponse = readRawResponse(response);
			if(response.getStatusLine().getStatusCode() == 200){	
				return readJsonResponse(rawResponse, classEntityResponse);
			}
			else{
				LOGGER.warn("La requête est en erreur [{}]", response.getStatusLine());
			}
		}
		else{
			LOGGER.error("Erreur la réponse est nulle");
		}
		return null;
	}


	/**
	 * @param request
	 * @return réponse HTTP
	 */
	protected HttpResponse executeHTTPRequest(HttpRequestBase request){
		HttpClient httpClient = HttpClientBuilder.create().build();
		if(request != null){
			try {
				LOGGER.debug("Exécution de la requête [{}]" , request.getURI());
				HttpResponse response = httpClient.execute(request);
				LOGGER.debug("Réponse {}", response.getStatusLine());
				return response;
			} catch (IOException e) {
				LOGGER.error("Erreur lors de l'exécution de la requête [{}]" , request.getURI());
			}
		}
		return null;
	}




	/**
	 * @param response
	 * @return
	 */
	protected String readRawResponse(HttpResponse response){
		try {

			if(response != null && response.getEntity() != null){
				@SuppressWarnings("resource")
				Scanner s = new Scanner(new InputStreamReader(response.getEntity().getContent(), "UTF-8")).useDelimiter("\\A");
				String stringStream = s.hasNext() ? s.next() : "";
				LOGGER.debug("Réponse HTTP : [{}] : {}", response.getStatusLine(), stringStream);
				s.close();
				return stringStream;
			}
			else{
				return null;
			}
		} catch (UnsupportedOperationException | IOException e) {
			LOGGER.error("Erreur lors de la réception de la réponse HTTP ", e);
			return null;
		}
	}

	/**
	 * @param stringStream stringStream
	 * @param classEntityResponse
	 * @return objet Json en Java
	 */
	protected static <T> T readJsonResponse(String stringStream, Class<T> classEntityResponse){
		LOGGER.debug("Parsing de [{}] en {}", stringStream, classEntityResponse);
		return new Gson().fromJson(stringStream, classEntityResponse);
	}
}
