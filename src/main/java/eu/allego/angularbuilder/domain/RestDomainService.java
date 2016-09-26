package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class RestDomainService extends DomainService {
	
	private String baseUrl;

	public RestDomainService(DomainInterface domainInterface, String baseUrl) {
		super(domainInterface);
		this.baseUrl = baseUrl;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getBaseUrl() {
		return baseUrl;
	}
	
	
}
