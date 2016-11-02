package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public abstract class Node {

	public abstract void accept(Visitor visitor);

}
