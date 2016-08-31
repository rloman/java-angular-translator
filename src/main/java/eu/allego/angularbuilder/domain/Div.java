package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class Div extends Widget {

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
	}

}
