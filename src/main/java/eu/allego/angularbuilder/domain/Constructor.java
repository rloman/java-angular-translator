package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class Constructor extends Node {

	private String code;

	public Constructor(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
