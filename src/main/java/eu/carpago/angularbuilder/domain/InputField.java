package eu.carpago.angularbuilder.domain;

import eu.carpago.angularbuilder.visitor.Visitor;

public class InputField extends Widget {

	private ComponentAttribute ngModel;

	public ComponentAttribute getNgModel() {
		return ngModel;
	}
	
	public void setNgModel(ComponentAttribute inputComponent) {
		this.ngModel = inputComponent;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);

	}
}
