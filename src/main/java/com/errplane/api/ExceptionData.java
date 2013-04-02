package com.errplane.api;

public class ExceptionData {
	private String controller;
	private String action;
	private String userAgent;
	
	public ExceptionData(String controller, String action, String userAgent) {
		super();
		this.controller = controller;
		this.action = action;
		this.userAgent = userAgent;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
