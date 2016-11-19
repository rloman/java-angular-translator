package eu.carpago.angularbuilder.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.carpago.angularbuilder.utils.Utils;
import eu.carpago.angularbuilder.visitor.Visitor;

public class DomainInterface extends Node implements Iterable<DomainInterfaceAttribute> {

	private String name;

	private List<DomainInterfaceAttribute> attributes = new ArrayList<>();

	public DomainInterface(String name) {
		this.name = name;
	}

	public void addInstanceVar(String name, String type, boolean mandatory) {
		this.attributes.add(new DomainInterfaceAttribute(name, type, mandatory));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getSingularPascalcaseName() {
		return name;
	}
	
	public String getSingularCamelcaseName() {
		return Utils.convertFirstCharacterToLowercase(this.getSingularPascalcaseName());
		
	}
	
	public String getPluralPascalcaseName() {
		return this.getSingularPascalcaseName()+"s";
	}
	
	public String getPluralCamelcaseName() {
		return this.getSingularCamelcaseName()+"s";
	}
	
	public String getSingularLowercaseName() {
		return this.getSingularCamelcaseName().toLowerCase();
	}
	
	@Override
	public Iterator<DomainInterfaceAttribute> iterator() {
		
		return this.attributes.iterator();
	}
}
