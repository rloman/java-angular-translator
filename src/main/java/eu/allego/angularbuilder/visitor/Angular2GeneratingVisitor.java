package eu.allego.angularbuilder.visitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import eu.allego.angularbuilder.domain.Button;
import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.ComponentAttribute;
import eu.allego.angularbuilder.domain.ComponentAttributeList;
import eu.allego.angularbuilder.domain.ComponentList;
import eu.allego.angularbuilder.domain.Constructor;
import eu.allego.angularbuilder.domain.Css;
import eu.allego.angularbuilder.domain.CustomPipe;
import eu.allego.angularbuilder.domain.CustomPipeList;
import eu.allego.angularbuilder.domain.Directive;
import eu.allego.angularbuilder.domain.DirectiveList;
import eu.allego.angularbuilder.domain.Div;
import eu.allego.angularbuilder.domain.Event;
import eu.allego.angularbuilder.domain.ITag;
import eu.allego.angularbuilder.domain.InlineStyle;
import eu.allego.angularbuilder.domain.InlineStyle.InlineStyleLine;
import eu.allego.angularbuilder.domain.InlineStyleList;
import eu.allego.angularbuilder.domain.InputField;
import eu.allego.angularbuilder.domain.InputProperty;
import eu.allego.angularbuilder.domain.OutputProperty;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;
import eu.allego.angularbuilder.domain.ServicesList;
import eu.allego.angularbuilder.domain.Template;
import eu.allego.angularbuilder.domain.TextField;
import eu.allego.angularbuilder.domain.Widget;

public class Angular2GeneratingVisitor implements Visitor {

	private PrintStream currentOutputStream = System.out;

	@Override
	public void visit(ComponentList componentList) {
		for (Component child : componentList) {
			System.out.println("import {" + child.getName() + "Component} from './" + child.getName().toLowerCase()
					+ ".component'");
		}
	}

	@Override
	public void visit(ServicesList servicesList) {

		for (Service service : servicesList) {
			System.out.println("import {" + service.getName() + "Service} from './" + service.getName().toLowerCase()
					+ ".service'");
		}
	}

	@Override
	public void visit(DirectiveList directiveList) {

		for (Directive directive : directiveList) {
			System.out.println("import {" + directive.getName() + "Directive} from './"
					+ this.convertUpperCamelCaseToAngularString(directive.getName()) + ".directive'");
		}
	}

	@Override
	public void visit(ComponentAttributeList componentAttributeList) {

		for (ComponentAttribute attribute : componentAttributeList) {
			attribute.accept(this);
		}
	}

	@Override
	public void visit(InlineStyleList inlineStyleList) {
		if (!inlineStyleList.isEmpty()) {
			System.out.println(",");
			System.out.println("\tstyles: [`");
			for (InlineStyle inlineStyle : inlineStyleList) {
				inlineStyle.accept(this);
			}
			System.out.println("\t`]");
		}
	}

	@Override
	public void visit(InlineStyle inlineStyle) {
		System.out.println(String.format("\t\t%s { ", inlineStyle.getStyleName().indexOf(":") == 0 ? inlineStyle.getStyleName() : "."+inlineStyle.getStyleName()));
		for (InlineStyleLine keyValue : inlineStyle) {
			System.out.printf("\t\t\t%s : %s;%n", keyValue.key, keyValue.value);
		}
		System.out.println("\t\t}");
	}
	
	
	@Override
	public void visit(CustomPipeList customPipeList) {
		for(CustomPipe pipe : customPipeList){
			pipe.accept(this);
		}
	}
	
	@Override
	public void visit(CustomPipe customPipe) {
		
		setOutputStream(customPipe);
		
		System.out.println("import {Pipe, PipeTransform} from 'angular2/core';");
		System.out.println();
		System.out.printf("@Pipe({name: '%s'})%n", customPipe.getName().toLowerCase());
		
		System.out.println("export class "+this.convertFirstCharacterToUppercase(customPipe.getName())+"Pipe implements PipeTransform {");
		
		System.out.println();
		System.out.println("\ttransform(value: string, args: string[]) {");
		
		System.out.println("\t\t// sample code");
		System.out.println("\t\tif(value){ ");
		System.out.println("\t\t\tvar limit = (args && args[0]) ? parseInt(args[0]) : 50;");
		System.out.println("\t\t\t return value.substring(0, limit) + '...';");
		System.out.println("\t\t}");
		
		System.out.println("\t}");
		
		System.out.println("}");
		
		resetOutputStream();
		
	}

	public void visit(Component component) {
		setOutputStream(component);

		// beetje exotisch maar wel een keer lekker / leuk :-)
		System.out.printf("import {Component%s%s} from 'angular2/core'%n",
				component.containsInputProperty() ? ", Input" : "",
				component.containsOutputProperty() ? ", Output, EventEmitter" : "");

		component.getChildren().accept(this);

		component.getServices().accept(this);

		component.getDirectives().accept(this);
		
		for(CustomPipe pipe : component.getPipes()) {
			System.out.println("import {"+this.convertFirstCharacterToUppercase(pipe.getName()+"Pipe} from './"+pipe.getName().toLowerCase()+".pipe'"));
		}

		System.out.println();
		// render header and selector
		System.out.println("@Component({");
		System.out.println("\tselector: '" + component.getSelector() + "', ");

		renderTemplate(component);

		if (!component.getServices().isEmpty()) {
			System.out.println(", ");
			System.out.print("\tproviders: [");
			System.out.print(String.join(", ", component.getServices().stream().map(e -> {
				return e.getName() + "Service";
			}).collect(Collectors.toList())));
			System.out.print("]");
		}

		// render subcomponent directives
		if (!component.getChildren().isEmpty())

		{
			System.out.println(", ");
			System.out.print("\tdirectives: [");

			System.out.print(String.join(", ", component
					.getChildren()
					.stream()
					.map(e -> {
						return e.getName() + "Component";
					})
					.collect(Collectors.toList())));

			System.out.print("]");
		}

		if (!component.getDirectives().isEmpty())

		{
			System.out.println(", ");
			System.out.print("\tdirectives: [");

			System.out.print(String.join(", ", component
					.getDirectives()
					.stream()
					.map(e -> {
						return e.getName() + "Directive";
					})
					.collect(Collectors.toList())));
			System.out.println("]");
		}
		
		if(!component.getPipes().isEmpty()) {
			System.out.println(", ");
			System.out.print("\tpipes: [");
			System.out.print(String.join(", ", component
					.getPipes()
					.stream()
					.map(e -> {
						return this.convertFirstCharacterToUppercase(e.getName()) + "Pipe";
					})
					.collect(Collectors.toList())));
			System.out.println("]");
		}

		component.getInlineStyles().accept(this);

		System.out.println("\n" + "})");

		System.out.println("export class " + component.getName() + "Component {");
		System.out.println();

		component.getAttributes().accept(this);

		// render the constructor (for now render the services and their call)
		if (!component.getServices().isEmpty())

		{
			System.out.print("\n\tconstructor(");

			System.out.print(String.join(", ", component
					.getServices()
					.stream()
					.map(e -> {
						return e.getName().toLowerCase() + "Service: " + e.getName() + "Service";
					})
					.collect(Collectors.toList())));

			System.out.println(") {");

			if (component.getConstructor() != null) {
				component.getConstructor().accept(this);
			}

			System.out.println("\n\t}");
		}

		// // render the properties of the conditional css of the widgets of
		// this
		// component if applicable recursively
		for (

		Widget widget : component.getTemplate().getWidgets())

		{
			recursiveRenderConditionCssStylesForWidget(widget);
		}

		// render the event handlers recursively
		for (

		Widget widget : component.getTemplate().getWidgets())

		{
			recursiveRenderEventHandlersForWidget(widget);
		}

		System.out.println("}");

		// render the (FILES) of the children
		// rloman might refactor all those component like component, service and
		// directive to one common base class
		for (

		Component child : component.getChildren())

		{
			child.accept(this);
		}

		for (

		Service service : component.getServices())

		{
			service.accept(this);
		}

		for (

		Directive directive : component.getDirectives())

		{
			directive.accept(this);
		}
		
		component.getPipes().accept(this);

		resetOutputStream();

	}

	private void renderTemplate(Component component) {
		if (component.getTemplate().isRenderTemplateFile()) {
			System.out.printf("\ttemplateUrl: '%s' %n",
					"app/" + this.convertUpperCamelCaseToAngularString(component.getName()) + ".template.html");
			setOutputStreamForExternalTemplate(component.getName());
		}
		else {
			System.out.println("\ttemplate: `\n\t\t");
		}

		component.getTemplate().accept(this);

		// since this entire project! is a learning project. here I will inspect
		// if it is a string[] for now
		for (ComponentAttribute attr : component.getAttributes()) {
			if ("string[]".equals(attr.getType().trim())) {
				StringBuilder builder = new StringBuilder();
				builder.append(String.format("<div *ngIf='%s.length > 0'>", attr.getName()));
				builder.append("<ul>");
				builder.append(String.format("<li *ngFor='#%s of %s'>",
						attr.getName().substring(0, attr.getName().length() - 1), attr.getName()));
				builder.append(String.format("{{ %s }}", attr.getName().substring(0, attr.getName().length() - 1)));
				builder.append("</li>");
				builder.append("</ul>");
				builder.append("</div>");
				builder.append(String.format("<div *ngIf='%s.length == 0'>", attr.getName()));
				builder.append(String.format("You don't have any %s yet", attr.getName()));
				builder.append("</div>");
				System.out.println(builder.toString());
			}

		}
		// render subcomponents his selectors in the template
		for (Component child : component.getChildren()) {
			List<String> namesOfInputProperties = new ArrayList<>();
			List<String> namesOfOutputProperties = new ArrayList<>();
			for (ComponentAttribute attr : child.getAttributes()) {
				if (attr instanceof InputProperty) {
					namesOfInputProperties.add(
							String.format("%s='%s'", attr.getName(), attr.getValue() != null ? attr.getValue() : ""));
				}
				if (attr instanceof OutputProperty) {
					namesOfOutputProperties
							.add(String.format("(%s)='%s'", attr.getName(),
									((OutputProperty) attr).getEventHandlerInJavascriptCode()));
				}

			}
			System.out.printf("\t\t<%s %s %s></%s>%n", child.getSelector(), String.join(", ", namesOfInputProperties),
					String.join(", ", namesOfOutputProperties), child.getSelector());
		}

		if (component.getTemplate().isRenderTemplateFile()) {
			resetOutputStream();
		}
		else {
			System.out.print("\t\t`");
		}
	}

	private void recursiveRenderConditionCssStylesForWidget(Widget widget) {
		for (Entry<Css, String> cssConditional : widget.getConditionalCssStyles().entrySet()) {
			System.out.println("\t// " + cssConditional.getValue() + " = true; // amend or remove if necessary");
		}
		System.out.println();
		for (Widget child : widget.getChildren()) {
			recursiveRenderConditionCssStylesForWidget(child);
		}
	}

	@Override
	public void visit(InputField inputField) {
		System.out.println("<br/>");

		System.out.println("\t\t\t<input type='text' [(ngModel)]='" + inputField.getNgModel().getName() + "' />");

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
					"\t\tconsole.log('You " + event.toString().toLowerCase() + "ed a "
							+ widget.getClass().getSimpleName() + " widget', $event);");
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
			System.out.printf("(%s)='on%s($event);' ", e.toString().toLowerCase(), widget.getClass().getSimpleName() +
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

	// perhaps refactor later to one Widget class with tagName (div, button) to
	// render in one visit method
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

	@Override
	public void visit(ITag itag) {
		System.out.println("<br/>");
		System.out.print("\t\t<i ");
		// this is too much repeating code since every concrete widget class
		// must render this... :-(
		renderCss(itag);
		renderConditionalCss(itag);
		renderEvents(itag);

		System.out.print(">");

		visit((Widget) itag);
		System.out.println("\t\t</i>");

	}

	// render the attributes (and maybe title may be removed now directly) from
	// the compnent
	@Override
	public void visit(ComponentAttribute componentAttribute) {
		System.out.println();
		System.out.printf("\t%s: %s %s ;%n", componentAttribute.getName(), componentAttribute.getType(),
				componentAttribute.getValue() != null ? String.format("= '%s'", componentAttribute.getValue()) : "");
	}

	@Override
	public void visit(InputProperty inputComponentAttribute) {
		System.out.println();
		System.out.printf("\t@Input()\n\t%s: %s;%n", inputComponentAttribute.getName(),
				inputComponentAttribute.getType());
	}

	@Override
	public void visit(OutputProperty outputComponentAttribute) {
		System.out.println();
		System.out.printf("\t@Output()\n\t%s %s;%n", outputComponentAttribute.getName(),
				"= new " + outputComponentAttribute.getType() + "()");

		// Add some code for the dummy implementation regarding the event
		// handler for this event emitter
		System.out.println();
		System.out.println(
				"\t// Add the emitting of the event somewhere. Perhaps here or in some other event handler (e.g. onClick)");
		System.out.println("\tonSomeEvent() {");
		System.out.println("\t\tthis." + outputComponentAttribute.getName()
				+ ".emit({newValue: 'some attribute of this component e.g.: \"this.isFavourite\" ' });");
		System.out.println("\t}");

	}

	@Override
	public void visit(TextField textField) {

		System.out.println("<br/>");
		String filters = String.join("", textField.getFilters().stream().map(filter -> filter.toString()).collect(Collectors.toList()));
		if(!textField.getCustomPipes().isEmpty()) {
			for(CustomPipe customPipe : textField.getCustomPipes()){
				String attr = customPipe.getAttributes();
				filters += " | "+customPipe.getName().toLowerCase() + (attr != null ? ":"+attr : "");
			}
		}
		System.out.printf("<span>%s: {{ %s %s }}</span> %n", textField.getLabel(), textField.getNgModel().getName(), filters);

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

	private void setOutputStreamForExternalTemplate(String componentName) {
		currentOutputStream = System.out;

		try {
			FileOutputStream outputStream = new FileOutputStream(
					"app/" + this.convertUpperCamelCaseToAngularString(componentName) + ".template.html");
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

	private void setOutputStream(CustomPipe customPipe) {
		currentOutputStream = System.out;
		try {
			FileOutputStream outputStream = new FileOutputStream(
					"app/" + this.convertUpperCamelCaseToAngularString(customPipe.getName()) + ".pipe.ts");
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
