package fr.insa.soa.C02.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/C02") 
public class C02Controller {
	
	@GetMapping("/idC02")
	public int ValueC02() {
		return (int)(Math.random()*3000);
	}
}
