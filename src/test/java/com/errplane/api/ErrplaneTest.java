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
		
		// now try batch sends
		for (int i = 0; i < 10; i++) {
			assertTrue("Report batch sends failed",
					Errplane.report(("unittest_errplane-java/testReportBatch"+i)));
		}
	}

	@Test
	@Ignore
	public void reportStringInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void reportStringDouble() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void reportStringString() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void reportStringIntString() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void reportStringDoubleString() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void reportExceptionException() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void reportExceptionExceptionString() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void reportExceptionWithHash() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void reportExceptionExceptionStringString() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void startTimer() {
		fail("Not yet implemented");
	}

}
