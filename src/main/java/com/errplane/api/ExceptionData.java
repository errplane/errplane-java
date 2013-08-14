package com.errplane.api;

public class ExceptionData {
	private String controller;
	private String action;
	private String userAgent;
  private String method;

	public ExceptionData(String controller, String action, String userAgent, String method) {
		super();
		this.controller = controller;
		this.action = action;
		this.userAgent = userAgent;
		this.method = method;
	}

	public String getMethod() {
	  return method;
	}

	public void setMethod(String method) {
	  this.method = method;
	}

	/**
	 * @Deprecated use {@link #setMethod(String)} and {@link #getMethod()} instead
	 */
	public String getController() {
		return controller;
	}

	/**
	 * @Deprecated use {@link #setMethod(String)} and {@link #getMethod()} instead
	 */
	public void setController(String controller) {
		this.controller = controller;
	}

	/**
	 * @Deprecated use {@link #setMethod(String)} and {@link #getMethod()} instead
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @Deprecated use {@link #setMethod(String)} and {@link #getMethod()} instead
	 */
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
