package fr.insa.soa.alarme.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fr.insa.soa.alarme.model.Alarme;



@RestController 
@RequestMapping("/alarme")

public class AlarmeResource {
	
	Alarme al = new Alarme(0,true) ; 
	

	@GetMapping("/all") 
	public Alarme getAlarme() {
		return al ; 
	}

	@GetMapping("/idValue")
	public int test() {
		return al.getAlarm() ; 
	}
	
	@PostMapping("/order")
	public void setOrder(@RequestParam int value) {
		al.setAlarm(value); 
		System.out.println(" New value = " + value) ; 
	}
	
	@PutMapping("/")
	public void updateAlarme(@RequestBody Alarme al) {
		this.al.setAlarm(al.getAlarm()) ; 
	}
	
	
	
	
	
}
		
		

