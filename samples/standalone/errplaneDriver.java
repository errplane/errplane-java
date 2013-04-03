package com.errplane.examples.standalone;

import com.errplane.api.Errplane;
import com.errplane.api.TimerTask;

public class errplaneDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// try to initialize Errplane
		if (!initErrplane()) {
			System.out.println("Errplane initialization failed!");
			System.out.println("Make sure you have set the environment variables: EP_APP, EP_API, and EP_ENV!");
			return;
		}
		
		// fire up the flusher
		ErrplaneFlusher flusher = new ErrplaneFlusher();
		flusher.startFlusher();
		
		// fire up the heartbeat at the default 30 second interval
		flusher.heartbeat("errplane-java/sampleDriverHB");
		
		// do your application thing and report to Errplane
		TimerTask timer = Errplane.startTimer("sampleDriverTimerTest");
		
		for (int i = 0; i < 400; i++) {
			Errplane.report("errplane-java/sampleDriverTest");
		}
		
		timer.finish();
		
		// stop the flusher
		flusher.stopFlusher();
		
	}
	
	private static boolean initErrplane() {

		String appKey = System.getenv("EP_APP");
		String apiKey = System.getenv("EP_API");
		String env = System.getenv("EP_ENV");
		return Errplane.init(appKey, apiKey, env);
	}

}
