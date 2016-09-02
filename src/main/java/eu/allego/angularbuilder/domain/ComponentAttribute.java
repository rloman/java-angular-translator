package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class ComponentAttribute extends Node {
	
	private String name;
	private String type;

	
	public ComponentAttribute(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);

	}

}
