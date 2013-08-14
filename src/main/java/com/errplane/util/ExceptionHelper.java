package com.errplane.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.errplane.api.ExceptionData;

public class ExceptionHelper {

	private final Exception reportException;

	private final ExceptionData exceptionData;

	private final Object[] breadcrumbs;

	private final String customData;

  private final String hostname;

	public ExceptionHelper(Exception ex, ExceptionData exData, Object[] bc, String custom, String hostname) {
	  this.hostname = hostname;
		reportException = ex;
		exceptionData = exData;
		breadcrumbs = bc;
		customData = custom;
	}

	protected HashMap<String,Object> createJSONHash() {

	    HashMap<String,String> reqData = new HashMap<String,String>();
	    reqData.put("controller",exceptionData.getController());
	    reqData.put("action",exceptionData.getAction());
	    reqData.put("user_agent",exceptionData.getUserAgent());

	    HashMap<String,Object> jsonHash = new HashMap<String,Object>();

	    if (breadcrumbs.length > 0) {
	    	ArrayList<String> bcList = new ArrayList<String>();
	    	for (Object o: breadcrumbs) {
	    		bcList.add((String)o);
	    	}

	    	jsonHash.put("breadcrumbs", bcList);
	    }

	    // jsonHash.put("exception_class", reportException.getClass().getName());
	    jsonHash.put("message", reportException.getMessage());
	    jsonHash.put("request_data", reqData);

	    if (customData != null) {
	    	jsonHash.put("custom_data", customData);
	    }

	    // create backtrace array of strings
	    ArrayList<String> backT = new ArrayList<String>();
	    for (StackTraceElement ste: reportException.getStackTrace()) {
	    	backT.add(ste.getClassName() + ":" + ste.getMethodName() + ":" + ste.getLineNumber());
	    }
	    jsonHash.put("backtrace", backT);

	    return jsonHash;
	}

	public String createExceptionContext() {
	    return Json.marshalToJson(createJSONHash());
	}

	public Map<String, String> createExceptionDimensions() {
	  HashMap<String, String> dimensions = new HashMap<String, String>();
	  dimensions.put("class", reportException.getClass().getName());
	  dimensions.put("server", hostname);
	  dimensions.put("status", "open");
	  return dimensions;
	}
}
