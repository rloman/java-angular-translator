package eu.allego.angularbuilder.visitor;

import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.Constructor;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;

public interface Visitor {

	void visit(Component component);

	void visit(Service service);

	void visit(ServiceMethod serviceMethod);

	void visit(Constructor constructor);

}
