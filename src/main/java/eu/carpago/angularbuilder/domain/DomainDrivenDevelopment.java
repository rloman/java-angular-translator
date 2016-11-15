package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.domain.DomainInterface.KeyValueBean;
import eu.carpago.angularbuilder.visitor.Visitor;

public class DomainDrivenDevelopment extends Node {
	
	private Component appComponent;
	
	private DomainDrivenDevelopment(DomainDrivenDevelopmentBuilder builder) {
		this.appComponent = builder.appComponent;
	}

	public Component getAppComponent() {
		return appComponent;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	public static class DomainDrivenDevelopmentBuilder{
		
		private static final String ID = "id";
		private String title;
		private Component appComponent;
		private DomainInterface domain;
		private String baseUrl;
		private RestDomainService domainService;
		private Component pluralComponent;
		private Component createComponent;
		
		public DomainDrivenDevelopmentBuilder(DomainInterface domain, String baseUrl) {
			this.domain = domain;
			this.domain.addInstanceVar(ID, "number", false);
			this.baseUrl = baseUrl;
		}
		
		public DomainDrivenDevelopment build() {
			renderRestDomainService();
			
			renderPluralComponent();
			renderSingularComponent();
			renderCreateComponent();
			
			renderAppComponent();
			
			addAllToAppComponent();
			
			return new DomainDrivenDevelopment(this);
		}
		
		private void renderRestDomainService() {
			this.domainService = new RestDomainService(this.domain, this.baseUrl);
		}

		private void addAllToAppComponent() {
			this.appComponent.addChildComponent(this.pluralComponent);
			this.appComponent.addChildComponent(this.createComponent);
		}

		private void renderPluralComponent() {
			Template pluralTemplate = new Template(
					String.format("<h1>:: %s ::</h1>%n", this.domain.getPluralPascalcaseName()), true);
			this.pluralComponent = new Component(this.domain.getPluralPascalcaseName(),
					this.domain.getPluralPascalcaseName(), pluralTemplate);
			ComponentAttribute plurals = new ComponentAttribute(this.domain.getPluralCamelcaseName(),
					this.domain.getSingularPascalcaseName()+"[]");
			pluralComponent.addAttribute(plurals);

			ComponentAttribute warning = new ComponentAttribute("warning",
					"string");
			pluralComponent.addAttribute(warning);
			this.pluralComponent.addService(this.domainService);
			
			// constructor
			Constructor constructorForPlural = new Constructor(
					String.format("\t\t%sService.get%s().subscribe(%s => this.%s = %s);",this.domain.getSingularCamelcaseName(), this.domain.getPluralPascalcaseName(), this.domain.getPluralCamelcaseName(), this.domain.getPluralCamelcaseName(), this.domain.getPluralCamelcaseName()));
			this.pluralComponent.setConstructor(constructorForPlural);
			pluralComponent.setDefaultRoute(true);
			pluralComponent.setDomain(this.domain);
			
			
			this.pluralComponent.setCrud(Crud.DELETE);
		}
		
		private void renderSingularComponent(){
			String templateSingularString = String.format("<h1>:: %s ::</h1>\n<div>\n\t<pre>{{ %s | json }}</pre>\n</div>\n", this.domain.getSingularPascalcaseName(), this.domain.getSingularCamelcaseName());
			templateSingularString += String.format("<div *ngIf='%s'>", this.domain.getSingularCamelcaseName());
			templateSingularString += "<div class='input-group'>";
			for(KeyValueBean keyValueBean : this.domain.getAttributes() ){
				String property = keyValueBean.name;
				if(!ID.equals(property)) {
					templateSingularString += String.format("%s: <input type='text' class='form-control' [(ngModel)]='%s.%s'><br>",property, this.domain.getSingularCamelcaseName(), property);
				}
			}
			templateSingularString		+= "</div>"
					+ "<span class='input-group-btn'><button class='btn btn-primary' (click)='update()'>Update</button></span>"
					+ "</div>";

			Template singularTemplate = new Template(templateSingularString,
					true);
			Component singularComponent = new Component(this.domain.getSingularPascalcaseName(), this.domain.getSingularCamelcaseName(),
					singularTemplate);
			singularComponent.setCrud(Crud.UPDATE);
			ComponentAttribute singular = new ComponentAttribute(this.domain.getSingularCamelcaseName(),
					this.domain.getSingularPascalcaseName()+"={}");
			singularComponent.addAttribute(singular);
			singularComponent.setForSingularUse(true);
			Constructor constructorForSingular = new Constructor(
					String.format("%sService.get%s(parseInt(routeParams.get('id'))).subscribe(%s => this.%s = %s);",
							this.domain.getSingularCamelcaseName(), this.domain.getSingularPascalcaseName(),
							this.domain.getSingularCamelcaseName(), this.domain.getSingularCamelcaseName(),
							this.domain.getSingularCamelcaseName()));
			singularComponent.setConstructor(constructorForSingular);
			singularComponent.addService(this.domainService);
			singularComponent.setDomain(this.domain);
			
			this.pluralComponent.addChildComponent(singularComponent);
		}
		
		private void renderCreateComponent() {
			String createTemplateString = String.format("<h1>:: Create %s ::</h1>",
					this.domain.getSingularCamelcaseName());
			createTemplateString += "<div class='input-group'>";
			for (KeyValueBean keyValueBean : this.domain.getAttributes()) {
				String property = keyValueBean.name;
				if (!ID.equals(property)) {
					createTemplateString += String.format(
							"%s: <input type='text' class='form-control' [(ngModel)]='%s.%s'><br>",
							property, this.domain.getSingularCamelcaseName(),
							property);
				}
			}
			createTemplateString += "</div>" + "<span class='input-group-btn'>"
					+ "<button class='btn btn-primary' (click)='create()'>Create</button>" + "</span>";

			Template createTemplate = new Template(createTemplateString, true);
			this.createComponent = new Component(this.domain.getSingularPascalcaseName() + "Create",
					this.domain.getSingularCamelcaseName() + "-create", createTemplate);
			this.createComponent.setDomain(this.domain);
			for (KeyValueBean keyValueBean : this.domain.getAttributes()) {
				if (!ID.equals(keyValueBean.name)) {
					ComponentAttribute attr = new ComponentAttribute(keyValueBean.name, keyValueBean.type);
					createComponent.addAttribute(attr);
				}
			}

			ComponentAttribute singularComponent = new ComponentAttribute(this.domain.getSingularCamelcaseName(),
					this.domain.getSingularPascalcaseName() + "={}");
			createComponent.addAttribute(singularComponent);

			createComponent.addService(this.domainService);
			createComponent.setRoutingEnabled(false);
			createComponent.setCrud(Crud.CREATE);
		}

		private void renderAppComponent(){
			Template template = new Template(this.title == null ? "" :"<h1>:: Java to Angular2 Translator Demo ::</h1>", true);
			this.appComponent = new Component("App", "my-app", template);
			
			this.appComponent.setRoutingEnabled(true);
		}
	}
}
