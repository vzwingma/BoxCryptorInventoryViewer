/**
 * 
 */
package com.terrier.boxcryptor.service.available.hubic.objects;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.internal.LinkedTreeMap;

/**
 * @author vzwingma
 *
 */
public class HubicAPIContent {

	String name;

	
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Chargement des contents
	 * @param containers
	 * @return liste des contenus
	 */
	public static List<HubicAPIContent> getContents(List<LinkedTreeMap<String, String>> containers){		
		List<HubicAPIContent> listeContents = new ArrayList<>();
		for (LinkedTreeMap<String, String> object : containers) {
			listeContents.add(HubicAPIContent.getContent(object));
		}
		return listeContents;
	}
	
	/**
	 * Mapping des attributs en content Hubic
	 * @param mapAttributes
	 * @return content
	 */
	public static HubicAPIContent getContent(LinkedTreeMap<String, String> mapAttributes){
		HubicAPIContent hubicAPIContent = new HubicAPIContent();
		hubicAPIContent.setName(mapAttributes.get("name"));
		return hubicAPIContent;
	}
}
