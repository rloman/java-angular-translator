package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class Directive extends Node {
	
	private String name;
	private Event[] events;
	

	public Directive(String name, Event ...events) {
		this.name = name;
		this.events = events;
	}


	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
	}


	public String getName() {
		return name;
	}


	public Event[] getEvents() {
		return events;
	}

}
