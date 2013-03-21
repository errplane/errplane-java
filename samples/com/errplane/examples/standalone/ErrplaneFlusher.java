package com.errplane.examples.standalone;

import com.errplane.api.Errplane;

public class ErrplaneFlusher implements Runnable {

	@Override
	public void run() {
		while (true) {
			
			flush();
			
			try {  // sleep for 5 secs to generate batches rather than ind. req
				Thread.sleep(5000);
			}
			catch (InterruptedException ie) {
				// time to quit - one last flush
				flush();
				break;
			}
			catch (Exception e) {
				
			}
		}
	}
	
	protected void flush() {
		int reportsSent = Errplane.flush();
		System.out.println("Sent " + reportsSent + " reports!");
	}

}
