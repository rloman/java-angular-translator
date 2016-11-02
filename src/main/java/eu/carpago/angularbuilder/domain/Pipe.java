package eu.carpago.angularbuilder.domain;

public enum Pipe {

	uppercase, lowercase, number("'2.2-2'") {
	},
	date("'shortDate'"), // longDate
	json, 
	currency("'AUD':true:'2.2-2'");

	private String attr;

	private Pipe() {

	}

	private Pipe(String attr) {
		this.attr = attr;
	}

	@Override
	public String toString() {
		return " | "+name() + (attr!= null ? ":" + attr : "");
	}

}
