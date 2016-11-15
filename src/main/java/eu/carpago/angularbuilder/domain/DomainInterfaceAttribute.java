package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class DomainInterfaceAttribute extends Node {

	private String name;
	private String type;
	private boolean mandatory;

	public DomainInterfaceAttribute(String name, String type, boolean mandatory) {
		this.name = name;
		this.type = type;
		this.mandatory = mandatory;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}