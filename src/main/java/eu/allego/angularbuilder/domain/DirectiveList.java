package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import eu.allego.angularbuilder.visitor.Visitor;

public class DirectiveList extends Node implements Iterable<Directive> {
	
	private List<Directive> directives = new ArrayList<>();
	
	@Override
	public Iterator<Directive> iterator() {
		return this.directives.iterator();
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public boolean add(Directive e) {
		return directives.add(e);
	}

	public Stream<Directive> stream() {
		return this.directives.stream();
	}

	public boolean isEmpty() {
		return directives.isEmpty();
	}
}
