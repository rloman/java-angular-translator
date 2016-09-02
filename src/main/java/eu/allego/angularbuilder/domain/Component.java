package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.allego.angularbuilder.visitor.Visitor;

public class Component extends Node {

	// angular2 api properties

	private String name;

	private String selector;
	private Template template;

	private List<Component> children = new ArrayList<>();
	
	private List<Service> services = new ArrayList<>();
	
	private List<Directive> directives = new ArrayList<>();
	
	private List<ComponentAttribute> attributes = new ArrayList<>();
	
	private Constructor constructor;

	// application specific properties
	private String title; // mostly all component do have a title (will add more
							// later)

	// list of some collections which must be rendered by the component (and
	// later will be refactored to a service when we get to that section)
	private Map<String, List<Object>> listMap = new HashMap<>();
	
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
	public Component(String name, String title, String selector, String template) {
		this.name = name;
		this.title = title;
		this.selector = selector;
		this.template = new Template(template);
	}
	
	public Component(String name, String title, String selector, Template template) {
		this.name = name;
		this.title = title;
		this.selector = selector;
		this.template = template;
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

	public void addCollection(String name, List<Object> list) {
		this.listMap.put(name, list);
	}

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

	public List<Component> getChildren() {
		return children;
	}

	public String getTitle() {
		return title;
	}

	public Map<String, List<Object>> getListMap() {
		return listMap;
	}


	public List<Service> getServices() {
		return services;
	}

	public List<Directive> getDirectives() {
		return directives;
	}

	public void addAttribute(ComponentAttribute componentAttribute) {
		this.attributes.add(componentAttribute);
	}

	public List<ComponentAttribute> getAttributes() {
		return attributes;
	}

}
