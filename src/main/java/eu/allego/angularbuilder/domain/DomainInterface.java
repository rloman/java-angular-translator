package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.List;

import eu.allego.angularbuilder.visitor.Visitor;

public class DomainInterface extends Node {

	public static class KeyValueBean {

		public String name;
		public String type;

		public KeyValueBean(String name, String type) {
			this.name = name;
			this.type = type;
		}
	}

	private String name;

	private List<KeyValueBean> attributes = new ArrayList<>();

	public DomainInterface(String name) {
		this.name = name;
	}

	public void addInstanceVar(String name, String type) {
		this.attributes.add(new KeyValueBean(name, type));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public List<KeyValueBean> getAttributes() {
		return attributes;
	}
}
