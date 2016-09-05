package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.allego.angularbuilder.visitor.Visitor;

public class ComponentAttributeList extends Node implements Iterable<ComponentAttribute> {
	
	private List<ComponentAttribute> attributes = new ArrayList<>();

	public boolean isEmpty() {
		return attributes.isEmpty();
	}
	
	public boolean add(ComponentAttribute e) {
		return attributes.add(e);
	}

	@Override
	public Iterator<ComponentAttribute> iterator() {
		return this.attributes.iterator();
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
