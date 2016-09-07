package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.allego.angularbuilder.visitor.Visitor;

public class InlineStyleList extends Node implements Iterable<InlineStyle> {

	private List<InlineStyle> styles = new ArrayList<>();

	public boolean isEmpty() {
		return styles.isEmpty();
	}

	public boolean add(InlineStyle e) {
		return styles.add(e);
	}

	@Override
	public Iterator<InlineStyle> iterator() {
		return this.styles.iterator();
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
