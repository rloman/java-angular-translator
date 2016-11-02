package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class Div extends Widget {

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
	}

}
