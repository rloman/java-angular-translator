package eu.carpago.angularbuilder.utils;

public abstract class Utils {

	private Utils() {
	}

	public static String convertFirstCharacterToLowercase(String input) {
		String output = Character.toLowerCase(input.charAt(0)) + (input.length() > 1 ? input.substring(1) : "");

		return output;
	}

	public static String convertFirstCharacterToUppercase(String input) {
		String output = Character.toUpperCase(input.charAt(0)) + (input.length() > 1 ? input.substring(1) : "");

		return output;
	}

	// e.g. convert AutoGrow to auto-grow
	public static String convertUpperCamelCaseToAngularString(String input) {

		return Utils.convertFirstCharacterToLowercase(input).replaceAll("([A-Z])", "-$1").toLowerCase();
	}
}
