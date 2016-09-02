package eu.allego.angularbuilder.visitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.allego.angularbuilder.domain.Button;
import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.ComponentAttribute;
import eu.allego.angularbuilder.domain.Constructor;
import eu.allego.angularbuilder.domain.Css;
import eu.allego.angularbuilder.domain.Directive;
import eu.allego.angularbuilder.domain.Div;
import eu.allego.angularbuilder.domain.Event;
import eu.allego.angularbuilder.domain.InputField;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;
import eu.allego.angularbuilder.domain.Template;
import eu.allego.angularbuilder.domain.TextField;
import eu.allego.angularbuilder.domain.Widget;

public class Angular2GeneratingVisitor implements Visitor {

	private PrintStream currentOutputStream = System.out;

	public void visit(Component component) {
		setOutputStream(component);

		System.out.println("import {Component} from 'angular2/core'");

		for (Component child : component.getChildren()) {
			System.out.println("import {" + child.getName() + "Component} from './" + child.getName().toLowerCase()
					+ ".component'");
		}

		for (Service service : component.getServices()) {
			System.out.println("import {" + service.getName() + "Service} from './" + service.getName().toLowerCase()
					+ ".service'");
		}
		for (Directive directive : component.getDirectives()) {
			System.out.println("import {" + directive.getName() + "Directive} from './"
					+ this.convertUpperCamelCaseToAngularString(directive.getName()) + ".directive'");
		}

		System.out.println();
		// render selector and template
		System.out.println("@Component({\n"
				+ "\tselector: '" + component.getSelector() + "', \n"
				+ "\ttemplate: `\n\t\t");
		if (component.getTitle() != null && !component.getTitle().isEmpty()) {
			System.out.print("\t\t{{ title }}");
		}
		component.getTemplate().accept(this);
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
			System.out.println("\t\t<" + child.getSelector() + "></" + child.getSelector() + ">");
		}
		System.out.print("\t\t`");

		if (!component.getServices().isEmpty()) {
			System.out.println(", ");
			System.out.print("\tproviders: [");
			List<String> names = new ArrayList<>();
			for (Service service : component.getServices()) {
				names.add(service.getName() + "Service");
				System.out.print(String.join(", ", names));
				System.out.print("]");
			}
		}
		List<String> names = new ArrayList<>();
		// render subcomponent directives
		if (!component.getChildren().isEmpty()) {
			System.out.println(", ");
			System.out.print("\tdirectives: [");

			// refactor to llambda later

			for (Component child : component.getChildren()) {
				names.add(child.getName() + "Component");
			}
			System.out.print(String.join(", ", names));

			System.out.print("]");
		}

		if (!component.getDirectives().isEmpty()) {
			System.out.println(", ");
			System.out.print("\tdirectives: [");

			for (Directive directive : component.getDirectives()) {
				names.add(directive.getName() + "Directive");
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
			System.out.println();
		}
		
		//render the real attributes (will remove the code above with title later)
		for(ComponentAttribute attribute : component.getAttributes()){
			attribute.accept(this);
		}

		// render the collection if applicable (as part of the training) if
		// applicable
		for (Entry<String, List<Object>> element : component.getListMap().entrySet()) {
			System.out.print("\t" + element.getKey() + " = ");
			System.out.print("[");
			{
				names = new ArrayList<>();
				for (Object o : element.getValue()) {
					names.add("'" + o.toString() + "'");
				}
				System.out.print(String.join(", ", names));
			}

			System.out.println("]");
		}

		// render the constructor (for now render the services and their call)
		if (!component.getServices().isEmpty()) {
			System.out.print("\n\tconstructor(");

			names = new ArrayList<>();
			for (Service service : component.getServices()) {
				names.add(service.getName().toLowerCase() + "Service: " + service.getName() + "Service");
			}
			System.out.print(String.join(", ", names));

			System.out.println(") {");

			if (component.getConstructor() != null) {
				component.getConstructor().accept(this);
			}

			System.out.println("\n\t}");
		}

		// // render the properties of the conditional css of the widgets of
		// this
		// component if applicable recursively
		for (Widget widget : component.getTemplate().getWidgets()) {
			recursiveRenderConditionCssStylesForWidget(widget);
		}

		// render the event handlers recursively
		for (Widget widget : component.getTemplate().getWidgets()) {
			recursiveRenderEventHandlersForWidget(widget);
		}

		System.out.println("}");

		// render the (FILES) of the children
		// rloman might refactor all those component like component, service and
		// directive to one common base class
		for (Component child : component.getChildren()) {
			child.accept(this);
		}

		for (Service service : component.getServices()) {
			service.accept(this);
		}

		for (Directive directive : component.getDirectives()) {
			directive.accept(this);
		}

		resetOutputStream();
	}

	private void recursiveRenderConditionCssStylesForWidget(Widget widget) {
		for (Entry<Css, String> cssConditional : widget.getConditionalCssStyles().entrySet()) {
			System.out.println("\t" + cssConditional.getValue() + " = true; // amend if necessary");
		}
		System.out.println();
		for (Widget child : widget.getChildren()) {
			recursiveRenderConditionCssStylesForWidget(child);
		}
	}
	
	@Override
	public void visit(InputField inputField) {
		System.out.println("<br/>");
		
		System.out.println("\t\t\t<input type='text' [(ngModel)]='"+inputField.getNgModel().getName()+"' />");
		
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

	private void recursiveRenderEventHandlersForWidget(Widget widget) {
		// render the template his event handling if applicable
		for (Event event : widget.getEvents()) {
			System.out.printf("\ton%s($event) {%n", widget.getClass().getSimpleName()
					+ this.convertFirstCharacterToUppercase(event.toString().toLowerCase()));
			// TODO rloman je zou hier ook nog iets kunne doen zodat
			// $event.stopPropagation(); // werkt. later.
			System.out.println(
					"\t\tconsole.log('You clicked a " + widget.getClass().getSimpleName() + " widget', $event);");
			System.out.println("\t}");
		}
		System.out.println();
		for (Widget child : widget.getChildren()) {
			recursiveRenderEventHandlersForWidget(child);
		}

	}

	@Override
	public void visit(ServiceMethod serviceMethod) {

		System.out.println("\t" + serviceMethod.getName() + " : " + serviceMethod.getReturnType() + " {");
		System.out.println("\t\t" + serviceMethod.getBody() + ";");
		System.out.println("\t}");

	}

	@Override
	public void visit(Constructor constructor) {

		System.out.print(constructor.getCode());
	}

	@Override
	public void visit(Directive directive) {

		setOutputStream(directive);

		System.out.println("import {Directive, ElementRef, Renderer} from 'angular2/core'");
		System.out.println();
		System.out.print("@Directive ({");
		System.out.println("selector: '[" + this.convertFirstCharacterToLowercase(directive.getName()) + "]',");
		System.out.println("\thost: {");
		List<String> names = new ArrayList<>();
		for (Event event : directive.getEvents()) {
			names.add("\t\t'(" + event.toString().toLowerCase() + ")'" + ": " + "'on"
					+ this.convertFirstCharacterToUppercase(event.toString().toLowerCase()) + "()'");

		}
		System.out.println(String.join(",\n", names));
		System.out.println("\t}");

		System.out.println();
		System.out.println("})");
		System.out.println("export class " + directive.getName() + "Directive {");

		// render the constructor for now using the Angular2 default settings
		// from Udemy
		System.out.println();
		System.out.println("\tconstructor(private el: ElementRef, private renderer: Renderer) {");
		System.out.println("\t}");
		System.out.println();

		for (Event event : directive.getEvents()) {
			int randomWidth = 100 + Double.valueOf((Math.random() * 100)).intValue();
			System.out.println("\ton" + this.convertFirstCharacterToUppercase(event.toString().toLowerCase()) + "(){");
			System.out.println("\t\t// Implement your event handling code here!");
			System.out.println("\t\t // Which might be something like this");
			System.out.printf("\t\tthis.renderer.setElementStyle(this.el.nativeElement, 'width', '%d');%n",
					randomWidth);

			System.out.print("\t}");
			System.out.println();
			System.out.println();
		}

		System.out.print("}");

		resetOutputStream();

	}

	@Override
	public void visit(Template template) {
		if (template.getTemplateString() != null) {
			System.out.println(template.getTemplateString());
		}
		else {
			for (Widget widget : template.getWidgets()) {
				widget.accept(this);
			}
		}

	}

	@Override
	public void visit(Widget widget) {
		for (Widget child : widget.getChildren()) {
			child.accept(this);
		}
	}

	// en alsl het meer dan 1 event is / wordt?
	/*
	 * This method renders the (click)='onClick' part of the event of the
	 * widget. NOt to be confused with the latter implementation of the onClick
	 * event handler
	 */
	private void renderEvents(Widget widget) {
		for (Event e : widget.getEvents()) {
			System.out.printf("(%s)='on%s($event);'", e.toString().toLowerCase(), widget.getClass().getSimpleName() +
					this.convertFirstCharacterToUppercase(e.toString().toLowerCase()));
		}
	}

	private void renderCss(Widget widget) {
		if (!widget.getCssStyles().isEmpty()) {
			System.out.print(" class='");
			List<String> names = new ArrayList<>();

			for (Css css : widget.getCssStyles()) {
				names.add(this.convertUpperCamelCaseToAngularString(css.toString()));
			}
			System.out.print(String.join(" ", names));
			System.out.print("' ");
		}
	}

	private void renderConditionalCss(Widget widget) {
		if (!widget.getConditionalCssStyles().isEmpty()) {

			for (Entry<Css, String> cssConditionalEntry : widget.getConditionalCssStyles().entrySet()) {
				System.out.printf(" [class.%s]='%s' ",
						this.convertUpperCamelCaseToAngularString(cssConditionalEntry.getKey().toString()),
						cssConditionalEntry.getValue());
			}
		}

	}

	@Override
	public void visit(Button button) {
		System.out.println("<br/>");
		System.out.print("\t\t\t<button ");

		renderCss(button);
		renderConditionalCss(button);
		renderEvents(button);

		System.out.println(">" + button.getLabel());
		visit((Widget) button);
		System.out.println("\t\t\t</button>");
	}

	@Override
	public void visit(Div div) {
		System.out.println("<br/>");
		System.out.print("\t\t<div ");

		// this is too much repeating code since every concrete widget class
		// must render this... :-(
		renderCss(div);
		renderConditionalCss(div);
		renderEvents(div);

		System.out.print(">");
		visit((Widget) div);
		System.out.println("\t\t</div>");

	}
	
	// render the attributes (and maybe title may be removed now directly) from the compnent
	@Override
	public void visit(ComponentAttribute componentAttribute) {
		System.out.println();
		System.out.printf("\t%s: %s = '%s';%n", componentAttribute.getName(), componentAttribute.getType(), componentAttribute.getValue() != null ? componentAttribute.getValue() : "");
	}
	
	@Override
	public void visit(TextField textField) {
		
		System.out.println("<br/>");
		System.out.println(textField.getLabel()+": {{"+textField.getNgModel().getName()+"}}");
		
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

	private void setOutputStream(Directive directive) {
		currentOutputStream = System.out;

		try {
			FileOutputStream outputStream = new FileOutputStream(
					"app/" + this.convertUpperCamelCaseToAngularString(directive.getName()) + ".directive.ts");
			PrintStream ps = new PrintStream(outputStream);
			System.setOut(ps);

		}
		catch (FileNotFoundException e) {
			// rloman nog try with resources doen.
			e.printStackTrace();
		}

	}

	String convertFirstCharacterToLowercase(String input) {
		String output = Character.toLowerCase(input.charAt(0)) +
				(input.length() > 1 ? input.substring(1) : "");

		return output;

	}

	String convertFirstCharacterToUppercase(String input) {
		String output = Character.toUpperCase(input.charAt(0)) +
				(input.length() > 1 ? input.substring(1) : "");

		return output;
	}

	// e.g. convert AutoGrow to auto-grow
	String convertUpperCamelCaseToAngularString(String input) {

		return this.convertFirstCharacterToLowercase(input).replaceAll("([A-Z])", "-$1").toLowerCase();

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
