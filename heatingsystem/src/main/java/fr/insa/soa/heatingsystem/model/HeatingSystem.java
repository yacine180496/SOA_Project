package fr.insa.soa.heatingsystem.model;

public class HeatingSystem {
	
	private int clim ; 
	private int chauffage ;
	private int ext_water ; 
	
	
	public HeatingSystem() {} ; 
	
	public HeatingSystem(int clim, int chauffage ,int ext_water   ) {
		this.clim = clim ; 
		this.chauffage=chauffage; 
		this.ext_water =ext_water ; 
	}

	public int getClim() {
		return clim;
	}

	public void setClim(int clim) {
		this.clim = clim;
	}

	public int getChauffage() {
		return chauffage;
	}

	public void setChauffage(int chauffage) {
		this.chauffage = chauffage;
	}

	public int getExt_water() {
		return ext_water;
	}

	public void setExt_water(int ext_water) {
		this.ext_water = ext_water;
	}
	
	
	

}
