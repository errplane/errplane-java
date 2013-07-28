package com.errplane.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergedReports {

  private final List<ReportHelper> rhs;

  private final ReportHelper.ReportType type;

  private final String reportType;

  private final String database;

  private final String apiKey;

  public MergedReports(List<ReportHelper> rhs) {
    this.rhs = rhs;

    if (rhs.size() == 0) {
      throw new IllegalArgumentException("Empty list");
    }

    type = rhs.get(0).getType();
    database = rhs.get(0).getDatabase();
    apiKey = rhs.get(0).getApiKey();
    reportType = rhs.get(0).getReportType();
  }

  public String getReportBody() {
    switch (type) {
    case HTTP:
      return getHttpReportBody();
    case UDP:
      return getUdpReportBody();
    default:
      throw new IllegalArgumentException("Unkown type " + type);
    }
  }

  // [{"n":"exceptions","p":[{"c":"some_context","d":{"foo":"bar"},"t":%d,"v":123.4}]}]
  public String getHttpReportBody() {
    Map<String, List<ReportHelper>> metricNameToPoints = new HashMap<String, List<ReportHelper>>();

    for (ReportHelper rh : rhs) {
      List<ReportHelper> list = metricNameToPoints.get(rh.getName());
      if (list == null) {
        list = new ArrayList<ReportHelper>();
        metricNameToPoints.put(rh.getName(), list);
      }
      list.add(rh);
    }

    List<Map<String, Object>> bodyPoints = new ArrayList<Map<String,Object>>();

    for (Map.Entry<String, List<ReportHelper>> entry : metricNameToPoints.entrySet()) {
      String name = entry.getKey();
      for (ReportHelper rh : entry.getValue()) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("n", name);
        List<Map<String, Object>> points = new ArrayList<Map<String, Object>>();
        Map<String, Object> point = new HashMap<String, Object>();
        points.add(point);
        if (rh.getContext() != null) {
          point.put("c", rh.getContext());
        }
        if (rh.getDimensions() != null) {
          point.put("d", rh.getDimensions());
        }
        point.put("t", rh.getTime().getTime() / 1000);
        point.put("v", rh.getValue());
        body.put("p", points);

        bodyPoints.add(body);
      }
    }

    return Json.marshalToJson(bodyPoints);
  }

  // {"a":"some_key","d":"app4you2lovestaging","o":"r","w":[{"n":"some_metric","p":[{"c":"doesn't send empty points","d":{"foo":"bar"},"v":123.4}]}]}
  public String getUdpReportBody() {
    Map<String, List<ReportHelper>> metricNameToPoints = new HashMap<String, List<ReportHelper>>();

    for (ReportHelper rh : rhs) {
      List<ReportHelper> list = metricNameToPoints.get(rh.getName());
      if (list == null) {
        list = new ArrayList<ReportHelper>();
        metricNameToPoints.put(rh.getName(), list);
      }
      list.add(rh);
    }

    List<Map<String, Object>> metrics = new ArrayList<Map<String, Object>>();

    for (Map.Entry<String, List<ReportHelper>> entry : metricNameToPoints.entrySet()) {
      String name = entry.getKey();
      Map<String, Object> metric = new HashMap<String, Object>();
      metric.put("n", name);
      List<Map<String, Object>> points = new ArrayList<Map<String, Object>>();

      for (ReportHelper rh : entry.getValue()) {
        Map<String, Object> point = new HashMap<String, Object>();
        point.put("v", rh.getValue());
        if (rh.getContext() != null)
          point.put("c", rh.getContext());
        if (rh.getDimensions() != null)
          point.put("d", rh.getDimensions());

        points.add(point);
      }
      metric.put("p", points);
      metrics.add(metric);
    }

    Map<String, Object> payload = new HashMap<String, Object>();
    payload.put("o", reportType);
    payload.put("d", database);
    payload.put("a", apiKey);
    payload.put("w", metrics);
    return Json.marshalToJson(payload);
  }
}
