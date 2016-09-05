package eu.allego.angularbuilder;

import eu.allego.angularbuilder.domain.Button;
import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.ComponentAttribute;
import eu.allego.angularbuilder.domain.Constructor;
import eu.allego.angularbuilder.domain.Css;
import eu.allego.angularbuilder.domain.Directive;
import eu.allego.angularbuilder.domain.Div;
import eu.allego.angularbuilder.domain.Event;
import eu.allego.angularbuilder.domain.ITag;
import eu.allego.angularbuilder.domain.InputField;
import eu.allego.angularbuilder.domain.InputProperty;
import eu.allego.angularbuilder.domain.OutputProperty;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;
import eu.allego.angularbuilder.domain.Template;
import eu.allego.angularbuilder.domain.TextField;
import eu.allego.angularbuilder.domain.Widget;
import eu.allego.angularbuilder.visitor.Angular2GeneratingVisitor;
import eu.allego.angularbuilder.visitor.Visitor;

public class Application {
	public static void main(String[] args) {
		
		Template template = new Template("<h1>My First Angular Application</h1>", true);

		Component appComponent = new Component("App", "my-app",	template);
	
		Template favouriteComponentTemplate = new Template(true);
		
		Widget itag = new ITag();
		itag.addCss(Css.glyphicon);
		itag.addConditionalCssStyle(Css.glyphiconStarEmpty, "!isFavourite");
		itag.addConditionalCssStyle(Css.glyphiconStar, "isFavourite");
		itag.addEvent(Event.CLICK);
		favouriteComponentTemplate.add(itag);
		Component favouriteComponent = new Component("Favourite", "favourite", favouriteComponentTemplate);
		ComponentAttribute input = new InputProperty("isFavourite", "boolean");
		
		ComponentAttribute output = new OutputProperty("changeit");
		
		favouriteComponent.addAttribute(input);
		favouriteComponent.addAttribute(output);
		
		appComponent.addChildComponent(favouriteComponent);
		

		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);
		
		foo(appComponent);
		
	}

	public static void foo(Component appComponent) {
		
//		Template appComponentTemplate = new Template("<h1>My First Angular App</h1>", true);

//		Component appComponent = new Component("App", "my-app", appComponentTemplate);
		{
			ServiceMethod method = new ServiceMethod("getCourses()", "string[]", "return ['aaa', 'bbb']");
			Service courseService = new Service("Course", method);
			
			Template coursesComponentTemplate = new Template("<h2>Courses</h2> Title: {{title}}	<input type='text' autoGrow />", true);

			Component coursesComponent = new Component("Courses", "courses", coursesComponentTemplate);
			ComponentAttribute title = new ComponentAttribute("title", "string", "Overview of Courses");
			coursesComponent.addAttribute(title);

			ComponentAttribute courses = new ComponentAttribute("courses", "string[]");
			coursesComponent.addAttribute(courses);

			coursesComponent.addService(courseService);
			Directive autoGrowDirective = new Directive("AutoGrow", Event.FOCUS, Event.BLUR);
			coursesComponent.addDirective(autoGrowDirective);

			Constructor c = new Constructor("\t\tthis.courses = courseService.getCourses();");
			coursesComponent.setConstructor(c);

			appComponent.addChildComponent(coursesComponent);
		}

		{
			ServiceMethod method = new ServiceMethod("getAuthors()", "string[]",
					"return ['Andrew Hunt', 'Dave Thomas', 'Donald Knuth']");
			Service authorService = new Service("Author", method);
			
			Template authorsTemplate = new Template("<h2>Authors</h2> {{title}}", false);

			Component authorsComponent = new Component("Authors", "authors", authorsTemplate);
			ComponentAttribute title = new ComponentAttribute("title", "string", "Overview of Authors");
			authorsComponent.addAttribute(title);

			authorsComponent.addService(authorService);

			ComponentAttribute authors = new ComponentAttribute("authors", "string[]");
			authorsComponent.addAttribute(authors);

			Constructor c = new Constructor("\t\tthis.authors = authorService.getAuthors();");
			authorsComponent.setConstructor(c);

			appComponent.addChildComponent(authorsComponent);
		}

		{
			Div div = new Div();
			div.addEvent(Event.CLICK);

			ComponentAttribute firstNameComponentAttribute = new InputProperty("firstName", "string");
			ComponentAttribute titleComponentAttribute = new ComponentAttribute("title", "string",
					"Overview of buttons");

			InputField input = new InputField();
			input.setNgModel(firstNameComponentAttribute);

			Widget textField = new TextField("Voornaam", firstNameComponentAttribute);
			Widget titleField = new TextField("Titel", titleComponentAttribute);

			div.addChild(textField);
			div.addChild(input);
			div.addChild(titleField);

			Widget button = new Button("click me");
			button.addEvent(Event.CLICK);
			button.addEvent(Event.BLUR);
			button.addCss(Css.btn);
			button.addCss(Css.btnPrimary);
			button.addConditionalCssStyle(Css.active, "isActive");
			div.addChild(button);

			Template template = new Template(true);
			template.add(div);

			Component buttonComponent = new Component("Buttons", "buttons", template);

			buttonComponent.addAttribute(titleComponentAttribute);
			buttonComponent.addAttribute(firstNameComponentAttribute);

			appComponent.addChildComponent(buttonComponent);

		}
		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);
	}
}
