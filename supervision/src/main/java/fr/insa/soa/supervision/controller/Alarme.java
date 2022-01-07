package fr.insa.soa.supervision.controller;

public class Alarme {
	
	private int alarm ; 
	private boolean status ; 
	
	public  Alarme() {}
	
	public Alarme(int alarm, boolean status) {
		
		this.alarm=alarm; 
		this.status=status; 
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	

}
