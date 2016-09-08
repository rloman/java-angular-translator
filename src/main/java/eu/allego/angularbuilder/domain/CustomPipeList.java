package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import eu.allego.angularbuilder.visitor.Visitor;

public class CustomPipeList extends Node implements Iterable<CustomPipe> {
	
	private List<CustomPipe> pipes = new ArrayList<>();
	
	public void addPipe(CustomPipe pipe){
		this.pipes.add(pipe);
	}

	@Override
	public Iterator<CustomPipe> iterator() {
		return this.pipes.iterator();
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
	}

	public boolean isEmpty() {
		return pipes.isEmpty();
	}

	public Stream<CustomPipe> stream() {
		return this.pipes.stream();
	}

}
