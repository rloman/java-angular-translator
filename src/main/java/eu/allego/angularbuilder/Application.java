package eu.allego.angularbuilder;

import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.visitor.Angular2GeneratingVisitor;
import eu.allego.angularbuilder.visitor.Visitor;

public class Application {
	public static void main(String[] args) {
		
		Component appComponent = new Component("App", "my-app", "<h1>My First Angular App</h1>");
		
		appComponent.addChildComponent(new Component("Courses", "courses", "<h2>Courses</h2>"));
		
		Visitor visitor = new Angular2GeneratingVisitor();
		
		appComponent.accept(visitor);
		
	}
}
