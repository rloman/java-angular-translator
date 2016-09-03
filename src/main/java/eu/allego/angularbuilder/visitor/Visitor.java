package eu.allego.angularbuilder.visitor;

import eu.allego.angularbuilder.domain.Button;
import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.ComponentAttribute;
import eu.allego.angularbuilder.domain.Constructor;
import eu.allego.angularbuilder.domain.Directive;
import eu.allego.angularbuilder.domain.Div;
import eu.allego.angularbuilder.domain.ITag;
import eu.allego.angularbuilder.domain.InputField;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;
import eu.allego.angularbuilder.domain.Template;
import eu.allego.angularbuilder.domain.TextField;
import eu.allego.angularbuilder.domain.Widget;

public interface Visitor {

	void visit(Component component);

	void visit(Service service);

	void visit(ServiceMethod serviceMethod);

	void visit(Constructor constructor);

	void visit(Directive directive);

	void visit(Template template);

	void visit(Button widget);

	void visit(Div div);

	void visit(Widget widget);

	void visit(InputField inputField);

	void visit(ComponentAttribute componentAttribute);

	void visit(TextField textField);

	void visit(ITag itag);

}
