package eu.carpago.angularbuilder.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Widget extends Node  {
	
	private String label;
	
	private List<Event> events = new ArrayList<>();
	
	private List<Widget> children = new ArrayList<>();
	
	public List<Css> cssStyles = new ArrayList<>();
	
	public Map<Css, String> conditionalCssStyles = new HashMap<>();
	
	public Widget() {
		
	}
	public void addEvent(Event ...events) {
		for(Event event : events) {
			this.events.add(event);
		}
	}
	
	/**
	 * 
	 * @param css a condtional css styles
	 * @param property on which property in the component of the containing widget this css style is conditional on (e.g. active -> isActive might render an active css style)
	 */
	public void addConditionalCssStyle(Css css, String property) {
		this.conditionalCssStyles.put(css,  property);
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

	public List<Event> getEvents() {
		return events;
	}

	public void addCss(Css css) {
		this.cssStyles.add(css);
	}

	public List<Css> getCssStyles() {
		return cssStyles;
	}

	public Map<Css, String> getConditionalCssStyles() {
		return this.conditionalCssStyles;
	}
	

}
