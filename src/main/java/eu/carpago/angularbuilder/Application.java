package eu.carpago.angularbuilder;

import eu.carpago.angularbuilder.domain.Component;
import eu.carpago.angularbuilder.domain.ComponentAttribute;
import eu.carpago.angularbuilder.domain.Constructor;
import eu.carpago.angularbuilder.domain.Crud;
import eu.carpago.angularbuilder.domain.Css;
import eu.carpago.angularbuilder.domain.CustomPipe;
import eu.carpago.angularbuilder.domain.DomainDrivenDevelopment;
import eu.carpago.angularbuilder.domain.DomainDrivenDevelopment.DomainDrivenDevelopmentBuilder;
import eu.carpago.angularbuilder.domain.DomainInterface;
import eu.carpago.angularbuilder.domain.DomainService;
import eu.carpago.angularbuilder.domain.Event;
import eu.carpago.angularbuilder.domain.ITag;
import eu.carpago.angularbuilder.domain.InlineStyle;
import eu.carpago.angularbuilder.domain.InputProperty;
import eu.carpago.angularbuilder.domain.OutputProperty;
import eu.carpago.angularbuilder.domain.Pipe;
import eu.carpago.angularbuilder.domain.RestDomainService;
import eu.carpago.angularbuilder.domain.Service;
import eu.carpago.angularbuilder.domain.ServiceMethod;
import eu.carpago.angularbuilder.domain.Template;
import eu.carpago.angularbuilder.domain.TextField;
import eu.carpago.angularbuilder.domain.Widget;
import eu.carpago.angularbuilder.visitor.Angular2GeneratingVisitor;
import eu.carpago.angularbuilder.visitor.Visitor;

public class Application {
	public static void main(String[] args) {
		domainDrivenDevelopment();
	}
	
	
	public static void domainDrivenDevelopment() {
		DomainInterface customerInterface = new DomainInterface("Customer");

		// question mark means this instance var may be empty in the created
		// instance
		customerInterface.addInstanceVar("naam", "string", true);
		customerInterface.addInstanceVar("debiteurennummer", "string", false);
		
		DomainDrivenDevelopment b = new DomainDrivenDevelopmentBuilder(customerInterface, "http://localhost:8081/api/klanten/").build();
		b.accept(new Angular2GeneratingVisitor());
	}

	public static void restFullDomainServiceDemoWithHttp() {

		Template template = new Template(
				"<h1>:: Java to Angular2 Translator Demo ::</h1>", true);
		Component appComponent = new Component("App", "my-app", template);
		appComponent.setRoutingEnabled(true);

		// customers
		{

			Template customersTemplate = new Template(
					"<h1>:: Customers ::</h1>", true);
			Component customersComponent = new Component("Customers",
					"customers", customersTemplate);
			ComponentAttribute customers = new ComponentAttribute("customers",
					"Customer[]");
			customersComponent.addAttribute(customers);

			ComponentAttribute warning = new ComponentAttribute("warning",
					"string");
			customersComponent.addAttribute(warning);

			DomainInterface customerInterface = new DomainInterface("Customer");

			// question mark means this instance var may be empty in the created
			// instance
			customerInterface.addInstanceVar("id", "number", false);
			customerInterface.addInstanceVar("naam", "string", false);
			customerInterface.addInstanceVar("debiteurennummer", "string", false);

			RestDomainService restKlantService = new RestDomainService(
					customerInterface, "http://localhost:8081/api/klanten/");

			customersComponent.addService(restKlantService);
			customersComponent.setCrud(Crud.DELETE);
			// refactor this constructor to a default setting (since this is
			// possible)
			Constructor constructorForPlural = new Constructor(
					"\t\tcustomerService.getCustomers().subscribe(customers => this.customers = customers);");
			customersComponent.setConstructor(constructorForPlural);

			// temp to test if singular is easy to create
			// suppose we always want to make a CustomerComponent for singular
			// instances
			String templateSingularString = "<h1>:: Customer ::</h1>\n<div>\n\t<pre>{{ customer | json }}</pre>\n</div>\n";
			templateSingularString += "<div *ngIf='customer'>"
					+ "<div class='input-group'>Naam: <input type='text' class='form-control' [(ngModel)]='customer.naam'><br>"
					+ "Debnr: <input type='text' class='form-control' [(ngModel)]='customer.debiteurennummer'>		<br>"
					+ "</div>"
					+ "<span class='input-group-btn'><button class='btn btn-primary' (click)='update()'>Update</button></span>"
					+ "</div>";

			Template singularTemplate = new Template(templateSingularString,
					true);
			Component singularComponent = new Component("Customer", "customer",
					singularTemplate);
			singularComponent.setCrud(Crud.UPDATE);
			ComponentAttribute customer = new ComponentAttribute("customer",
					"Customer={}");
			singularComponent.addAttribute(customer);
			singularComponent.setForSingularUse(true);
			Constructor constructorForSingular = new Constructor(
					"customerService.getCustomer(parseInt(routeParams.get('id'))).subscribe(customer => this.customer = customer);");
			singularComponent.setConstructor(constructorForSingular);
			singularComponent.addService(restKlantService);

			// create an object
			String createTemplateString = "<h1>:: Create customer ::</h1>"
					+ "<div class='input-group'>"
					+ "Naam: <input type='text' class='form-control' [(ngModel)]='customer.naam'><br>"
					+ "Debnr: <input type='text' class='form-control' [(ngModel)]='customer.debiteurennummer'> <br>"
					+ "</div>" + "<span class='input-group-btn'>"
					+ "<button class='btn btn-primary' (click)='create()'>Create</button>"
					+ "</span>";

			Template createTemplate = new Template(createTemplateString, true);
			Component createComponent = new Component("CustomerCreate",
					"customer-create", createTemplate);
			// rloman refactor. if this is a create component than you cal
			// calculate during rendering the properties and the class it should
			// have
			ComponentAttribute naam = new ComponentAttribute("naam", "string");
			ComponentAttribute debiteurnnummer = new ComponentAttribute(
					"debiteurennummer", "string");
			createComponent.addAttribute(customer);
			createComponent.addAttribute(naam);
			createComponent.addAttribute(debiteurnnummer);
			createComponent.addService(restKlantService);
			createComponent.setRoutingEnabled(false);
			createComponent.setCrud(Crud.CREATE);

			customersComponent.addChildComponent(singularComponent);

			customersComponent.setDefaultRoute(true);

			appComponent.addChildComponent(customersComponent);
			appComponent.addChildComponent(createComponent);
		}

		appComponent.accept(new Angular2GeneratingVisitor());
	}

	public static void domainServiceWithHttp() {

		Template template = new Template("<h1>Testing REST domain service</h1>",
				false);
		Component appComponent = new Component("App", "my-app", template);

		Template domainServiceTestTemplate = new Template(false);
		Component domainServiceTestComponent = new Component("ServiceTest",
				"servicetest", domainServiceTestTemplate);
		ComponentAttribute posts = new ComponentAttribute("posts", "Post[]");
		domainServiceTestComponent.addAttribute(posts);

		DomainInterface postInterface = new DomainInterface("Post");

		// question mark means this instance var may be empty in the created
		// instance
		postInterface.addInstanceVar("id", "number", false);
		postInterface.addInstanceVar("title", "string", false);
		RestDomainService postService = new RestDomainService(postInterface,
				"http://jsonplaceholder.typicode.com/posts");

		domainServiceTestComponent.addService(postService);
		// refactor this constructor to a default setting (since this is
		// possible)
		Constructor constructor = new Constructor(
				"\t\tpostService.getPosts().subscribe(posts => this.posts = posts);");
		domainServiceTestComponent.setConstructor(constructor);

		appComponent.addChildComponent(domainServiceTestComponent);

		appComponent.accept(new Angular2GeneratingVisitor());
	}

	public static void domainServiceWithoutHttp() {

		Template template = new Template("<h1>Testing domain service</h1>",
				false);
		Component appComponent = new Component("App", "my-app", template);

		Template domainServiceTestTemplate = new Template(false);
		Component domainServiceTestComponent = new Component("ServiceTest",
				"servicetest", domainServiceTestTemplate);
		ComponentAttribute posts = new ComponentAttribute("posts", "Post[]");
		domainServiceTestComponent.addAttribute(posts);

		DomainInterface postInterface = new DomainInterface("Post");

		// question mark means this instance var may be empty in the created
		// instance
		postInterface.addInstanceVar("id", "number", false);
		postInterface.addInstanceVar("title", "string", false);
		DomainService postService = new DomainService(postInterface);

		domainServiceTestComponent.addService(postService);
		Constructor constructor = new Constructor(
				"\t\tthis.posts = postService.getPosts();");
		domainServiceTestComponent.setConstructor(constructor);

		appComponent.addChildComponent(domainServiceTestComponent);

		appComponent.accept(new Angular2GeneratingVisitor());
	}

	public static void renderPipesAndCustomPipes() {

		Template template = new Template(
				"<h1>Firstname test with pipes and custom pipes</h1>", false);

		Component appComponent = new Component("App", "my-app", template);

		Template pipeTemplate = new Template(false);

		Component pipeComponent = new Component("Samenvatting", "samenvatting",
				pipeTemplate);

		ComponentAttribute attr = new ComponentAttribute("firstName", "string",
				"Raymond Loman is lief.");

		TextField textField = new TextField("Firstname", attr);
		textField.setPipes(Pipe.uppercase);

		pipeTemplate.add(textField);

		CustomPipe pipe = new CustomPipe("Summary", "3");
		pipeComponent.addPipe(pipe);
		pipeComponent.addAttribute(attr);
		appComponent.addChildComponent(pipeComponent);

		textField.addCustomPipe(pipe);

		appComponent.accept(new Angular2GeneratingVisitor());

	}

	public static void renderLikeApplication() {

		Template template = new Template("<h1>Like application</h1>", false);

		Component appComponent = new Component("App", "my-app", template);

		{
			Template likeTemplate = new Template(false);

			Widget itag = new ITag();
			itag.addCss(Css.glyphicon);
			itag.addCss(Css.glyphiconHeart);
			itag.addConditionalCssStyle(Css.highlighted, "highlighted");
			itag.addConditionalCssStyle(Css.glyphiconStarEmpty, "!highlighted");
			itag.addEvent(Event.CLICK);

			likeTemplate.add(itag);
			Component likesComponent = new Component("LikeMe", "likes",
					likeTemplate);
			ComponentAttribute input = new InputProperty("likes", "string");

			// styles
			InlineStyle inlineStyle = new InlineStyle("highlighted");
			inlineStyle.addKeyValue("color", "deeppink");

			likesComponent.addInlineStyle(inlineStyle);

			// styles
			InlineStyle inlineStyleHover = new InlineStyle(":hover");
			inlineStyleHover.addKeyValue("color", "gray");

			likesComponent.addInlineStyle(inlineStyleHover);

			TextField textField = new TextField();
			textField.setLabel("Firstname");
			textField.setPipes(Pipe.uppercase);
			textField.setNgModel(input);

			likesComponent.addAttribute(input);
			likeTemplate.add(textField);

			appComponent.addChildComponent(likesComponent);

			Visitor visitor = new Angular2GeneratingVisitor();

			appComponent.accept(visitor);
		}

		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);

	}

	public static void likeTemplate(Component appComponent) {
		Template likeTemplate = new Template(false);

		Widget itag = new ITag();
		itag.addCss(Css.glyphicon);
		itag.addCss(Css.glyphiconHeart);
		itag.addConditionalCssStyle(Css.highlighted, "highlighted");
		itag.addEvent(Event.CLICK);

		likeTemplate.add(itag);
		Component likesComponent = new Component("likeme", "likes",
				likeTemplate);
		ComponentAttribute input = new InputProperty("likes", "number");

		TextField textField = new TextField();
		textField.setLabel("Likes");
		textField.setNgModel(input);

		likesComponent.addAttribute(input);
		likeTemplate.add(textField);

		appComponent.addChildComponent(likesComponent);

		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);

	}

	public static void foo1() {
		Template template = new Template("<h1>Like application</h1>", true);

		Component appComponent = new Component("App", "my-app", template);

		Template favouriteComponentTemplate = new Template(true);

		Widget itag = new ITag();
		itag.addCss(Css.glyphicon);
		itag.addConditionalCssStyle(Css.glyphiconStarEmpty, "!isFavourite");
		itag.addConditionalCssStyle(Css.glyphiconStar, "isFavourite");
		itag.addEvent(Event.CLICK);
		favouriteComponentTemplate.add(itag);
		Component favouriteComponent = new Component("Favourite", "favourite",
				favouriteComponentTemplate);
		ComponentAttribute input = new InputProperty("isFavourite", "boolean");

		ComponentAttribute output = new OutputProperty("changeit");

		favouriteComponent.addAttribute(input);
		favouriteComponent.addAttribute(output);

		appComponent.addChildComponent(favouriteComponent);

		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);

	}

	public static void renderADefaultServerSkeleton() {

		Template appComponentTemplate = new Template(
				"<h1>Test for making a skeleton of a service</h1>", true);

		Component appComponent = new Component("App", "my-app",
				appComponentTemplate);
		{
			ServiceMethod method = new ServiceMethod("getPosts()", "string[]",
					"return ['aaa', 'bbb']");
			Service postService = new Service("Post", method);

			Template postsComponentTemplate = new Template(
					"<h2>Courses</h2> Title: {{title}}	<input type='text' autoGrow />",
					true);

			Component coursesComponent = new Component("Posts", "posts",
					postsComponentTemplate);
			ComponentAttribute title = new ComponentAttribute("title", "string",
					"Overview of Posts");
			coursesComponent.addAttribute(title);

			ComponentAttribute courses = new ComponentAttribute("posts",
					"string[]");
			coursesComponent.addAttribute(courses);

			coursesComponent.addService(postService);

			Constructor c = new Constructor(
					"\t\tthis.posts = postService.getPosts();");
			coursesComponent.setConstructor(c);

			appComponent.addChildComponent(coursesComponent);
		}

		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);
	}
}
