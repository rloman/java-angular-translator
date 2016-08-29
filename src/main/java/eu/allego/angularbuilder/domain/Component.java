package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.List;

import eu.allego.angularbuilder.visitor.Visitor;

public class Component implements Node {
	
	private String name;
	
	private String selector;
	private String template;
	
	private List<Component> children = new ArrayList<>();
	
	public Component(String name, String selector, String template) {
		this.name = name;
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

}
