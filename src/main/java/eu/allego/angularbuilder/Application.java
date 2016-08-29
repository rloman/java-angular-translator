package eu.allego.angularbuilder;

import java.util.ArrayList;
import java.util.List;

import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;
import eu.allego.angularbuilder.visitor.Angular2GeneratingVisitor;
import eu.allego.angularbuilder.visitor.Visitor;

public class Application {
	public static void main(String[] args) {

		Component appComponent = new Component("App", "", "my-app", "<h1>My First Angular App</h1>");

		{
			ServiceMethod method = new ServiceMethod("getCourses()", "string[]", "return ['aaa', 'bbb']");
			Service courseService = new Service("Course", method);
			
			Component coursesComponent = new Component("Courses", "Overview of Courses", "courses", "<h2>Courses</h2>");
			coursesComponent.addService(courseService);
			List<Object> courses = new ArrayList<>();
			coursesComponent.addCollection("courses", courses);
			appComponent.addChildComponent(coursesComponent);
		}

		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);

		// System.out.println("Done");

	}
}
