package com.errplane.util;

import java.util.Date;
import java.util.Map;

public class ReportHelper {
  public static enum ReportType {UDP, HTTP};

  private final ReportType type;

	private final String name;

	private final Date time;

	private double value;

	private String context;

  private Map<String, String> dimensions;

  private final String apiKey;

  private final String database;

  private final String reportType;

	public ReportHelper(String name, String reportType, ReportType type, String database, String apiKey) {
	  this.reportType = reportType;
    this.database = database;
    this.apiKey = apiKey;
    time = new Date();
	  this.type = type;
	  this.name = name;
	}


	public double getReportValue() {
		return value;
	}

	public ReportType getType() {
	  return type;
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

  public void setDimensions(Map<String, String> dimensions) {
    this.dimensions = dimensions;
  }

  public Map<String, String> getDimensions() {
    return dimensions;
  }


  public String getReportType() {
    return reportType;
  }


  public String getDatabase() {
    return database;
  }


  public String getApiKey() {
    return apiKey;
  }

  public String getName() {
    return name;
  }

  public Date getTime() {
    return time;
  }

  public double getValue() {
    return value;
  }
}
