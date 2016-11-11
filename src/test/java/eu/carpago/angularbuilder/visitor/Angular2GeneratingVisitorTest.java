package eu.carpago.angularbuilder.visitor;

import org.junit.Assert;
import org.junit.Test;

import eu.carpago.angularbuilder.utils.Utils;


public class Angular2GeneratingVisitorTest {
	
	private Angular2GeneratingVisitor visitor = new Angular2GeneratingVisitor();
	
	@Test
	public void testConvertFirstCharacterToLowercase() {
		Assert.assertEquals("jansen", Utils.convertFirstCharacterToLowercase("Jansen"));
		Assert.assertEquals("janSen", Utils.convertFirstCharacterToLowercase("JanSen"));
	}
	
	@Test
	public void testConvertFirstCharacterToUppercase() {
		Assert.assertEquals("Jansen", visitor.convertFirstCharacterToUppercase("jansen"));
		Assert.assertEquals("JanSen", visitor.convertFirstCharacterToUppercase("janSen"));
	}
	
	@Test
	public void testconvertUpperCamelCaseToAngularString() {
		
		Assert.assertEquals("auto-grow", visitor.convertUpperCamelCaseToAngularString("AutoGrow"));
		
	}

}
