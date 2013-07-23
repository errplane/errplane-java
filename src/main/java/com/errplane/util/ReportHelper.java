package com.errplane.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportHelper {
	private final String name;

	private final Date time;

	private double value;

	private String context;

  private Map<String, String> dimensions;

	public ReportHelper(String name) {
		time = new Date();
		this.name = name;
	}


	public double getReportValue() {
		return value;
	}

	public void setReportValue(double value) {
		this.value = value;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	// [{"n":"exceptions","p":[{"c":"some_context","d":{"foo":"bar"},"t":%d,"v":123.4}]}]
	public String getReportBody() {
	  Map<String, Object> body = new HashMap<String, Object>();
	  body.put("n", name);
	  List<Map<String, Object>> points = new ArrayList<Map<String, Object>>();
	  Map<String, Object> point = new HashMap<String, Object>();
	  points.add(point);
	  if (context != null) {
	    point.put("c", context);
	  }
	  if (dimensions != null) {
	    point.put("d", dimensions);
	  }
	  point.put("t", time.getTime() / 1000);
	  point.put("v", value);
	  body.put("p", points);
	  return "[" + Json.marshalToJson(body) + "]";
	}


  public void setDimensions(Map<String, String> dimensions) {
    this.dimensions = dimensions;
  }

  public Map<String, String> getDimensions() {
    return dimensions;
  }
}
