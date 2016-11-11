package eu.carpago.angularbuilder.visitor;

import eu.carpago.angularbuilder.domain.Button;
import eu.carpago.angularbuilder.domain.Component;
import eu.carpago.angularbuilder.domain.ComponentAttribute;
import eu.carpago.angularbuilder.domain.ComponentAttributeList;
import eu.carpago.angularbuilder.domain.ComponentList;
import eu.carpago.angularbuilder.domain.Constructor;
import eu.carpago.angularbuilder.domain.CustomPipe;
import eu.carpago.angularbuilder.domain.CustomPipeList;
import eu.carpago.angularbuilder.domain.Directive;
import eu.carpago.angularbuilder.domain.DirectiveList;
import eu.carpago.angularbuilder.domain.Div;
import eu.carpago.angularbuilder.domain.DomainDrivenDevelopment;
import eu.carpago.angularbuilder.domain.DomainInterface;
import eu.carpago.angularbuilder.domain.DomainService;
import eu.carpago.angularbuilder.domain.ITag;
import eu.carpago.angularbuilder.domain.InlineStyle;
import eu.carpago.angularbuilder.domain.InlineStyleList;
import eu.carpago.angularbuilder.domain.InputField;
import eu.carpago.angularbuilder.domain.InputProperty;
import eu.carpago.angularbuilder.domain.OutputProperty;
import eu.carpago.angularbuilder.domain.RestDomainService;
import eu.carpago.angularbuilder.domain.Service;
import eu.carpago.angularbuilder.domain.ServiceMethod;
import eu.carpago.angularbuilder.domain.ServicesList;
import eu.carpago.angularbuilder.domain.Template;
import eu.carpago.angularbuilder.domain.TextField;
import eu.carpago.angularbuilder.domain.Widget;

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

	void visit(DomainDrivenDevelopment domainDrivenDevelopment);

}
