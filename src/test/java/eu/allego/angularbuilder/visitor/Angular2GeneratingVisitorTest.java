package eu.allego.angularbuilder.visitor;

import org.junit.Assert;
import org.junit.Test;


public class Angular2GeneratingVisitorTest {
	
	private Angular2GeneratingVisitor visitor = new Angular2GeneratingVisitor();
	
	@Test
	public void testConvertFirstCharacterToLowercase() {
		Assert.assertEquals("jansen", visitor.convertFirstCharacterToLowercase("Jansen"));
		Assert.assertEquals("janSen", visitor.convertFirstCharacterToLowercase("JanSen"));
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
