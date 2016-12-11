/**
 * 
 */
package com.terrier.boxcryptor.utils;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.junit.Test;

import com.terrier.utilities.automation.bundles.boxcryptor.objects.BCInventaireRepertoire;

/**
 * Test class for utils methods
 * @author vzwingma
 *
 */
public class TestCheckAvailabilityCallable {

	
	
	/**
	 * Test dump and load YML file of inventory
	 * @throws Exception 
	 */
	@Test
	public void testAvailability() throws Exception{
		System.setProperty("file.encoding","UTF-8");
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);
		
		
		File testDir = new File("src/test/resources");
		// Load
		BCInventaireRepertoire loadedInventory = BCUtils.loadYMLInventory(testDir.getPath());
		assertNotNull(loadedInventory);
		
		CheckAvailabilityCallable callable = new CheckAvailabilityCallable(loadedInventory, testDir.getAbsolutePath());
		callable.call();
		
	}
	
}
