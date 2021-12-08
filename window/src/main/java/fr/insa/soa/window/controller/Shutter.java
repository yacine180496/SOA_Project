package fr.insa.soa.window.controller;


public class Shutter {
	
	private double currentOpening ; 
	private double order ; 
	private boolean isMoving ; 
	
	public Shutter() {}
	
	public Shutter(double currentOpening, double order) {
		this.currentOpening = currentOpening ; 
		this.order = order ; 
		updateIsMoving(); 
	}

	public double getCurrentOpening() {
		return currentOpening;
	}

	public void setCurrentOpening(double currentOpening) {
		this.currentOpening = currentOpening;
	}

	public double getOrder() {
		return order;
	}

	public void setOrder(double order) {
		this.order = order;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
		
	}
	
	private void updateIsMoving() {
		this.isMoving= !(this.order == this.currentOpening); 
	}
	
	
}
