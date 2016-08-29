package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.List;

import eu.allego.angularbuilder.visitor.Visitor;

public class Component implements Node {

	//angular2 api properties
	
	private String name;
	
	private String selector;
	private String template;

	private List<Component> children = new ArrayList<>();
	
	// application specific properties
	private String title; // mostly all component do have a title (will add more later)

	/**
	 * 
	 * @param name = the name of the component (e.g. CoursesComponent has name "Courses"
	 * @param selector = the html selector of the component (e.g. <courses></courses> is "courses"
	 * @param template = the to be rendered html of this component
	 */
	public Component(String name, String title, String selector, String template) {
		this.name = name;
		this.title = title;
		this.selector = selector;
		this.template = template;
	}

	public void addChildComponent(Component component) {
		this.children.add(component);

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

	public String getTemplate() {
		return template;
	}

	public List<Component> getChildren() {
		return children;
	}

	public String getTitle() {
		return title;
	}
}
