package fr.insa.soa.heatingsystem.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.soa.heatingsystem.model.HeatingSystem;

@RestController
@RequestMapping("/hs") 

public class HeatingSystemResource {
	
	HeatingSystem hs = new HeatingSystem(0,0,0); 
	
	@GetMapping("/all")
	public HeatingSystem getHeatingSystem() {
		return hs; 
	}
	
	@GetMapping("/status")
	public String getStatus() {
		String msg = "" ; 
		if (hs.getChauffage() == 1 ) {
			msg += "<p>  Chauffage <strong> en marche </strong> </p>";
		} else {
			msg += "<p>  Chauffage <strong> éteint </strong> </p>";
		}
		
		if (hs.getClim()==1) {
			msg += "<p>  Clim <strong> en marche </strong> </p>"; 
		} else {
			msg += "<p>  Clim <strong> éteint </strong> </p> " ; 
		}
		
		if(hs.getExt_water() == 1 ) {
			msg += " <p> Extincteur d'eau en marche <strong> Risque d'incendie </strong> </p> " ; 
		}else {
			msg += "<p> Extincteur d'eau éteint <strong> rien à signaler </strong> </p> " ; 
		}
		return msg ; 
	}
	
	@GetMapping("/idValue")
	public int[] test(){
		
		int [] tab ; 
		
		tab = new int[3]; 
		tab[0] = hs.getClim(); 
		tab[1] = hs.getChauffage(); 
		tab[2] = hs.getExt_water(); 
		
		return tab; 
	}
	
	@GetMapping("/idClim")
	public int test1() {
		return hs.getClim() ; 
	}
	
	@GetMapping("/idChauffage") 
	public int test2() {
		return hs.getChauffage() ; 
	}
	
	@GetMapping("/idExtincteur")
	public int test3() {
		return hs.getExt_water(); 
	}
	
	
	
	@PostMapping("/order")
	public void setOrder(@RequestParam int value) {
		hs.setClim(value); 
	}
	
	@PostMapping("/order2")
	public void setOrder2(@RequestParam int value) {
		hs.setChauffage(value); 
	}
	
	@PostMapping("/order3")
	public void setOrder3(@RequestParam int value) {
		hs.setExt_water(value); 
	}
	
	
	@PutMapping("/")
	public void updateAlarme(@RequestBody HeatingSystem hs) {
		this.hs.setClim(hs.getClim()); 
		this.hs.setChauffage(hs.getChauffage()); 
		this.hs.setExt_water(hs.getExt_water());
	}
	
	
	

}
