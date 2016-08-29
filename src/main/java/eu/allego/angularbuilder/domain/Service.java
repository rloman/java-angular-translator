package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class Service implements Node {

	private String name = "Course";
	
	private ServiceMethod serviceMethod;
	

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
