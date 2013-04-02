package com.errplane.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.errplane.api.ExceptionData;;

public class ExceptionHelper {
	
	private Exception reportException;
	
	private ExceptionData exceptionData;
	
	private Object[] breadcrumbs;
	
	private String customData;
	
	private ExceptionHelper(){}
	
	public ExceptionHelper(Exception ex, ExceptionData exData,
			Object[] bc, String custom) {
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
	    
	    jsonHash.put("exception_class", reportException.getClass().getName());
	    jsonHash.put("message", reportException.getMessage());
	    jsonHash.put("request_data", reqData);
	    
	    if (customData != null) {
	    	jsonHash.put("custom_data", customData);
	    }
	    
	    // create backtrace array of strings
	    ArrayList<String> backT = new ArrayList<String>();
	    for (StackTraceElement ste: reportException.getStackTrace()) {
	    	backT.add(ste.getClassName() + ":" + ste.getMethodName() + ":" +
	    			ste.getLineNumber());
	    }
	    jsonHash.put("backtrace", backT);
	    
	    return jsonHash;
	}

	protected String createExceptionDetailFromHash(HashMap<String,Object> hash) {
	    String jsonStr = "{";
	    int ind = 0;
	    int count = hash.size();
	    for (Map.Entry<String,Object> entry: hash.entrySet()) {
	    	Object obj = entry.getValue();
	    	if (obj instanceof ArrayList) {// backtrace or breadcrumbs
	            jsonStr += "\"" + entry.getKey() + "\":[";
	            int arrayInd = 0;
	            int arrayCount = ((ArrayList<String>)obj).size();
	            for (String arrVal: (ArrayList<String>)obj) {
	                jsonStr += "\"" + arrVal + "\"";
	                if (arrayInd < (arrayCount-1)) {
	                    jsonStr += ",";
	                }
	                arrayInd++;
	            }
                jsonStr += "]";
	        }
	        else if (obj instanceof HashMap) {// handles HashMap<String,String>
	            jsonStr += "\"" + entry.getKey() + "\":{";
	            HashMap<String,String> objHash = (HashMap<String,String>)obj;
	            int subInd = 0;
	            int subCount = objHash.size();
	            for (Map.Entry<String, String> subEntry: objHash.entrySet()) {
                    jsonStr += "\"" + subEntry.getKey() + "\":\"" +
                    		subEntry.getValue() + "\"";

	                if (subInd < (subCount-1)) {
	                    jsonStr += ",";
	                }
	                subInd++;
	            }
                jsonStr += "}";
	        }
	        else {
	        	
	        	// custom data could start with a bracket - if so then don't include the quotes
	        	if (obj.toString().charAt(0) == '{') {
	        		jsonStr += "\"" + entry.getKey() + "\":" +
	        				obj;
	        	}
	        	else {
	        		jsonStr += "\"" + entry.getKey() + "\":\"" +
	        				obj + "\"";
	        	}
	        }
	        
	        if (ind < (count-1)) {
	            jsonStr += ",";
	        }
	        ind++;
	    }
        jsonStr += "}";
	    return jsonStr;
	}

	public String createExceptionDetail() {
	    return createExceptionDetailFromHash(createJSONHash());
	}
}
