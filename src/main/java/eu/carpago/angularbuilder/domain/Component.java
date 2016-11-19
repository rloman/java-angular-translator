package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class Component extends Node {

	// angular2 api properties

	private String name;

	private String selector;
	private Template template;

	private ComponentList children = new ComponentList();

	private ServicesList services = new ServicesList();

	private DirectiveList directives = new DirectiveList();

	private InlineStyleList inlineStyles = new InlineStyleList();

	private ComponentAttributeList attributes = new ComponentAttributeList();

	private CustomPipeList pipes = new CustomPipeList();

	private Constructor constructor;

	private boolean routingEnabled;// kan ik wellicht afleiden uit de
									// aanwezigheid van subcomponenen maar dat
									// is niet zeker volgens mij

	private boolean forSingularUse = false;

	private Crud[] crud;

	private boolean defaultRoute;
	
	private DomainInterface domain;

	/**
	 * 
	 * @param name
	 *            = the name of the component (e.g. CoursesComponent has name
	 *            "Courses"
	 * @param selector
	 *            = the html selector of the component (e.g. <courses></courses>
	 *            is "courses"
	 * @param template
	 *            = the to be rendered html of this component
	 */
	public Component(String name, String selector, Template template) {
		this.name = name;
		this.selector = selector;
		this.template = template;
		this.routingEnabled = false;
	}

	public void addDirective(Directive directive) {
		this.directives.add(directive);
	}

	public void setConstructor(Constructor constructor) {
		this.constructor = constructor;
	}

	public Constructor getConstructor() {
		return this.constructor;
	}

	public void addService(Service service) {
		this.services.add(service);
	}

	public void addChildComponent(Component component) {
		this.children.add(component);

	}

	public void addInlineStyle(InlineStyle style) {
		this.inlineStyles.add(style);
	}

	public void addPipe(CustomPipe pipe) {
		this.pipes.addPipe(pipe);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public String getSelector() {
		return selector;
	}

	public Template getTemplate() {
		return template;
	}

	public ComponentList getChildren() {
		return children;
	}

	public ServicesList getServices() {
		return services;
	}

	public DirectiveList getDirectives() {
		return directives;
	}

	public void addAttribute(ComponentAttribute componentAttribute) {
		this.attributes.add(componentAttribute);
	}

	public ComponentAttributeList getAttributes() {
		return attributes;
	}

	public InlineStyleList getInlineStyles() {
		return inlineStyles;
	}

	public boolean containsInputProperty() {
		for (ComponentAttribute attr : this.attributes) {
			if (attr instanceof InputProperty) {
				return true;
			}
		}
		return false;
	}

	public boolean containsOutputProperty() {
		for (ComponentAttribute attr : this.attributes) {
			if (attr instanceof OutputProperty) {
				return true;
			}
		}
		return false;
	}

	public CustomPipeList getPipes() {
		return pipes;
	}

	public boolean isRoutingEnabled() {
		return routingEnabled;
	}

	public void setRoutingEnabled(boolean routingEnabled) {
		this.routingEnabled = routingEnabled;
	}

	public boolean isForSingularUse() {
		return forSingularUse;
	}

	public void setForSingularUse(boolean forSingularUse) {
		this.forSingularUse = forSingularUse;
	}

	public Crud[] getCrud() {
		return crud;
	}

	public void setCrud(Crud... crud) {
		this.crud = crud;
	}

	public boolean isDefaultRoute() {
		return defaultRoute;
	}

	public void setDefaultRoute(boolean defaultRoute) {
		this.defaultRoute = defaultRoute;
	}

	public DomainInterface getDomain() {
		return domain;
	}

	public void setDomain(DomainInterface domain) {
		this.domain = domain;
	}
}
