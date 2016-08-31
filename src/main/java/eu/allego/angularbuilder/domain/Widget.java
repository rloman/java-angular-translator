package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Widget extends Node  {
	
	private String label;
	
	private List<Widget> children = new ArrayList<>();
	
	public Widget() {
		
	}
	
	public Widget(String label) {
		this.label = label;
	}
	
	public void addChild(Widget widget) {
		this.children.add(widget);
	}

	public String getLabel() {
		return label;
	}

	public List<Widget> getChildren() {
		return children;
	}
	

}
