package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class DomainService extends Service  {
	
	private DomainInterface domainInterface;

	public DomainService(DomainInterface domainInterface) {
		super(domainInterface.getName());
		this.domainInterface = domainInterface;
	}

	public DomainInterface getDomainInterface() {
		return domainInterface;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	
}
