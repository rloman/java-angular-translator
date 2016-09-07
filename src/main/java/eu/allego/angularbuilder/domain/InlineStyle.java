package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.allego.angularbuilder.domain.InlineStyle.InlineStyleLine;
import eu.allego.angularbuilder.visitor.Visitor;

public class InlineStyle extends Node implements Iterable<InlineStyleLine> {

	public static class InlineStyleLine {
		public String key;
		public String value;

		public InlineStyleLine(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	private String styleName;
	private List<InlineStyleLine> keyValues = new ArrayList<>();

	public InlineStyle(String styleName) {
		this.styleName = styleName;
	}
	
	public String getStyleName() {
		return styleName;
	}

	public void addKeyValue(String key, String value) {
		this.keyValues.add(new InlineStyleLine(key, value));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Iterator<InlineStyleLine> iterator() {
		return this.keyValues.iterator();
	}
}
