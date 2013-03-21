Errplane
========
This library integrates your applications with [Errplane](http://errplane.com), a cloud-based tool for handling exceptions, log aggregation, uptime monitoring, and alerting.

Installing the library
----------------------
The easiest way to get started is to download the latest prebuilt jar file from [errplane-\<version\>.jar](https://github.com/errplane/errplane-java/tree/master/dist).
The library contains no 3rd party dependencies so you should be able to add it to your classpath and go.

Example Code
------------
The example code can be found in [Example Code](https://github.com/errplane/errplane-java/tree/master/samples/com/errplane/examples).  The example code is not built into the library as it will (eventually) contain environment specific code.
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

Additionally, to set a session user to better trace exceptions reported to Errplane the following can (and should) be used:

    Errplane.setSessionUser("string that identifies the current user");

Basic Usage Scenario
--------------------
The first thing you must do when using Errplane is initialize the library.  Reports will get stored prior to initialization, but nothing gets sent until its initialized.  After that you would begin reporting.  After sending
a number of `report(...)` commands it is good to flush the queued reports to the Errplane server (or flush it on the loop as is done in the [standalone example](https://github.com/errplane/errplane-java/tree/master/samples/com/errplane/examples/standalone)).
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

      timer.finish();

      // flush it
      int numReportsSent = Errplane.flush();
      
    }

Reporting
---------
Errplane allows you to report any amount of useful numeric and context relevant data that can be used for analyzing performance and usage of your application.
There are six methods used for reporting - use the methods that make the most sense for your application.  For each reporting method, the name passed in to the `Errplane.report()` methods must be < 250 characters.
The following are simple examples for each method:

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
Under Development

Breadcrumbs
-----------
Under Development

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
