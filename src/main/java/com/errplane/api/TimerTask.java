package com.errplane.api;


public class TimerTask {
	
	private String name;
	private long start;
	
	public TimerTask(String rptName) {
		name = rptName;
		start = System.nanoTime();
	}
	
	public boolean finish() {
		return Errplane.report(name, (System.nanoTime()-start));
	}
}
