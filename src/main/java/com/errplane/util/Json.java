package com.errplane.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Json {

  private static void addMap(StringBuffer buffer, Map<String, Object> map) {
    for (Iterator<Map.Entry<String,Object>> itr = map.entrySet().iterator();itr.hasNext();) {
      Entry<String, Object> entry = itr.next();
      buffer.append("\"").append(entry.getKey()).append("\":");
      add(buffer, entry.getValue());
      if (itr.hasNext()) {
        buffer.append(",");
      }
    }
  }
  private static void addPrimitive(StringBuffer buffer, Object obj) {
    if (obj instanceof Number) {
      buffer.append(obj);
    } else {
      String str = (String) obj;
      String newString = "";
      for (int i = 0; i < str.length(); i++) {
        if (str.charAt(i) != '"' || (i > 0 && str.charAt(i - 1) == '\\')) {
          newString += str.charAt(i);
        } else {
          newString += "\\\"";
        }
      }
      // str = str.replaceAll("(^\\)\"", "\1\"");
      buffer.append("\"").append(newString).append("\"");
    }
  }
  private static void addArray(StringBuffer buffer, List<Object> list) {
    for (int i = 0; i < list.size(); i++) {
      add(buffer, list.get(i));
      if (i != list.size() - 1) {
        buffer.append(",");
      }
    }
  }
  @SuppressWarnings("unchecked")
  private static void add(StringBuffer buffer, Object obj) {
    if (obj instanceof List) {
      buffer.append("[");
      addArray(buffer, (List<Object>)obj);
      buffer.append("]");
    } else if (obj instanceof Map) {
      buffer.append("{");
      addMap(buffer, (Map<String, Object>)obj);
      buffer.append("}");
    } else {
      addPrimitive(buffer, obj);
    }
  }

  public static String marshalToJson(Object obj) {
    StringBuffer buffer = new StringBuffer();
    add(buffer, obj);
    return buffer.toString();
  }

	@SuppressWarnings("unchecked")
  public static String marshalToJsonOld(Map<String,Object> hash) {
	    String jsonStr = "{";
	    int ind = 0;
	    int count = hash.size();
	    for (Map.Entry<String,Object> entry: hash.entrySet()) {
	    	Object obj = entry.getValue();
	    	if (obj instanceof List) {// backtrace or breadcrumbs
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
	        else if (obj instanceof Map) {// handles HashMap<String,String>
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

	        	if (obj instanceof Number) {
	        	  jsonStr += "\"" + entry.getKey() + "\":" + obj;
	        	} else {
	        		jsonStr += "\"" + entry.getKey() + "\":\"" + obj + "\"";
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

}
