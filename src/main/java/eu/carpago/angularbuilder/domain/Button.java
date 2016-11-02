package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class Button extends Widget {
	
	public Button(String label) {
		super(label);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
