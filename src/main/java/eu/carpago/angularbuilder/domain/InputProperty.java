package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class InputProperty extends ComponentAttribute {

	public InputProperty(String name, String type) {
		super(name, type);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
