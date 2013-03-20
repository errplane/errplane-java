package com.errplane.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.errplane.util.*;

/**
 * This class is the entrypoint for the Errplane library.  Use it to report
 * data to Errplane.
 * 
 * @author gdix
 *
 */
public class Errplane {
	
	private static final int RPT_CAPACITY = 5000;
	
	private static final int BC_CAPACITY = 10;
	
	private static ExceptionHashInterface hashFunc = new DefaultExceptionHash();
	
	private static AtomicInteger reportCount = new AtomicInteger(0);
	
	private static AtomicInteger breadcrumbCount = new AtomicInteger(0);
	
	private static ConcurrentLinkedQueue<ReportHelper> reportQueue =
			new ConcurrentLinkedQueue<ReportHelper>();
	
	private static ConcurrentLinkedQueue<String> breadcrumbQueue =
			new ConcurrentLinkedQueue<String>();
	
	private static URL errplaneUrl;
	
	private static String urlStr = "https://apiv2.errplane.com/databases/";
	
	private static String app;
	
	private static String api;
	
	private static String environment;
	
	private static String sessionUser;
	
	/**
	 * This method initializes Errplane so it is ready to send data.
	 * @param appKey the application key.
	 * @param apiKey the api key.
	 * @param env the environment (development, staging, or production).
	 * @return true if initialization succeeded.
	 */
	public static boolean init(String appKey, String apiKey, String env) {
		
		if ((appKey == null) || (apiKey == null) || (env == null)) {
			return false;
		}
		
		app = appKey;
		api = apiKey;
		environment = env;
		
		return initUrl();
	}
	
	public static boolean setUrl(String url) {
		urlStr = url;
		return initUrl();
	}
	
	private static boolean initUrl() {
		try {
			errplaneUrl = new URL((urlStr+app+environment+"/points?api_key="+api));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 Overrides the default exception hashing behavior.
	 
	 @param hashFuncOverride a sub-class of EPDefaultExceptionHash that provides an overridden
	        hash function.
	 */
	public static void exceptionHashOverride(DefaultExceptionHash hashFuncOverride) {
		hashFunc = hashFuncOverride;
	}

	/**
	 The user associated with the current Errplane session.
	 
	 @param sessUser the session user to be sent with exception details.
	 */
	public static synchronized void setSessionUser(String sessUser) {
		sessionUser = sessUser;
	}

	/**
	 Try to clear any outstanding Errplane reports.
	 */
	public static void flush() {
		// TODO - send stuff
	}

	/**
	 Leave a trail indicating what might have lead to an Exception.  The last 10 are sent
	    with exception details.  If pushing a breadcrumb on the queue when it already has
	    10 breadcrumbs, the oldest will be popped off the back of the queue.
	 
	 @param bc the meaningful breadcrumb to push on the queue.
	 */
	public static void breadcrumb(String bc) {
		breadcrumbQueue.add(bc);
	}
	
	private static void addReportHelper
			(String name, Integer iVal, Double dVal, String context) {
		
		ReportHelper rh = new ReportHelper(name);
		rh.setReportInt(iVal);
		rh.setReportDouble(dVal);
		rh.setContext(context);
		reportQueue.add(rh);
		reportCount.addAndGet(1);
	}

	/**
	 Posts a datapoint with a default int value of 1 to the timeline[s] specified.
	 @param name the name[s] of the timeline[s] to post the data point to.
	 @return false if Errplane was not previously initialized or the name exceeds 249 characters.
	 */
	public static boolean report(String name) {
		
		if ((name == null) || (name.length() >= 250)) {
			return false;
		}
		
		addReportHelper(name,1,null,null);
		
		return true;
	}

	/**
	 Posts a datapoint with the value specified to the timeline[s] specified.
	 @param name the name[s] of the timeline[s] to post the data point to.
	 @param value the int value to post to the timeline.
	 @return false if Errplane was not previously initialized or the name exceeds 249 characters.
	 */
	public static boolean report(String name, int value) {
		
		if ((name == null) || (name.length() >= 250)) {
			return false;
		}
		
		addReportHelper(name,value,null,null);
		
		return true;
	}

	/**
	 Posts a datapoint with the value specified to the timeline[s] specified.
	 @param name the name[s] of the timeline[s] to post the data point to.
	 @param value the double value to post to the timeline.
	 @return false if Errplane was not previously initialized or the name exceeds 249 characters.
	 */
	public static boolean report(String name, double value) {
		
		if ((name == null) || (name.length() >= 250)) {
			return false;
		}
		
		addReportHelper(name,null,value,null);
		
		return true;
	}

	/**
	 Posts a datapoint with a default int value of 1 and a context to the timeline[s] specified.
	 @param name the name[s] of the timeline[s] to post the data point to.
	 @param context the context to post along with the datapoint to the timeline.
	 @return false if Errplane was not previously initialized or the name exceeds 249 characters.
	 */
	public static boolean report(String name, String context) {
		
		if ((name == null) || (name.length() >= 250) || (context == null)) {
			return false;
		}
		
		addReportHelper(name,1,null,context);
		
		return true;
	}

	/**
	 Posts a datapoint with the int value and a context to the timeline[s] specified.
	 @param name the name[s] of the timeline[s] to post the data point to.
	 @param value the int value to post to the timeline[s].
	 @param context the context to post along with the datapoint to the timeline.
	 @return false if Errplane was not previously initialized or the name exceeds 249 characters.
	 */
	public static boolean report(String name, int value, String context) {
		
		if ((name == null) || (name.length() >= 250) || (context == null)) {
			return false;
		}
		
		addReportHelper(name,value,null,context);
		
		return true;
	}

	/**
	 Posts a datapoint with a default int value of 1 and a context to the name[s] specified.
	 @param name the name[s] of the timeline[s] to post the data point to.
	 @param value the double value to post to the timeline[s].
	 @param context the context to post along with the datapoint to the timeline.
	 @return false if Errplane was not previously initialized or the name exceeds 249 characters.
	 */
	public static boolean report(String name, double value, String context) {
		
		if ((name == null) || (name.length() >= 250) || (context == null)) {
			return false;
		}
		
		addReportHelper(name,null,value,context);
		
		return true;
	}

	/**
	 Posts an exception using either the default hash method or the overridden one (if provided).
	 @param ex the exception to report.
	 @return false if Errplane was not previously initialized.
	 */
	public static boolean reportException(Exception ex) {
		return false;
	}

	/**
	 Posts an exception using either the default hash method or the overridden one (if provided)
	        and the custom data supplied.
	 @param ex the exception to report.
	 @param customData the NSString to place in the custom_data section of the exception detail
	        reporting to Errplane.
	 @return false if Errplane was not previously initialized.
	 */
	public static boolean reportException(Exception ex, String customData) {
		return false;
	}

	/**
	 Posts an exception using either the hash passed in to group the exception.
	 @param ex the exception to report.
	 @param hash the overridden hash to use rather than the default.
	 @return false if Errplane was not previously initialized or the name exceeds 249 characters.
	 */
	public static boolean reportExceptionWithHash(Exception ex, String hash) {
		return false;
	}

	/**
	 Posts an exception using either the default hash method or the overridden one (if provided).
	 @param ex the exception to report.
	 @param hash the overridden hash to use rather than the default.
	 @param customData the NSString to place in the custom_data section of the exception detail
	 reporting to Errplane.
	 @return false if Errplane was not previously initialized.
	 */
	public static boolean reportException(Exception ex, String hash, String customData) {
		return false;
	}
	
	/**
	 * Starts a timer using the specified name for recording duration of
	 * executed code.
	 * @param name the time series name.
	 * @return true if Errplane reported successfully.
	 */
	public static TimerTask startTimer(String name) {
		return (new TimerTask(name));
	}
}
