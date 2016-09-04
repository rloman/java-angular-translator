package eu.allego.angularbuilder.domain;

import eu.allego.angularbuilder.visitor.Visitor;

public class OutputProperty extends ComponentAttribute {
	
	private static final String ANGULAR2CLASSFOREVENTEMITTING = "EventEmitter";
	
	private String eventHandlerInJavascriptCode;
	
	public OutputProperty(String name) {
		this(name, "onFavouriteChangeWhichYouShouldAmend($event);");
	}
	
	public OutputProperty(String name, String eventHandlerInJavascriptCode) {
		super(name, ANGULAR2CLASSFOREVENTEMITTING);
		this.eventHandlerInJavascriptCode = eventHandlerInJavascriptCode;
	}
	
	public String getEventHandlerInJavascriptCode() {
		return eventHandlerInJavascriptCode;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
