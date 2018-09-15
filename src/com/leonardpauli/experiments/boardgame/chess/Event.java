package com.leonardpauli.experiments.boardgame.chess;

class Event {
	public Date start;
	public Date end;

	Event(Date start) { this.start = start; }
	Event() { this.start = new Date(); }

	public void setEnd(Date date) { this.end = date; }

	public long duration() {
		long ms = end.getTime()-start.getTime();
		return ms;
	}
}
