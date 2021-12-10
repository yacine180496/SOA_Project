package fr.insa.soa.temperature.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/temperature") 
public class TemperatureController {
	
	@GetMapping("/idValue")
	public int ValueTemperature() {
		return (int)(Math.random()*100);
	}
}
