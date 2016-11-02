package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class CustomPipe extends Node  {
	
	private String name;
	private String attributes;
	
	public CustomPipe(String name, String attributes) {
		this.name = name;
		this.attributes = attributes;
	}
	
	
	public CustomPipe(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public String getAttributes() {
		return attributes;
	}


	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
	}
	
}
