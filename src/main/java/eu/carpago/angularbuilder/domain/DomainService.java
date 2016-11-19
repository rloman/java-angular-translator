package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class DomainService extends Service  {
	
	private DomainInterface domainInterface;

	public DomainService(DomainInterface domainInterface) {
		super(domainInterface.getSingularPascalcaseName());
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
