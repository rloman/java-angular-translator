package eu.allego.angularbuilder.domain;

public enum Filter {

	uppercase, lowercase, number("'2.2-2'") {
	},
	date("'shortDate'"), // longDate
	json,
	currency("'AUD':true:'2.2-2'");

	private String attr;

	private Filter() {

	}

	private Filter(String attr) {
		this.attr = attr;
	}

	public String toString() {
		return " | "+name() + (attr!= null ? ":" + attr : "");
	}

}
