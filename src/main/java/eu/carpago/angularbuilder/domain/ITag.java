package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class ITag extends Widget {

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
	}

}
