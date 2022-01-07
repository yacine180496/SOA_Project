package fr.insa.soa.supervision.controller;


public class Shutter {
	
	private int volet ; 
	
	public Shutter() {}
	
	public Shutter(int volet) {
		this.volet=volet; 
	}

	public int getVolet() {
		return volet;
	}

	public void setVolet(int volet) {
		this.volet = volet;
	}
	
	
	
}
