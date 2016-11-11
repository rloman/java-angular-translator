package eu.carpago.angularbuilder.utils;

public abstract class Utils {
	
	private Utils() {
		
	}
	
	public static String convertFirstCharacterToLowercase(String input) {
		String output = Character.toLowerCase(input.charAt(0)) + (input.length() > 1 ? input.substring(1) : "");

		return output;

	}
	
	

}
