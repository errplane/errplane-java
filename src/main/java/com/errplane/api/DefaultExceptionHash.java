package com.errplane.api;

public class DefaultExceptionHash implements ExceptionHashInterface {

	public String hash(Exception ex) {
		StackTraceElement ste = ex.getStackTrace()[0];
		return (ste.toString()+ex.getClass().getName());
	}

}
