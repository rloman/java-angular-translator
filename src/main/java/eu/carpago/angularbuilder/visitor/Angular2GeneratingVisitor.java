package eu.carpago.angularbuilder.visitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import eu.carpago.angularbuilder.domain.Button;
import eu.carpago.angularbuilder.domain.Component;
import eu.carpago.angularbuilder.domain.ComponentAttribute;
import eu.carpago.angularbuilder.domain.ComponentAttributeList;
import eu.carpago.angularbuilder.domain.ComponentList;
import eu.carpago.angularbuilder.domain.Constructor;
import eu.carpago.angularbuilder.domain.Crud;
import eu.carpago.angularbuilder.domain.Css;
import eu.carpago.angularbuilder.domain.CustomPipe;
import eu.carpago.angularbuilder.domain.CustomPipeList;
import eu.carpago.angularbuilder.domain.Directive;
import eu.carpago.angularbuilder.domain.DirectiveList;
import eu.carpago.angularbuilder.domain.Div;
import eu.carpago.angularbuilder.domain.DomainDrivenDevelopment;
import eu.carpago.angularbuilder.domain.DomainInterface;
import eu.carpago.angularbuilder.domain.DomainService;
import eu.carpago.angularbuilder.domain.Event;
import eu.carpago.angularbuilder.domain.ITag;
import eu.carpago.angularbuilder.domain.InlineStyle;
import eu.carpago.angularbuilder.domain.InlineStyleList;
import eu.carpago.angularbuilder.domain.InputField;
import eu.carpago.angularbuilder.domain.InputProperty;
import eu.carpago.angularbuilder.domain.OutputProperty;
import eu.carpago.angularbuilder.domain.RestDomainService;
import eu.carpago.angularbuilder.domain.Service;
import eu.carpago.angularbuilder.domain.ServiceMethod;
import eu.carpago.angularbuilder.domain.ServicesList;
import eu.carpago.angularbuilder.domain.Template;
import eu.carpago.angularbuilder.domain.TextField;
import eu.carpago.angularbuilder.domain.Widget;
import eu.carpago.angularbuilder.domain.InlineStyle.InlineStyleLine;

public class Angular2GeneratingVisitor implements Visitor {

	private PrintStream currentOutputStream = System.out;
	
	@Override
	public void visit(DomainDrivenDevelopment domainDrivenDevelopment) {
		domainDrivenDevelopment.getAppComponent().accept(this);
	}

	@Override
	public void visit(RestDomainService service) {

		setOutputStream(service);

		System.out.println("import {Http} from 'angular2/http';");
		System.out.println("import 'rxjs/add/operator/map';");
		System.out.println("import {Observable} from 'rxjs/Observable';");
		System.out.println("import {Injectable} from 'angular2/core';");
		System.out.println();

		DomainInterface domain = service.getDomainInterface();
		String name = this.convertFirstCharacterToUppercase(domain.getSingularPascalcaseName());
		String smallName = this.convertFirstCharacterToLowercase(name);

		System.out.printf("import {%s} from './%s';%n", name, smallName);

		System.out.println("import {Headers, RequestOptions, RequestMethod, Request, Response} from 'angular2/http';");

		System.out.println();

		System.out.println("@Injectable()");
		System.out.println("export class " + service.getName() + "Service {");
		System.out.println();
		System.out.println();

		System.out.println("private headers: Headers;");
		System.out.println("private requestoptions: RequestOptions;");

		System.out.printf("private url: string = '%s';%n", service.getBaseUrl());

		System.out.println();

		System.out.println("\tconstructor(private http:Http) {");

		System.out.println("\t}");

		System.out.println();

		// create the service method for the plular list
		System.out.printf("\tget%ss() : Observable<%s[]> {%n", name, name);
		System.out.printf("\t\treturn this.http.get(%s)%n", "this.url");
		System.out.println("\t\t\t.map(res => res.json());");
		System.out.println("\t}");

		System.out.println();

		// create the sevice method for the singular get
		System.out.printf("\tget%s(id : number) : Observable<%s> {%n", name, name);
		System.out.printf("\t\treturn this.http.get(%s+id)%n", "this.url");
		System.out.println("\t\t\t.map(res => res.json());");
		System.out.println("\t}");

		System.out.println();

		// create
		System.out.printf("\tcreate(%s) {%n", smallName);
		System.out.println("\t\tthis.headers = new Headers();");
		System.out.println("\t\tthis.headers.append('Content-Type', 'application/json');");
		System.out.println("\t\tthis.requestoptions = new RequestOptions({");
		System.out.println("\t\t\tmethod: RequestMethod.Post,");
		System.out.println("\t\t\turl: this.url,");
		System.out.println("\t\t\theaders: this.headers,");
		System.out.printf("\t\t\tbody: JSON.stringify(%s)%n", smallName);
		System.out.println("\t\t})");
		System.out.println("\t\treturn this.http.request(new Request(this.requestoptions))");
		System.out.println("\t\t\t.map((res: Response) => {");
		System.out.println("\t\t\t\tif (res) {");
		System.out.println("\t\t\t\t\tconsole.log(res);");
		System.out.println("\t\t\t\t\treturn [{ status: res.status, json: res.json() }]");
		System.out.println("\t\t\t\t}");
		System.out.println("\t\t\t});");
		System.out.println("\t}");

		System.out.println();

		// update
		System.out.printf("\tupdate(%s) : Observable<%s>{%n", smallName, name);
		System.out.println("\t\tthis.headers = new Headers();");
		System.out.println("\t\tthis.headers.append('Content-Type', 'application/json');");
		System.out.println("\t\tthis.requestoptions = new RequestOptions({");
		System.out.println("\t\t\tmethod: RequestMethod.Put,");
		System.out.printf("\t\t\turl: this.url+%s.id,%n", smallName);
		System.out.println("\t\t\theaders: this.headers,");
		System.out.printf("\t\t\tbody: JSON.stringify(%s)%n", smallName);
		System.out.println("\t\t})");
		System.out.println("\t\treturn this.http.request(new Request(this.requestoptions))");
		System.out.println("\t\t\t.map((res: Response) => {");
		System.out.println("\t\t\tif (res) {");
		System.out.println("\t\t\t\tconsole.log(res);");
		System.out.println("\t\t\t\treturn [{ status: res.status, json: res.json() }]");
		System.out.println("\t\t\t}");
		System.out.println("\t\t});");
		System.out.println("\t}");

		System.out.println();

		// delete
		System.out.printf("\tdelete(%s) : Observable<boolean> {%n", smallName);
		System.out.println("\t\tthis.headers = new Headers();");
		System.out.println("\t\tthis.headers.append('Content-Type', 'application/json');");
		System.out.println();
		System.out.printf("\t\treturn this.http.delete(this.url+%s.id)%n", smallName);
		System.out.println("\t\t\t.map(result => result.json());");

		System.out.println("\t}");

		// close class
		System.out.println("}");

		resetOutputStream();

		service.getDomainInterface().accept(this);
	}

	@Override
	public void visit(DomainService service) {
		setOutputStream(service);

		DomainInterface domain = service.getDomainInterface();
		String name = this.convertFirstCharacterToUppercase(domain.getSingularPascalcaseName());

		// imports
		// rloman: refactor to StatementImport.java for later
		System.out.printf("import {%s} from './%s';%n", name, this.convertFirstCharacterToLowercase(name));

		System.out.println();

		System.out.println("export class " + service.getName() + "Service {");
		System.out.println();

		System.out.printf("\tget%ss() : %s[] {%n", name, name);

		System.out.println("\t\t// Implement your code here");

		System.out.println("\t\treturn [{id:3, title:'aap'},{id:4, title:'noot'}, {id:5, title:'mies'}]");
		System.out.println("\t}");

		System.out.printf("\tcreate%s(post:Post) {%n", name);

		System.out.println("\t\t// Implement your code here");
		System.out.println("\t}");

		System.out.println("}");

		resetOutputStream();

		service.getDomainInterface().accept(this);

	}

	@Override
	public void visit(DomainInterface domainInterface) {
		setOutputStream(domainInterface);

		System.out.printf("export interface %s {%n", domainInterface.getSingularPascalcaseName());
		String attributes = String.join("", domainInterface.getAttributes().stream().map(e -> {
			return String.format("\t %s : %s;%n", e.name, e.type);
		}).collect(Collectors.toList()));
		System.out.print(attributes);

		System.out.println("}");

		resetOutputStream();

	}

	@Override
	public void visit(ComponentList componentList) {
		for (Component child : componentList) {
			renderChildersImportRecursive(child);

		}
	}

	private void renderChildersImportRecursive(Component child) {
		System.out.println("import {" + child.getName() + "Component} from './"
				+ this.convertUpperCamelCaseToAngularString(child.getName()) + ".component'");
		for (Component subchild : child.getChildren()) {
			renderChildersImportRecursive(subchild);
		}
	}

	@Override
	public void visit(ServicesList servicesList) {

		for (Service service : servicesList) {
			System.out.println("import {" + service.getName() + "Service} from './" + service.getName().toLowerCase()
					+ ".service'");
			if (service instanceof DomainService) {
				DomainService domainService = (DomainService) service;
				System.out.println("import {" + domainService.getDomainInterface().getSingularPascalcaseName() + "} from './"
						+ domainService.getDomainInterface().getSingularPascalcaseName().toLowerCase() + "';");
				if (service instanceof RestDomainService) {
					System.out.println("import {HTTP_PROVIDERS} from 'angular2/http';");
				}
			}

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
		System.out.println(String.format("\t\t%s { ", inlineStyle.getStyleName().indexOf(":") == 0
				? inlineStyle.getStyleName() : "." + inlineStyle.getStyleName()));
		for (InlineStyleLine keyValue : inlineStyle) {
			System.out.printf("\t\t\t%s : %s;%n", keyValue.key, keyValue.value);
		}
		System.out.println("\t\t}");
	}

	@Override
	public void visit(CustomPipeList customPipeList) {
		for (CustomPipe pipe : customPipeList) {
			pipe.accept(this);
		}
	}

	@Override
	public void visit(CustomPipe customPipe) {

		setOutputStream(customPipe);

		System.out.println("import {Pipe, PipeTransform} from 'angular2/core';");
		System.out.println();
		System.out.printf("@Pipe({name: '%s'})%n", customPipe.getName().toLowerCase());

		System.out.println("export class " + this.convertFirstCharacterToUppercase(customPipe.getName())
				+ "Pipe implements PipeTransform {");

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

	@Override
	public void visit(Component component) {
		setOutputStream(component);

		String pascalCaseName = component.getName();
		String camelCaseName = this.convertFirstCharacterToLowercase(pascalCaseName);

		// beetje exotisch maar wel een keer lekker / leuk :-)
		System.out.printf("import {Component%s%s} from 'angular2/core'%n",
				component.containsInputProperty() ? ", Input" : "",
				component.containsOutputProperty() ? ", Output, EventEmitter" : "");

		component.getChildren().accept(this);

		component.getServices().accept(this);

		component.getDirectives().accept(this);

		for (CustomPipe pipe : component.getPipes()) {
			System.out.println("import {" + this.convertFirstCharacterToUppercase(
					pipe.getName() + "Pipe} from './" + pipe.getName().toLowerCase() + ".pipe'"));
		}

		// for now always import the router related stuff
		System.out.println("import {RouteConfig, RouterOutlet, RouterLink, RouteParams} from 'angular2/router';");
		System.out.println("import {ROUTER_DIRECTIVES} from 'angular2/router';");

		// enable routing if applicable
		if (component.isEnableRouting()) {

			System.out.println("@RouteConfig(");

			System.out.println("\t[");

			renderPathExpressionsRecursively(component);

			// System.out.printf("\t\t{path:'/*other', name:'Other', redirectTo:
			// ['%s']}", destinationWhenInvalidTarget);

			System.out.println("\t]");

			System.out.println(")");
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
				if (e instanceof RestDomainService) {
					// kan dit niet anders / in 1 regel? volgens mij kan
					// dat in
					// een aparte map. namelijk
					return e.getName() + "Service, HTTP_PROVIDERS";
				} else {
					return e.getName() + "Service";
				}

			}).distinct().collect(Collectors.toList())));
			System.out.print("]");
		}

		// render subcomponent directives
		// mag dit wel twee keer? (hieronder wordt twee keer directives
		// gerendered)
		if (!component.getChildren().isEmpty()) {
			System.out.println(", ");
			System.out.print("\tdirectives: [");

			System.out.print(String.join(", ", component.getChildren().stream().map(e -> {
				return e.getName() + "Component";
			}).collect(Collectors.toList())));

			// rloman deze imports zijn niet altijd nodigen falen wellicht als
			// het comonent geen router enabled is
			System.out.print(", RouterOutlet, RouterLink]");
		}

		if (!component.getDirectives().isEmpty()) {
			System.out.println(", ");
			System.out.print("\tdirectives: [");

			System.out.print(String.join(", ", component.getDirectives().stream().map(e -> {
				return e.getName() + "Directive";
			}).collect(Collectors.toList())));
			System.out.println("]");
		}

		System.out.println(", ");
		System.out.print("\tdirectives: [ROUTER_DIRECTIVES]");

		if (!component.getPipes().isEmpty()) {
			System.out.println(", ");
			System.out.print("\tpipes: [");
			System.out.print(String.join(", ", component.getPipes().stream().map(e -> {
				return this.convertFirstCharacterToUppercase(e.getName()) + "Pipe";
			}).collect(Collectors.toList())));
			System.out.println("]");
		}

		component.getInlineStyles().accept(this);

		System.out.println("\n" + "})");

		System.out.println("export class " + component.getName() + "Component {");
		System.out.println();

		component.getAttributes().accept(this);

		// render the constructor (for now render the services and their call)
		if (!component.getServices().isEmpty()) {
			System.out.print("\n\tconstructor(");

			System.out.print(String.join(", ", component.getServices().stream().map(e -> {
				return "private " + e.getName().toLowerCase() + "Service: " + e.getName() + "Service";
			}).collect(Collectors.toList())));
			// always???? rloman
			if (component.isForSingularUse()) {
				System.out.print(", private routeParams :RouteParams");
			}
			System.out.println(") {");

			if (component.getConstructor() != null) {
				component.getConstructor().accept(this);
			}

			System.out.println("\n\t}");
		}

		// render the create method if applicable
		if (component.getCrud() != null) {
			for (Crud element : component.getCrud()) {
				switch (element) {
				case CREATE:
					System.out.println("create() {");
					System.out.printf("\tthis.%sService.create(this.%s).subscribe(%n", component.getDomain().getSingularCamelcaseName(), component.getDomain().getSingularCamelcaseName());
					System.out.println("\t\tresponse => console.log(response)" + ");");
					System.out.println("\t}");

					break;
				case DELETE:
					System.out.println();
					camelCaseName = camelCaseName.substring(0, camelCaseName.length() - 1);
					pascalCaseName = pascalCaseName.substring(0, pascalCaseName.length() - 1);
					System.out.printf("\tdelete(%s: %s) {%n", camelCaseName, pascalCaseName);
					System.out.printf("\t\tthis.%sService.delete(%s)%n", camelCaseName, camelCaseName);
					System.out.println("\t\t.subscribe(result => {");
					System.out.printf("\t\t\tthis.warning = '%s with id '+%s.id+' deleted!';%n", pascalCaseName,
							camelCaseName);
					System.out.println("\t\t\t}");
					System.out.println("\t\t);");
					System.out.println("\t}");

					break;
				case UPDATE:
					System.out.println();
					System.out.println("\tupdate() {");
					System.out.printf("\t\tthis.%sService.update(this.%s).subscribe(res => {%n", camelCaseName,
							camelCaseName);
					System.out.println("\t\t\tconsole.log(res);");
					System.out.println("\t\t});");
					System.out.println("\t}");

					break;

				default:
					break;
				}
			}

		}

		// // render the properties of the conditional css of the widgets of
		// this
		// component if applicable recursively
		for (Widget widget : component.getTemplate().getWidgets()) {
			recursiveRenderConditionCssStylesForWidget(widget);
		}

		// render the event handlers recursively
		for (Widget widget : component.getTemplate().getWidgets()){
			recursiveRenderEventHandlersForWidget(widget);
		}

		System.out.println("}");

		// render the (FILES) of the children
		// rloman might refactor all those component like component, service and
		// directive to one common base class
		for (Component child : component.getChildren()) {
			child.accept(this);
		}

		for (Service service : component.getServices()){
			service.accept(this);
		}

		for (Directive directive : component.getDirectives()) {
			directive.accept(this);
		}

		component.getPipes().accept(this);

		resetOutputStream();

	}

	private void renderPathExpressionsRecursively(Component component) {
		for (Component sub : component.getChildren()) {
			String defaultRouteString = "";
			if (sub.isDefaultRoute()) {

				defaultRouteString = ", useAsDefault:true";

			}
			System.out.printf("\t\t{path:'%s', name:'%s', component:%s%s}, %n",
					convertFirstCharacterToLowercase(sub.getName()), convertFirstCharacterToUppercase(sub.getName()),
					convertFirstCharacterToUppercase(sub.getName() + "Component"), defaultRouteString);

			renderPathExpressionsRecursively(sub);
		}
	}

	private void renderTemplate(Component component) {
		if (component.getTemplate().isRenderTemplateFile()) {
			System.out.printf("\ttemplateUrl: '%s' %n",
					"app/" + this.convertUpperCamelCaseToAngularString(component.getName()) + ".component.html");
			setOutputStreamForExternalTemplate(component.getName());
		} else {
			System.out.println("\ttemplate: `\n\t\t");
		}

		component.getTemplate().accept(this);

		if (component.isEnableRouting()) {
			// from udemy
			System.out.println("<nav class='navbar navbar-default'>");
			System.out.println("<div class='container-fluid'>");
			System.out.println(" <!-- Brand and toggle get grouped for better mobile display -->");
			System.out.println("<div class='navbar-header'>");
			System.out.println("<button type='button' class='navbar-toggle collapsed' data-toggle='collapse' "
					+ "data-target='#bs-example-navbar-collapse-1' aria-expanded='false'>");
			System.out.println("<span class='sr-only'>Toggle navigation</span>");
			System.out.println("<span class='icon-bar'></span>");
			System.out.println("<span class='icon-bar'></span>");
			System.out.println("<span class='icon-bar'></span>");
			System.out.println("</button>");
			System.out.println("<a class='navbar-brand' href='#'>Brand</a>");
			System.out.println("</div>");

			// the real world
			System.out.println("<div class='collapse navbar-collapse' id='bs-example-navbar-collapse-1'>");
			System.out.println("<ul class='nav navbar-nav'>");
			for (Component c : component.getChildren()) {
				System.out.printf("<li><a [routerLink]=\"['%s']\">%s</a> </li> %n",
						convertFirstCharacterToUppercase(c.getName()), convertFirstCharacterToUppercase(c.getName()));
			}
			System.out.println("</ul>");
			System.out.println("</div>");
			System.out.println("</div>");
			System.out.println("</nav>");
			System.out.println("<router-outlet></router-outlet>");
		}

		// since this entire project! is a learning project. here I will inspect
		// if it is a array for now
		for (ComponentAttribute attr : component.getAttributes()) {
			if (attr.getType().trim().contains("[]")) {
				StringBuilder builder = new StringBuilder();
				builder.append(String.format("<div *ngIf='%s'>", attr.getName()));
				builder.append("<ul>");
				builder.append(String.format("<li *ngFor='#%s of %s'>",
						attr.getName().substring(0, attr.getName().length() - 1), attr.getName()));
				builder.append(String.format("<a [routerLink]=\"['%s', {id:%s.id}]\">{{ %s.id }}</a>",
						convertFirstCharacterToUppercase(attr.getName().substring(0, attr.getName().length() - 1)),
						attr.getName().substring(0, attr.getName().length() - 1),
						attr.getName().substring(0, attr.getName().length() - 1)));
				builder.append(
						String.format("{{ %s | json }}", attr.getName().substring(0, attr.getName().length() - 1)));
				builder.append(" ");
				// remove rloman but should be decided upon Crud enum later
				builder.append(String.format("<button class='delete' (click)='delete(%s);'>X</button>",
						attr.getName().substring(0, attr.getName().length() - 1)));
				builder.append("</li>");
				builder.append("</ul>");
				builder.append("</div>");
				builder.append(String.format("<div *ngIf='!%s'>", attr.getName()));
				builder.append(String.format("You don't have any %s yet", attr.getName()));
				builder.append("</div>");
				System.out.println(builder.toString());
			}

		}
		// render subcomponents his selectors in the template
		// for now only the parent component (this one) does not have routing
		// enabled
		if (!component.isEnableRouting()) {
			for (Component child : component.getChildren()) {
				List<String> namesOfInputProperties = new ArrayList<>();
				List<String> namesOfOutputProperties = new ArrayList<>();
				for (ComponentAttribute attr : child.getAttributes()) {
					if (attr instanceof InputProperty) {
						namesOfInputProperties.add(String.format("%s='%s'", attr.getName(),
								attr.getValue() != null ? attr.getValue() : ""));
					}
					if (attr instanceof OutputProperty) {
						namesOfOutputProperties.add(String.format("(%s)='%s'", attr.getName(),
								((OutputProperty) attr).getEventHandlerInJavascriptCode()));
					}

				}
				System.out.printf("\t\t<%s %s %s></%s>%n", child.getSelector(),
						String.join(", ", namesOfInputProperties), String.join(", ", namesOfOutputProperties),
						child.getSelector());
			}
		}

		if (component.getTemplate().isRenderTemplateFile()) {
			resetOutputStream();
		} else {
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
			System.out.println("\t\tconsole.log('You " + event.toString().toLowerCase() + "ed a "
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
		} else {
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
			System.out.printf("(%s)='on%s($event);' ", e.toString().toLowerCase(), widget.getClass().getSimpleName()
					+ this.convertFirstCharacterToUppercase(e.toString().toLowerCase()));
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
			System.out.println();
			System.out.println("\t\t\t[ngClass]=\"{");
			System.out.println(String.join(",\n", widget.conditionalCssStyles.entrySet().stream().map(entry -> {
				return String.format("\t\t\t\t'%s': %s",
						this.convertUpperCamelCaseToAngularString(entry.getKey().toString()), entry.getValue());
			}).collect(Collectors.toList())));

			System.out.println("\t\t\t}\"");
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
		System.out.printf("\t@Input()%n\t%s: %s;%n", inputComponentAttribute.getName(),
				inputComponentAttribute.getType());
	}

	@Override
	public void visit(OutputProperty outputComponentAttribute) {
		System.out.println();
		System.out.printf("\t@Output()%n\t%s %s;%n", outputComponentAttribute.getName(),
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
		String pipes = String.join("",
				textField.getPipes().stream().map(pipe -> pipe.toString()).collect(Collectors.toList()));
		if (!textField.getCustomPipes().isEmpty()) {
			for (CustomPipe customPipe : textField.getCustomPipes()) {
				String attr = customPipe.getAttributes();
				pipes += " | " + customPipe.getName().toLowerCase() + (attr != null ? ":" + attr : "");
			}
		}
		System.out.printf("<span>%s: {{ %s %s }}</span> %n", textField.getLabel(), textField.getNgModel().getName(),
				pipes);

	}

	private void setOutputStream(DomainInterface domainInterface) {
		currentOutputStream = System.out;

		try {
			FileOutputStream outputStream = new FileOutputStream(
					"app/" + this.convertFirstCharacterToLowercase(domainInterface.getSingularPascalcaseName()) + ".ts");
			PrintStream ps = new PrintStream(outputStream);
			System.setOut(ps);

		} catch (FileNotFoundException e) {
			// rloman nog try with resources doen.
			e.printStackTrace();
		}
	}

	private void setOutputStream(Service service) {
		currentOutputStream = System.out;

		try {
			FileOutputStream outputStream = new FileOutputStream(
					"app/" + service.getName().toLowerCase() + ".service.ts");
			PrintStream ps = new PrintStream(outputStream);
			System.setOut(ps);

		} catch (FileNotFoundException e) {
			// rloman nog try with resources doen.
			e.printStackTrace();
		}

	}

	private void setOutputStreamForExternalTemplate(String componentName) {
		currentOutputStream = System.out;

		try {
			FileOutputStream outputStream = new FileOutputStream(
					"app/" + this.convertUpperCamelCaseToAngularString(componentName) + ".component.html");
			PrintStream ps = new PrintStream(outputStream);
			System.setOut(ps);

		} catch (FileNotFoundException e) {
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

		} catch (FileNotFoundException e) {
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

		} catch (FileNotFoundException e) {
			// rloman nog try with resources doen.
			e.printStackTrace();
		}

	}

	public static String convertFirstCharacterToLowercase(String input) {
		String output = Character.toLowerCase(input.charAt(0)) + (input.length() > 1 ? input.substring(1) : "");

		return output;

	}

	public static String convertFirstCharacterToUppercase(String input) {
		String output = Character.toUpperCase(input.charAt(0)) + (input.length() > 1 ? input.substring(1) : "");

		return output;
	}

	// e.g. convert AutoGrow to auto-grow
	public static String convertUpperCamelCaseToAngularString(String input) {

		return convertFirstCharacterToLowercase(input).replaceAll("([A-Z])", "-$1").toLowerCase();

	}

	// this methods set the output to the files where it should be
	private void setOutputStream(Component component) {
		currentOutputStream = System.out;

		try {
			FileOutputStream outputStream = new FileOutputStream(
					"app/" + convertUpperCamelCaseToAngularString(component.getName()) + ".component.ts");
			PrintStream ps = new PrintStream(outputStream);
			System.setOut(ps);

		} catch (FileNotFoundException e) {
			// rloman nog try with resources doen.
			e.printStackTrace();
		}

	}

	// resets the outputstream to the previous outputstream
	private void resetOutputStream() {
		System.setOut(currentOutputStream);
	}
}
