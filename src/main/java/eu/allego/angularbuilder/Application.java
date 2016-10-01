package eu.allego.angularbuilder;

import eu.allego.angularbuilder.domain.Component;
import eu.allego.angularbuilder.domain.ComponentAttribute;
import eu.allego.angularbuilder.domain.Constructor;
import eu.allego.angularbuilder.domain.Css;
import eu.allego.angularbuilder.domain.CustomPipe;
import eu.allego.angularbuilder.domain.DomainInterface;
import eu.allego.angularbuilder.domain.DomainService;
import eu.allego.angularbuilder.domain.Event;
import eu.allego.angularbuilder.domain.ITag;
import eu.allego.angularbuilder.domain.InlineStyle;
import eu.allego.angularbuilder.domain.InputProperty;
import eu.allego.angularbuilder.domain.OutputProperty;
import eu.allego.angularbuilder.domain.Pipe;
import eu.allego.angularbuilder.domain.RestDomainService;
import eu.allego.angularbuilder.domain.Service;
import eu.allego.angularbuilder.domain.ServiceMethod;
import eu.allego.angularbuilder.domain.Template;
import eu.allego.angularbuilder.domain.TextField;
import eu.allego.angularbuilder.domain.Widget;
import eu.allego.angularbuilder.visitor.Angular2GeneratingVisitor;
import eu.allego.angularbuilder.visitor.Visitor;

public class Application {
	public static void main(String[] args) {
		domainServiceWithHttpForLiebregts();
	}

	public static void domainServiceWithHttpForLiebregts() {

		Template template = new Template("<h1>:: Main ::</h1>", true);
		Component appComponent = new Component("App", "my-app", template);
		appComponent.setEnableRouting(true);

		// klanten
		{

			Template domainServiceTestTemplate = new Template("<h1>:: Customers ::</h1>", true);
			Component domainServiceTestComponent = new Component("Customers", "customers",
					domainServiceTestTemplate);
			ComponentAttribute klanten = new ComponentAttribute("customers", "Customer[]");
			domainServiceTestComponent.addAttribute(klanten);

			DomainInterface klantInterface = new DomainInterface("Customer");

			// question mark means this instance var may be empty in the created
			// instance
			klantInterface.addInstanceVar("id?", "number");
			klantInterface.addInstanceVar("naam?", "string");
			klantInterface.addInstanceVar("adres?", "string");

			RestDomainService restKlantService = new RestDomainService(klantInterface,
					"http://localhost:8081/api/klanten");

			domainServiceTestComponent.addService(restKlantService);
			// refactor this constructor to a default setting (since this is
			// possible)
			Constructor constructor = new Constructor(
					"\t\tcustomerService.getCustomers().subscribe(customers => this.customers = customers);");
			domainServiceTestComponent.setConstructor(constructor);

			appComponent.addChildComponent(domainServiceTestComponent);
		}

		// adressen
		{

			Template domainServiceTestTemplate = new Template("<h1>:: Addresses ::</h1>", true);
			Component domainServiceTestComponent = new Component("Addresses", "addresses",
					domainServiceTestTemplate);
			ComponentAttribute klanten = new ComponentAttribute("addresses", "Address[]");
			domainServiceTestComponent.addAttribute(klanten);

			DomainInterface klantInterface = new DomainInterface("Address");

			// question mark means this instance var may be empty in the created
			// instance
			klantInterface.addInstanceVar("id?", "number");
			klantInterface.addInstanceVar("straat?", "string");
			klantInterface.addInstanceVar("huisnummer?", "string");
			klantInterface.addInstanceVar("postcode?", "string");
			klantInterface.addInstanceVar("plaats?", "string");
			klantInterface.addInstanceVar("land?", "string");

			RestDomainService restKlantService = new RestDomainService(klantInterface,
					"http://localhost:8081/api/adressen");

			domainServiceTestComponent.addService(restKlantService);
			// refactor this constructor to a default setting (since this is
			// possible)
			Constructor constructor = new Constructor(
					"\t\taddressService.getAddresss().subscribe(addresses => this.addresses = addresses);");
			domainServiceTestComponent.setConstructor(constructor);

			appComponent.addChildComponent(domainServiceTestComponent);
		}

		appComponent.accept(new Angular2GeneratingVisitor());
	}

	public static void domainServiceWithHttp() {

		Template template = new Template("<h1>Testing REST domain service</h1>", false);
		Component appComponent = new Component("App", "my-app", template);

		Template domainServiceTestTemplate = new Template(false);
		Component domainServiceTestComponent = new Component("ServiceTest", "servicetest", domainServiceTestTemplate);
		ComponentAttribute posts = new ComponentAttribute("posts", "Post[]");
		domainServiceTestComponent.addAttribute(posts);

		DomainInterface postInterface = new DomainInterface("Post");

		// question mark means this instance var may be empty in the created
		// instance
		postInterface.addInstanceVar("id?", "number");
		postInterface.addInstanceVar("title?", "string");
		RestDomainService postService = new RestDomainService(postInterface,
				"http://jsonplaceholder.typicode.com/posts");

		domainServiceTestComponent.addService(postService);
		// refactor this constructor to a default setting (since this is
		// possible)
		Constructor constructor = new Constructor("\t\tpostService.getPosts().subscribe(posts => this.posts = posts);");
		domainServiceTestComponent.setConstructor(constructor);

		appComponent.addChildComponent(domainServiceTestComponent);

		appComponent.accept(new Angular2GeneratingVisitor());
	}

	public static void domainServiceWithoutHttp() {

		Template template = new Template("<h1>Testing domain service</h1>", false);
		Component appComponent = new Component("App", "my-app", template);

		Template domainServiceTestTemplate = new Template(false);
		Component domainServiceTestComponent = new Component("ServiceTest", "servicetest", domainServiceTestTemplate);
		ComponentAttribute posts = new ComponentAttribute("posts", "Post[]");
		domainServiceTestComponent.addAttribute(posts);

		DomainInterface postInterface = new DomainInterface("Post");

		// question mark means this instance var may be empty in the created
		// instance
		postInterface.addInstanceVar("id?", "number");
		postInterface.addInstanceVar("title?", "string");
		DomainService postService = new DomainService(postInterface);

		domainServiceTestComponent.addService(postService);
		Constructor constructor = new Constructor("\t\tthis.posts = postService.getPosts();");
		domainServiceTestComponent.setConstructor(constructor);

		appComponent.addChildComponent(domainServiceTestComponent);

		appComponent.accept(new Angular2GeneratingVisitor());
	}

	public static void renderPipesAndCustomPipes() {

		Template template = new Template("<h1>Firstname test with pipes and custom pipes</h1>", false);

		Component appComponent = new Component("App", "my-app", template);

		Template pipeTemplate = new Template(false);

		Component pipeComponent = new Component("Samenvatting", "samenvatting", pipeTemplate);

		ComponentAttribute attr = new ComponentAttribute("firstName", "string", "Raymond Loman is lief.");

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
			Component likesComponent = new Component("LikeMe", "likes", likeTemplate);
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
		Component likesComponent = new Component("likeme", "likes", likeTemplate);
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
		Component favouriteComponent = new Component("Favourite", "favourite", favouriteComponentTemplate);
		ComponentAttribute input = new InputProperty("isFavourite", "boolean");

		ComponentAttribute output = new OutputProperty("changeit");

		favouriteComponent.addAttribute(input);
		favouriteComponent.addAttribute(output);

		appComponent.addChildComponent(favouriteComponent);

		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);

	}

	public static void renderADefaultServerSkeleton() {

		Template appComponentTemplate = new Template("<h1>Test for making a skeleton of a service</h1>", true);

		Component appComponent = new Component("App", "my-app",
				appComponentTemplate);
		{
			ServiceMethod method = new ServiceMethod("getPosts()", "string[]", "return ['aaa', 'bbb']");
			Service postService = new Service("Post", method);

			Template postsComponentTemplate = new Template(
					"<h2>Courses</h2> Title: {{title}}	<input type='text' autoGrow />", true);

			Component coursesComponent = new Component("Posts", "posts", postsComponentTemplate);
			ComponentAttribute title = new ComponentAttribute("title", "string", "Overview of Posts");
			coursesComponent.addAttribute(title);

			ComponentAttribute courses = new ComponentAttribute("posts", "string[]");
			coursesComponent.addAttribute(courses);

			coursesComponent.addService(postService);

			Constructor c = new Constructor("\t\tthis.posts = postService.getPosts();");
			coursesComponent.setConstructor(c);

			appComponent.addChildComponent(coursesComponent);
		}

		Visitor visitor = new Angular2GeneratingVisitor();

		appComponent.accept(visitor);
	}
}
