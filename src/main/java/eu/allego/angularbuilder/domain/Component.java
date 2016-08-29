package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class Component implements Node {
	
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
