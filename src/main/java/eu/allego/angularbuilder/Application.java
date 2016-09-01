package eu.allego.angularbuilder;

import java.util.ArrayList;
import java.util.List;

import eu.allego.angularbuilder.domain.Button;
import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.Constructor;
import eu.allego.angularbuilder.domain.Css;
import eu.allego.angularbuilder.domain.Directive;
import eu.allego.angularbuilder.domain.Div;
import eu.allego.angularbuilder.domain.Event;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;
import eu.allego.angularbuilder.domain.Template;
import eu.allego.angularbuilder.domain.Widget;
import eu.allego.angularbuilder.visitor.Angular2GeneratingVisitor;
import eu.allego.angularbuilder.visitor.Visitor;

public class Application {
	public static void main(String[] args) {

		Component appComponent = new Component("App", "", "my-app", "<h1>My First Angular App</h1>");

		{
			ServiceMethod method = new ServiceMethod("getCourses()", "string[]", "return ['aaa', 'bbb']");
			Service courseService = new Service("Course", method);
			
			Component coursesComponent = new Component("Courses", "Overview of Courses", "courses", "<h2>Courses</h2> 	<input type='text' autoGrow />");
			coursesComponent.addService(courseService);
			List<Object> courses = new ArrayList<>();
			coursesComponent.addCollection("courses", courses);
			Directive autoGrowDirective = new Directive("AutoGrow", Event.FOCUS, Event.BLUR);
			coursesComponent.addDirective(autoGrowDirective);
			
			Constructor c = new Constructor("\t\tthis.courses = courseService.getCourses();");
			coursesComponent.setConstructor(c);
			
			appComponent.addChildComponent(coursesComponent);
		}
		
		{
			ServiceMethod method = new ServiceMethod("getAuthors()", "string[]", "return ['Andrew Hunt', 'Dave Thomas', 'Donald Knuthhhh']");
			Service authorService = new Service("Author", method);
			
			Component authorsComponent = new Component("Authors", "Overview of Authors", "authors", "<h2>Authors</h2>");
			authorsComponent.addService(authorService);
			List<Object> authors = new ArrayList<>();
			authorsComponent.addCollection("authors", authors);
			
			Constructor c = new Constructor("\t\tthis.authors = authorService.getAuthors();");
			authorsComponent.setConstructor(c);
			
			appComponent.addChildComponent(authorsComponent);
		}
		
		{
			Div div = new Div();
			div.addEvent(Event.CLICK);
			
			Widget button = new Button("click me");
			button.addEvent(Event.CLICK);
			button.addCss(Css.btn);
			button.addCss(Css.btnPrimary);
			button.addConditionalCssStyle(Css.active, "isActive");
			div.addChild(button);
			
			Template template = new Template();
			template.add(div);
			
			Component coursesComponent = new Component("Buttons", "Overview of buttons", "buttons",  template);
			
			appComponent.addChildComponent(coursesComponent);
		}

		Visitor visitor = new Angular2GeneratingVisitor();
		
		appComponent.accept(visitor);
	}
}
