package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public abstract class Node {

	public abstract void accept(Visitor visitor);

}
