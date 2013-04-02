Errplane
========
This library integrates your applications with [Errplane](http://errplane.com), a cloud-based tool for handling exceptions, log aggregation, uptime monitoring, and alerting.

Installing the library
----------------------
The easiest way to get started is to download the latest prebuilt jar file from [errplane-\<version\>.jar](https://github.com/errplane/errplane-java/tree/master/dist).
The library contains no 3rd party dependencies so you should be able to add it to your classpath and go.  The main import you'll use is:

    import com.errplane.api.Errplane;

Example Code
------------
The example code can be found in [Example Code](https://github.com/errplane/errplane-java/tree/master/samples/com/errplane/examples).  The example code is not built into the library since it contains environment specific code.
It is intended to be used as a reference and directly used in your project if one of the available samples fits your usage scenario and environment.

Initializing the library
------------------------
The library is initialized with the following arguments:

    appKey: your app name configured in Errplane
    apiKey: your api_key used to verify Errplane usage
    env: the environment you are using (usually one of: production, staging, development)

With these values the library is initialized using:

    boolean success = Errplane.init(appKey, apiKey, env);

success is true if none of the values passed in were null and a URL was successfully built.

Additionally, to set a session user (for single-user applications) to better trace exceptions reported to Errplane the following can be used:

    Errplane.setSessionUser("string that identifies the current user");

Basic Usage Scenario
--------------------
The first thing you must do when using Errplane is initialize the library.  Reports will get stored prior to initialization, but nothing gets sent until its initialized.  After that you would begin reporting.  After sending
a number of `report(...)` commands it is good to flush the queued reports to the Errplane server (or flush it on a loop as is done in the [standalone example](https://github.com/errplane/errplane-java/tree/master/samples/com/errplane/examples/standalone)).
Additionaly, you can report `Exception`s when they occur.
With that in mind here is a basic scenario:

    import com.errplane.api.Errplane;
    import com.errplane.api.TimerTask;
    
    ...

    // init Errplane
    if(Errplane.init(appKey, apiKey, env)) {
      // add reports
      Errplane.report("controllers/controller1");
      Errplane.report("controllers/controller2");
      Errplane.report("controllers/controller3");

      // send the reports to the Errplane server - flush also tells you how many were sent
      int numReportsSent = Errplane.flush();

      // time a long running task or potential bottleneck
      TimerTask timer = Errplane.startTimer("longTask");

      // do your applicationy stuff
      // ...

      // tell the timer you're done
      timer.finish();

      // flush it
      int numReportsSent = Errplane.flush();
      
      // force an exception
      String [] arr = {"one", "two", "threeButNotFour"};
      try {
        System.out.println(arr[3]);
      }
      catch (Exception e) {
        // Errplane.getExceptionData(String controller, String action, String userAgent) is a
        //   convenience method for creating an instance of com.errplane.api.ExceptionData.
        //   controller and action are expected to map to class and method respectively.
        //   userAgent specifies the client type (e.g. - browser, standalone app, mobile, ...)
        Errplane.reportException(e, Errplane.getExceptionData("YourController", "YourAction", "YourUserAgent"));
      }
      Errplane.flush();
    }

Reporting
---------
Errplane allows you to report any amount of useful numeric and context relevant data that can be used for analyzing performance and usage of your application.
There are six methods used for reporting - use the methods that make the most sense for your application.  For each reporting method, the name passed in to the `Errplane.report()` methods must be < 250 characters.
The following are simple examples for each method:

    import com.errplane.api.Errplane;

    // simple reporting (default int value of 1 is sent to Errplane)
    Errplane.report("user logged in");
    
    // reporting providing your own useful int value
    Errplane.report("minutes_since_last_login", 10);
    
    // reporting providing your own useful double value
    Errplane.report("avg_usage_pcnt", 99.9);
    
    // reporting providing your own useful context (uses default int value of 1)
    Errplane.report("problem_with_server", "Delayed Server Request");
    
    // reporting providing your own useful context and int value
    Errplane.report("slow_processing", 2500, "Slow Processing");
    
    // reporting providing your own useful context and double value
    Errplane.report("average_response_time", 192.75, "Average Response Time");


Exception Reporting
-------------------
There are four basic methods for reporting exceptions. Again, use the ones that make the most sense for your application.
All of these examples assume `exception` is derived from `java.lang.Exception`.

    import com.errplane.api.Errplane;

    // basic default
    Errplane.reportException(exception, Errplane.getExceptionData
            ("MyJavaClass", "myJavaMethod", "browser"));

    // with custom data
    Errplane.reportException(exception, "custom data for you to pass in can be json",
            Errplane.getExceptionData("MyJavaClass", "myJavaMethod", "standalone app"));

    // hash override
    Errplane.reportExceptionWithHash(exception, "hash this",
            Errplane.getExceptionData("MyJavaClass", "myJavaMethod", "mobile device"));

    // hash override with custom data
    Errplane.reportException(exception, "hash this", "more custom data",
            Errplane.getExceptionData("MyJavaClass", "myJavaMethod", "refrigerator"));

Breadcrumbs
-----------
Breadcrumbs are useful in a single-user application for providing checkpoints for your app leading up to the point where an exception is thrown. The Errplane library stores the last 10 breadcrumbs you provide and automatically sends them along when you report an exception:

    import com.errplane.api.Errplane;

    // assume you've inited Errplane
    Errplane.breadcrumb("now");
    Errplane.breadcrumb("some");
    Errplane.breadcrumb("breadcrumbs");

    try {
        throw new ClassCastException("Just some BCs, don't worry about it!");
    }
    catch (ClassCastException e) {
        Errplane.reportException(e, Errplane.getExceptionData
                ("MyJavaClass", "myJavaMethod", "gaming console")));
    }

Timed Execution Reporting
-------------------------
Timed execution reporting allows you to report the time taken to complete a section of code.  As when reporting events to Errplane, the name passed in to the `Errplane.startTimeer(String)` method must be < 250 characters.
To time execution use the following:

    import com.errplane.api.Errplane;
    import com.errplane.api.TimerTask;

    ...

    // assume you've inited Errplane (or that you will before you flush)
    TimerTask timer = Errplane.startTimer("fastTask");

    // your code goes here

    // tell the timer we're done
    timer.finish();

    // flush it if you don't have that running on a seperate thread
    Errplane.flush();

Customizing How Exceptions Get Grouped
--------------------------------------
Under Development

Full Examples
-------------
Test code can be found at:
[Errplane Tests](https://github.com/errplane/errplane-java/blob/master/src/test/java/com/errplane/api/ErrplaneTest.java)

Sample code can be found at:
[Errplane Samples](https://github.com/errplane/errplane-java/tree/master/samples/com/errplane/examples)
