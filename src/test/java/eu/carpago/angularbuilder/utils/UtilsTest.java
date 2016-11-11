package eu.carpago.angularbuilder.utils;

import org.junit.Assert;
import org.junit.Test;

import eu.carpago.angularbuilder.utils.Utils;


public class UtilsTest {
	
	@Test
	public void testConvertFirstCharacterToLowercase() {
		Assert.assertEquals("jansen", Utils.convertFirstCharacterToLowercase("Jansen"));
		Assert.assertEquals("janSen", Utils.convertFirstCharacterToLowercase("JanSen"));
	}
	
	@Test
	public void testConvertFirstCharacterToUppercase() {
		Assert.assertEquals("Jansen", Utils.convertFirstCharacterToUppercase("jansen"));
		Assert.assertEquals("JanSen", Utils.convertFirstCharacterToUppercase("janSen"));
	}
	
	@Test
	public void testconvertUpperCamelCaseToAngularString() {
		
		Assert.assertEquals("auto-grow", Utils.convertUpperCamelCaseToAngularString("AutoGrow"));
		
	}

}
