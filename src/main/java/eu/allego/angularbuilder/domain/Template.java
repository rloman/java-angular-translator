package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.List;

import eu.allego.angularbuilder.visitor.Visitor;

public class Template extends Node {
	
	private String templateString;
	
	private List<Widget> widgets = new ArrayList<Widget>();
	
	public Template() {
		
	}
	
	public Template(String templateString) {
		this.templateString = templateString;
	}
	
	public void add(Widget widget) {
		this.widgets.add(widget);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
	}

	public String getTemplateString() {
		return templateString;
	}

	public List<Widget> getWidgets() {
		return widgets;
	}

}
