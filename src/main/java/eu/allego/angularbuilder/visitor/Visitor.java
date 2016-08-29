package eu.allego.angularbuilder.visitor;

import eu.allego.angularbuilder.domain.Component;

public interface Visitor {

	void visit(Component component);

}
