package eu.carpago.angularbuilder.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import eu.carpago.angularbuilder.visitor.Visitor;

public class ComponentList extends Node implements Iterable<Component> {

	private List<Component> components = new ArrayList<>();

	public void add(Component component) {
		this.components.add(component);
	}

	@Override
	public Iterator<Component> iterator() {
		return this.components.iterator();
	}
	
	public boolean isEmpty() {
		return this.components.isEmpty();
	}

	public Stream<Component> stream() {
		return this.components.stream();
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}