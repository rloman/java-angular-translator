package eu.allego.angularbuilder.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.allego.angularbuilder.visitor.Visitor;

public class TextField extends Widget {
	
	private String label;

	private ComponentAttribute ngModel;
	
	private List<Pipe> pipes = new ArrayList<>();
	
	private List <CustomPipe> customPipes = new ArrayList<>();
	
	public TextField() {
		
	}
	
	public TextField(String label, ComponentAttribute ngModel) {
		this.label = label;
		this.ngModel = ngModel;
	}

	public ComponentAttribute getNgModel() {
		return ngModel;
	}
	
	public void setNgModel(ComponentAttribute inputComponent) {
		this.ngModel = inputComponent;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setPipes(Pipe ... pipes) {
		this.pipes = Arrays.asList(pipes);
	}
	
	public void addCustomPipe(CustomPipe pipe) {
		this.customPipes.add(pipe);
	}
	

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<Pipe> getPipes() {
		return pipes;
	}

	public List<CustomPipe> getCustomPipes() {
		return customPipes;
	}
}
