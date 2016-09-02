package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class TextField extends Widget {
	
	private String label;

	private ComponentAttribute ngModel;
	
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

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);

	}
}
