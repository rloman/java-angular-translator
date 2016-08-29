package eu.allego.angularbuilder;

import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.Node;
import eu.allego.angularbuilder.visitor.Angular2GeneratingVisitor;
import eu.allego.angularbuilder.visitor.Visitor;

public class Application {
	public static void main(String[] args) {
		
		Node component = new Component();
		
		Visitor v = new Angular2GeneratingVisitor();
		
		component.accept(v);
		
	}
}
