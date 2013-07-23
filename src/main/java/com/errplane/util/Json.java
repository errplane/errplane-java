package com.errplane.util;

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

}
