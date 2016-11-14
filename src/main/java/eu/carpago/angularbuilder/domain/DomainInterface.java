package eu.carpago.angularbuilder.domain;

import java.util.ArrayList;
import java.util.List;

import eu.carpago.angularbuilder.utils.Utils;
import eu.carpago.angularbuilder.visitor.Visitor;

public class DomainInterface extends Node {

	public static class KeyValueBean {

		public String name;
		public String type;
		public boolean mandatory;

		public KeyValueBean(String name, String type, boolean mandatory) {
			this.name = name;
			this.type = type;
			this.mandatory = mandatory;
		}
	}

	private String name;

	private List<KeyValueBean> attributes = new ArrayList<>();

	public DomainInterface(String name) {
		this.name = name;
	}

	public void addInstanceVar(String name, String type, boolean mandatory) {
		this.attributes.add(new KeyValueBean(name, type, mandatory));
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
	
	public List<KeyValueBean> getAttributes() {
		return attributes;
	}
}
