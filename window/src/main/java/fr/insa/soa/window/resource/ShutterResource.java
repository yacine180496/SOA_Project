package fr.insa.soa.window.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping ;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import fr.insa.soa.window.controller.Shutter;

@RestController
@RequestMapping ("/shutter")

public class ShutterResource {
	
	private Shutter shutter = new Shutter(0) ; 
	
	@GetMapping("/") 
	public Shutter getShutter() {
		return shutter ; 
	}
	
	@GetMapping("/idValue")
	public int getVolet() {
		return shutter.getVolet() ; 
	}
	
	@GetMapping("/status")
	public String getStatus() {
		String msg = ""; 
		if(shutter.getVolet()==1) {
			msg += " <p> Volet <strong> ouvert </strong>"; 
		} else if ( shutter.getVolet()==0) {
			msg += " <p> Volet <strong> fermé </strong> "; 
		}else {
			msg += " <p> Volet <strong> ouvert à moitié </strong>" ; 
		}
		return msg ; 
	}
	
	@PostMapping("/order")
	public void setOrder(@RequestParam int value) {
		shutter.setVolet(value); 
		System.out.println(" New value = " + value) ; 
	}
	
	@PutMapping("/")
	public void updateAlarme(@RequestBody Shutter shutter){
		this.shutter.setVolet(shutter.getVolet()); 
	}
	
	
	
	
	

}
