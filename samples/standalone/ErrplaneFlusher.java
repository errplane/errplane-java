package com.errplane.examples.standalone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.errplane.api.Errplane;

public class ErrplaneFlusher implements Runnable {
	
	private ExecutorService es = Executors.newFixedThreadPool(2);
	
	private Heartbeat heartbeat;

	public void startFlusher() {
		es.execute(this);
	}
	
	public void stopFlusher() {
		
		flush(0);
		
		// stop the flushing
		es.shutdownNow();
	}
	
	/**
	 * Starts a heartbeat using the default 30 second interval.
	 * @param name the name to use when reporting the heartbeat to Errplane.
	 */
	public void heartbeat(String name) {
		heartbeat = new Heartbeat(name);
		es.execute(heartbeat);
	}
	
	/**
	 * Starts a heartbeat using the specified interval in milliseconds.
	 * @param name the name to use when reporting the heartbeat to Errplane.
	 * @param interval the interval between heartbeats.
	 */
	public void heartbeat(String name, long interval) {
		heartbeat = new Heartbeat(name, interval);
		es.execute(heartbeat);
	}
	
	@Override
	public void run() {
		while (true) {
			
			flush(20);
			
			try {  // sleep for 2 secs to generate batches rather than ind. req
				Thread.sleep(2000);
			}
			catch (InterruptedException ie) {
				// time to quit - one last flush
				flush(0);
				break;
			}
			catch (Exception e) {
				flush(0);
				break;
			}
		}
	}
	
	protected void flush(int max) {
        boolean firstTime = true;
        while ((Errplane.getCount() > max) || firstTime) {
            int reportsSent = Errplane.flush();
            firstTime = false;
		}
	}
	
	private class Heartbeat implements Runnable {
		
		private Heartbeat(String name) {
			this.name = name;
		}
		
		private Heartbeat(String name, long interval) {
			this.name = name;
			this.interval = interval;
		}
		
		private long interval = 30000;
		
		private String name;
		
		@Override
		public void run() {
			while (true) {
				
				Errplane.report(name);
				
				try {  // sleep for 2 secs to generate batches rather than ind. req
					Thread.sleep(interval);
				}
				catch (InterruptedException ie) {
					break;
				}
				catch (Exception e) {
					break;
				}
			}
		}
	}

}
