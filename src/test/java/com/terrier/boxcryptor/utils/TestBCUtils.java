/**
 * 
 */
package com.terrier.boxcryptor.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireRepertoire;

/**
 * Test class for utils methods
 * @author vzwingma
 *
 */
public class TestBCUtils {

	
	/**
	 * Test of multiple split
	 */
	@Test
	public void testMulitpleSplit(){
		String valueToSplit = "recherche de tous_ces_termes";
		String[] valuesSplitted = valueToSplit.split(BCUtils.SPLIT_REGEX);
		assertEquals(5, valuesSplitted.length);
	}
	

	
	
	/**
	 * Test d'inventaire
	 */
	@Test
	public void testSearchTermsInInventoryDir(){
		BCInventaireRepertoire testInventory = new BCInventaireRepertoire("倐徎婢冈呹忪僣庝左勓嗊宽坻墒受䀊", "nom en clair_et_attaché");
		
		// Test null
		assertTrue(BCUtils.searchTermsInInventory(testInventory, null));
		assertTrue(BCUtils.searchTermsInInventory(testInventory, ""));
		
		// Test false search
		assertFalse(BCUtils.searchTermsInInventory(testInventory, "clari"));
		assertFalse(BCUtils.searchTermsInInventory(testInventory, "倐彈删媘娺咴囦坂圤婷"));
		
		// Test one word
		assertTrue(BCUtils.searchTermsInInventory(testInventory, "clair"));
		// Test multiple word
		assertTrue(BCUtils.searchTermsInInventory(testInventory, "clair attaché"));

		// Test false multiple word 
		assertFalse(BCUtils.searchTermsInInventory(testInventory, "clair non attaché"));
	}
}
