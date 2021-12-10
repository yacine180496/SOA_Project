package fr.insa.soa.movement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/movement") 

public class MovementController {
	
	@GetMapping("/idValue")

	// -- Valeur correspondant au valeur du laser qui va permettre de détecteur un objet 
	public int ValueMovement() {
		return (int)(Math.random()*100);
	}

}
