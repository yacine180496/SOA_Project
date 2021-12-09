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
	
	private Shutter shutter = new Shutter(0.5,1.0) ; 
	
	@GetMapping("/") 
	public Shutter getShutter() {
		return shutter ; 
	}
	
	@GetMapping("/status")
	public String getStatus() {
		if(shutter.isMoving()) return "moving from " + shutter.getCurrentOpening() + " to "+ shutter.getOrder() ; 
		if(shutter.getCurrentOpening() > 0 ) return "open" ;
		return "closed" ; 
	}
	
	@GetMapping("/opening")
	public double getOpening(@RequestParam(name="unit", required=false) String unit) {
		if (unit == null || unit.equals("decimal")) return shutter.getCurrentOpening(); 
		if (unit.equals("percent")) return shutter.getCurrentOpening()*100;
		return -1 ; 
	}
	
	@PostMapping("/order")
	public void setOrder(@RequestParam double value) {
		shutter.setOrder(value); 
	}
	
	@PutMapping("/")
	public void updateShutter(@RequestBody Shutter shutter) {
		this.shutter.setOrder(shutter.getOrder());
	}
	
	
	

}
