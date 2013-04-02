package com.errplane.api;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert.*;

public class ErrplaneTest {

	@Before
	public void setUp() throws Exception {
		String appKey = System.getenv("EP_APP");
		String apiKey = System.getenv("EP_API");
		String env = System.getenv("EP_ENV");
		assertTrue("Environemnt variables not set: EP_APP, EP_API, EP_ENV!",
				Errplane.init(appKey, apiKey, env));
	}

	@After
	public void tearDown() throws Exception {
		Errplane.flush();
	}

	@Test
	@Ignore
	public void init() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void setUrl() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void exceptionHashOverride() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void setSessionUser() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void flush() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void breadcrumb() {
		fail("Not yet implemented");
	}

	@Test
	public void reportString() {
		assertTrue("Report with name failed!", Errplane.report("unittest_errplane-java/testReport"));
		
		Errplane.flush();
		
		// now try batch sends and using specified time, not 'now'
		for (int i = 0; i < 10; i++) {
			assertTrue("Report batch sends failed",
					Errplane.report(("unittest_errplane-java/testReportBatch"+i)));
		}
		
		try {
			Thread.sleep(30001);
		}
		catch (Exception e){}
		
		Errplane.flush();
		
		// create a name around the 250 character limit and test for success/failure
		String baseName = "unittest_errplane-java/testReport/nameLimits";
		for (int i = baseName.length(); i < 246; i++) {
			baseName += "E";
		}
		String name = baseName + "249";
		assertTrue("Report failed with name length " + name.length() + " characters.", Errplane.report(name));
		
		name = baseName + "E250";
		
		assertFalse("Report succeeded with name length " + name.length() + 
				" characters!", Errplane.report(name));
		
		name = baseName + "EE251";
		
		assertFalse("Report succeeded with name length " + name.length() + 
				" characters!", Errplane.report(name));
		
		assertFalse("Report succeeded with null name!", Errplane.report(null));
		
	}

	@Test
	public void reportStringInt() {
		assertTrue("Report with name and int failed!",
				Errplane.report("unittest_errplane-java/testReportInt", 12345));
	}

	@Test
	public void reportStringDouble() {
		assertTrue("Report with name and double failed!",
				Errplane.report("unittest_errplane-java/testReportDouble", 123.45));
	}

	@Test
	public void reportStringString() {
		assertTrue("Report with name and context failed!",
				Errplane.report("unittest_errplane-java/testReportContext", "login"));
	}

	@Test
	public void reportStringIntString() {
		assertTrue("Report with name, int, and context failed!",
				Errplane.report("unittest_errplane-java/testReportIntContext", 2557325, "volume"));
	}

	@Test
	public void reportStringDoubleString() {
		assertTrue("Report with name, double, and context failed!",
				Errplane.report("unittest_errplane-java/testReportDoubleContext", 1174.3, "mssgs/s"));
	}

	@Test
	public void reportExceptionException() {
		try {
			throw new NullPointerException("TestNPE");
		}
		catch (NullPointerException e) {
			assertTrue("Report Exception failed!",
					Errplane.reportException(e, Errplane.getExceptionData
					("unittest_errplane-java", "testException", "junit")));
		}
		
		Errplane.breadcrumb("now");
		Errplane.breadcrumb("some");
		Errplane.breadcrumb("breadcrumbs");

		try {
			throw new ClassCastException("Just some BCs, don't worry about it!");
		}
		catch (ClassCastException e) {
			assertTrue("Report Exception failed!",
					Errplane.reportException(e, Errplane.getExceptionData
					("unittest_errplane-java", "testException", "junit")));
		}
		
	}

	@Test
	public void reportExceptionExceptionString() {
		try {
			throw new NullPointerException("TestNPE custom data");
		}
		catch (NullPointerException e) {
			assertTrue("Report Exception failed!",
					Errplane.reportException(e, "{\"user\":\"junit\"}", Errplane.getExceptionData
					("unittest_errplane-java", "testExceptionCustomData", "junit")));
			assertTrue("Report Exception failed!",
					Errplane.reportException(e, "just some custom data", Errplane.getExceptionData
					("unittest_errplane-java", "testExceptionCustomData", "junit")));
		}
	}

	@Test
	public void reportExceptionWithHash() {
		try {
			throw new NullPointerException("TestNPE Hash");
		}
		catch (NullPointerException e) {
			assertTrue("Report Exception failed!",
					Errplane.reportExceptionWithHash(e, "NPEHash", Errplane.getExceptionData
					("unittest_errplane-java", "testExceptionWithHash", "junit")));
		}
	}

	@Test
	public void reportExceptionExceptionStringString() {
		try {
			throw new NullPointerException("TestNPE with Hash and custom data");
		}
		catch (NullPointerException e) {
			assertTrue("Report Exception failed!",
					Errplane.reportException(e, "group this hash", "{\"user\":\"junit\"}", Errplane.getExceptionData
					("unittest_errplane-java", "testExceptionHashAndCustomData", "junit")));
		}
	}

	@Test
	public void startTimer() {
		TimerTask tt = Errplane.startTimer(null);
		assertNull("", tt);
		
		// create a name around the 250 character limit and test for success/failure
		String baseName = "unittest_errplane-java/testStartTimer/nameLimits";
		for (int i = baseName.length(); i < 246; i++) {
			baseName += "E";
		}
		String name = baseName + "249";
		tt = Errplane.startTimer(name);
		assertNotNull("startTimer failed with name length " + name.length() + " characters.", tt);
		
		name = baseName + "E250";
		
		tt = Errplane.startTimer(name);
		assertNull("startTimer succeeded with name length " + name.length() + " characters.", tt);
		
		name = baseName + "EE251";
		
		tt = Errplane.startTimer(name);
		assertNull("startTimer succeeded with name length " + name.length() + " characters.", tt);
		
		tt = Errplane.startTimer("unittest_errplane-java/timerTest");
		assertNotNull("TimerTask is null!", tt);
		try {
			Thread.sleep(555);
		}
		catch (Exception e) {}
		tt.finish();
	}

}
