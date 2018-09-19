package com.leonardpauli.experiments.boardgame.util;

import java.util.Date;

public class Event {
	public Date start;
	public Date end;

	Event(Date start) { this.start = start; }
	public Event() { this.start = new Date(); }

	public void setEnd(Date date) { this.end = date; }

	public long duration() {
		long ms = end.getTime()-start.getTime();
		return ms;
	}
}
