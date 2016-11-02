package eu.carpago.angularbuilder.domain;

import java.util.ArrayList;
import java.util.List;

import eu.carpago.angularbuilder.visitor.Visitor;

public class Template extends Node {
	
	private String templateString;
	
	// might refactor to WidgetList
	private List<Widget> widgets = new ArrayList<Widget>();

	private boolean renderTemplateFile;
	
	public Template(String templateString, boolean renderTemplateFile) {
		this(renderTemplateFile);
		this.templateString = templateString;
	}
	
	public Template(boolean renderTemplateFile) {
		this.renderTemplateFile = renderTemplateFile;
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

	public boolean isRenderTemplateFile() {
		return renderTemplateFile;
	}

}
