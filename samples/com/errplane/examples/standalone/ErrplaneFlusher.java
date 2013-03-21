package com.errplane.examples.standalone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.errplane.api.Errplane;

public class ErrplaneFlusher implements Runnable {
	
	private ExecutorService es = Executors.newSingleThreadExecutor();

	public void startFlusher() {
		es.execute(this);
	}
	
	public void stopFlusher() {
		
		flush(0);
		
		// stop the flushing
		es.shutdownNow();
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
		while (Errplane.getCount() > max) {
			int reportsSent = Errplane.flush();
			System.out.println("Sent " + reportsSent + " reports!");
		}
	}

}
