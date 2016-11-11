package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class Service extends Node {

	// rloman was dit nu
	private String name = "Course";
	
	private ServiceMethod serviceMethod;
	
	public Service(String name){
		this.name = name;
	}
	

	public Service(String name, ServiceMethod serviceMethod) {
		this.name = name;
		this.serviceMethod = serviceMethod;
	}


	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);

	}


	public String getName() {
		return name;
	}


	public ServiceMethod getServiceMethod() {
		return serviceMethod;
	}

}
