package fr.insa.soa.luminisoty.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/luminosity") 
public class LuminosityController {
	
	
	@GetMapping("/idValue")
	public int ValueLuminosity() {
		return (int)(Math.random()*1000);
	}
	
	
}
