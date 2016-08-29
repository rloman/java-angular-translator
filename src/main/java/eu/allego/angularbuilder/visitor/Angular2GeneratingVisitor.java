package eu.allego.angularbuilder.visitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;

public class Angular2GeneratingVisitor implements Visitor {

	private PrintStream currentOutputStream = System.out;

	public void visit(Component component) {
		setOutputStream(component);

		System.out.println("import {Component} from 'angular2/core'");

		for (Component child : component.getChildren()) {
			System.out.println("import {" + child.getName() + "Component} from './" + child.getName().toLowerCase()
					+ ".component'");
		}

		System.out.println();
		// render selector and template
		System.out.println("@Component({\n"
				+ "\tselector: '" + component.getSelector() + "', \n"
				+ "\ttemplate: `\n\t\t" + component.getTemplate());
		if (component.getTitle() != null && !component.getTitle().isEmpty()) {
			System.out.print("\t\t{{ title }}");
		}
		Map<String, List<Object>> listMap = component.getListMap();
		if (listMap != null && !listMap.isEmpty()) {
			StringBuilder builder = new StringBuilder();

			for (Entry<String, List<Object>> o : listMap.entrySet()) {
				builder.append("<ul>");
				builder.append("<li *ngFor='#element of " + o.getKey() + "'>");
				builder.append("{{ element }}");
				builder.append("</li>");
				builder.append("</ul>");
			}
			System.out.println(builder.toString());
		}
		// render subcomponents his selectors in the template
		for (Component child : component.getChildren()) {
			System.out.print("<" + child.getSelector() + "></" + child.getSelector() + ">");
		}
		System.out.print("\n\t\t`");
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

		// render the title if applicable
		if (component.getTitle() != null && !component.getTitle().isEmpty()) {
			System.out.printf("\ttitle: string = '%s'%n", component.getTitle());
		}

		// render the collection (as part of the training) if applicable
		for (Entry<String, List<Object>> element : component.getListMap().entrySet()) {
			System.out.print("\t" + element.getKey() + " = ");
			System.out.print("[");
			List<String> names = new ArrayList<>();
			for (Object o : element.getValue()) {
				names.add("'" + o.toString() + "'");
			}
			System.out.print(String.join(", ", names));
			System.out.println("]");
		}

		System.out.println("}");

		// render the (files) of the childer
		for (Component child : component.getChildren()) {
			child.accept(this);
		}

		for (Service service : component.getServices()) {
			service.accept(this);
		}

		resetOutputStream();
	}

	@Override
	public void visit(Service service) {
		setOutputStream(service);

		System.out.println();

		System.out.println("export class " + service.getName() + "Service {");
		System.out.println();

		service.getServiceMethod().accept(this);

		System.out.println("}");

		resetOutputStream();

	}

	@Override
	public void visit(ServiceMethod serviceMethod) {

		System.out.println("\t" + serviceMethod.getName() + " : " + serviceMethod.getReturnType() + " {");
		System.out.println("\t\t" + serviceMethod.getBody() + ";");
		System.out.println("\t}");

	}

	private void setOutputStream(Service service) {
		currentOutputStream = System.out;

		try {
			FileOutputStream outputStream = new FileOutputStream(
					"app/" + service.getName().toLowerCase() + ".service.ts");
			PrintStream ps = new PrintStream(outputStream);
			System.setOut(ps);

		}
		catch (FileNotFoundException e) {
			// rloman nog try with resources doen.
			e.printStackTrace();
		}

	}

	// this methods set the output to the files where it should be
	private void setOutputStream(Component component) {
		currentOutputStream = System.out;

		try {
			FileOutputStream outputStream = new FileOutputStream(
					"app/" + component.getName().toLowerCase() + ".component.ts");
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
