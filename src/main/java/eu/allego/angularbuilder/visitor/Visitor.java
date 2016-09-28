package eu.allego.angularbuilder.visitor;

import eu.allego.angularbuilder.domain.Button;
import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.ComponentAttribute;
import eu.allego.angularbuilder.domain.ComponentAttributeList;
import eu.allego.angularbuilder.domain.ComponentList;
import eu.allego.angularbuilder.domain.Constructor;
import eu.allego.angularbuilder.domain.CustomPipe;
import eu.allego.angularbuilder.domain.CustomPipeList;
import eu.allego.angularbuilder.domain.Directive;
import eu.allego.angularbuilder.domain.DirectiveList;
import eu.allego.angularbuilder.domain.Div;
import eu.allego.angularbuilder.domain.DomainInterface;
import eu.allego.angularbuilder.domain.DomainService;
import eu.allego.angularbuilder.domain.ITag;
import eu.allego.angularbuilder.domain.InlineStyle;
import eu.allego.angularbuilder.domain.InlineStyleList;
import eu.allego.angularbuilder.domain.InputField;
import eu.allego.angularbuilder.domain.InputProperty;
import eu.allego.angularbuilder.domain.OutputProperty;
import eu.allego.angularbuilder.domain.RestDomainService;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;
import eu.allego.angularbuilder.domain.ServicesList;
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

	void visit(InputProperty inputProperty);

	void visit(OutputProperty outputComponentAttribute);

	void visit(ComponentList componentList);

	void visit(ServicesList servicesList);

	void visit(DirectiveList directiveList);

	void visit(ComponentAttributeList componentAttributeList);

	void visit(InlineStyleList inlineStyleList);

	void visit(InlineStyle inlineStyle);

	void visit(CustomPipe customPipe);

	void visit(CustomPipeList customPipeList);

	void visit(DomainInterface domainInterface);

	void visit(DomainService domainService);

	void visit(RestDomainService service);

}
