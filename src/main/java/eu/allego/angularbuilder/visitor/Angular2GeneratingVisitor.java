package eu.allego.angularbuilder.visitor;

import java.util.ArrayList;
import java.util.List;

import eu.allego.angularbuilder.domain.Component;

public class Angular2GeneratingVisitor implements Visitor {

	public void visit(Component component) {

		System.out.println("import {Component} from 'angular2/core'");

		for (Component child : component.getChildren()) {
			System.out.println("import {" + child.getName() + "Component} from ./courses.component");
		}

		System.out.println();
		System.out.print("@Component({\n"
				+ "\tselector: '" + component.getSelector() + "', \n"
				+ "\ttemplate: '" + component.getTemplate());
		// render subcomponents his selectors
		for (Component child : component.getChildren()) {
			System.out.print("<" + child.getSelector() + "></" + child.getSelector() + ">");
		}
		System.out.print("'");
		// render subcomponent directives
		if (!component.getChildren().isEmpty()) {
			System.out.println(", ");
			System.out.print("\tdirectives: [");

			// refactor to llambda later
			List<String> names = new ArrayList<>();
			for (Component child : component.getChildren()) {
				names.add(child.getName() + "Component");
			}
			System.out.print(String.join(", ", names));

			System.out.println("]");
		}
		System.out.println("\n" + "})");

		System.out.println("export class " + component.getName() + "Component {");
		System.out.println();

		System.out.println("}");

	}

}
