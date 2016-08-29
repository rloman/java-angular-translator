package eu.allego.angularbuilder.visitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import eu.allego.angularbuilder.domain.Component;

public class Angular2GeneratingVisitor implements Visitor {
	
	private PrintStream currentOutputStream = System.out;
	
	
	public void visit(Component component) {
		setOutputStream(component);

		System.out.println("import {Component} from 'angular2/core'");

		for (Component child : component.getChildren()) {
			System.out.println("import {" + child.getName() + "Component} from './"+child.getName().toLowerCase()+".component'");
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
		
		// render the (files) of the childer
		for(Component child : component.getChildren()) {
			child.accept(this);
		}

		resetOutputStream();
	}
	
	// this methods set the output to the files where it should be
		private void setOutputStream(Component component) {
			currentOutputStream = System.out;

			try {
				FileOutputStream outputStream = new FileOutputStream("app/" + component.getName().toLowerCase() + ".component.ts");
				PrintStream ps = new PrintStream(outputStream);
				System.setOut(ps);

			}
			catch (FileNotFoundException e) {
				// rloman nog try with resources doen.
				e.printStackTrace();
			}

			
		}
		
		// resets the outputstream to the previous outputstream
		private void resetOutputStream() {
			System.setOut(currentOutputStream);
		}

}
