package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class ComponentAttribute extends Node {

	private String name;
	private String type;
	private String value;
	
	private boolean inputProperty = false;
	private boolean outputProperty = false;

	public ComponentAttribute(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public ComponentAttribute(String name, String type, String value) {
		this(name, type);
		this.value = value;
	}

	public boolean isInputProperty() {
		return inputProperty;
	}
	
	public boolean isOutputProperty() {
		return this.outputProperty;
	}

	public void setInputProperty(boolean inputProperty) {
		this.inputProperty = inputProperty;
	}
	
	public void setOutputProperty(boolean outputProperty) {
		this.outputProperty = outputProperty;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
