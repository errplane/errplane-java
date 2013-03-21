package com.errplane.api;


public class TimerTask {
	
	private String name;
	private long start;
	
	public TimerTask(String rptName) {
		name = "timed_blocks/#{" + rptName + "}";
		start = System.nanoTime();
	}
	
	public boolean finish() {
		return Errplane.report(name, ((System.nanoTime()-start)/1000000.0));
	}
}
