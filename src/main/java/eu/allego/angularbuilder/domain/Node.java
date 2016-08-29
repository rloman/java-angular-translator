package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public interface Node {

	void accept(Visitor visitor);

}
