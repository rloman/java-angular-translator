package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class RestDomainService extends DomainService {
	
	private String baseUrl;

	public RestDomainService(DomainInterface domainInterface, String baseUrl) {
		super(domainInterface);
		this.baseUrl = baseUrl;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}