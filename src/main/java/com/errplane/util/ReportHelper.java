package com.errplane.util;

import java.util.Date;

public class ReportHelper {
	private String name;
	
	private long time;
	
	private Integer reportInt;
	
	private Double reportDouble;
	
	private String context;
	
	public ReportHelper(String name) {
		time = (new Date()).getTime();
		this.name = name;
	}

	public Integer getReportInt() {
		return reportInt;
	}

	public void setReportInt(Integer reportInt) {
		this.reportInt = reportInt;
	}

	public Double getReportDouble() {
		return reportDouble;
	}

	public void setReportDouble(Double reportDouble) {
		this.reportDouble = reportDouble;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	public String getReportBody() {
		String rptBody = name + " ";
		if (reportInt != null) {
			rptBody += reportInt.toString() + " ";
		}
		else {
			rptBody += reportDouble.toString() + " ";
		}
		
		Date now = new Date();
		
		// if its been at least 30 seconds since creating this report, send the
		//   original time, otherwise send 'now'
		if ((now.getTime()-time) > 30000) {
			rptBody += (long)(time/1000);
		}
		else {
			rptBody += "now";
		}
		
		if (context != null) {
			rptBody += " " + Base64.encode(context);
		}
		return rptBody;
	}
}
