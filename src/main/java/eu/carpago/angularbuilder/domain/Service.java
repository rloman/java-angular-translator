package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class Service extends Node {

	private String name;
	
	private ServiceMethod serviceMethod;
	
	public Service(String name){
		this.name = name;
	}
	

	public Service(String name, ServiceMethod serviceMethod) {
		this.name = name;
		this.serviceMethod = serviceMethod;
	}

	public String getName() {
		return name;
	}


	public ServiceMethod getServiceMethod() {
		return serviceMethod;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);

	}

}
