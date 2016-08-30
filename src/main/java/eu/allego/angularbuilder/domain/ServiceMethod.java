package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class ServiceMethod extends Node {

	private String name;
	private String returnType;
	private String body;

	public ServiceMethod(String name, String returnType, String body) {
		this.name = name;
		this.returnType = returnType;
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getBody() {
		return body;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);

	}

}
