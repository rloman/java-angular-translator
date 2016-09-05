package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import eu.allego.angularbuilder.visitor.Visitor;

public class ServicesList extends Node implements Iterable<Service> {
	
	private List<Service> services = new ArrayList<>();
	
	public boolean add(Service e) {
		return services.add(e);
	}
	
	@Override
	public Iterator<Service> iterator() {
		return this.services.iterator();
	}

	public boolean isEmpty() {
		return services.isEmpty();
	}

	public Stream<Service> stream() {
		return this.services.stream();
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}