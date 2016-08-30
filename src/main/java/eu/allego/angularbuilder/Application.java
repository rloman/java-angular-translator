package eu.allego.angularbuilder;

import java.util.ArrayList;
import java.util.List;

import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.Constructor;
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
			
			Constructor c = new Constructor("\t\tthis.courses = courseService.getCourses();");
			coursesComponent.setConstructor(c);
			
			appComponent.addChildComponent(coursesComponent);
		}
		
		{
			ServiceMethod method = new ServiceMethod("getAuthors()", "string[]", "return ['Andrew Hunt', 'Dave Thomas', 'Donald Knuth']");
			Service authorService = new Service("Author", method);
			
			Component authorsComponent = new Component("Authors", "Overview of Authors", "authors", "<h2>Authors</h2>");
			authorsComponent.addService(authorService);
			List<Object> authors = new ArrayList<>();
			authorsComponent.addCollection("authors", authors);
			appComponent.addChildComponent(authorsComponent);
		}

		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);

		// System.out.println("Done");

	}
}
