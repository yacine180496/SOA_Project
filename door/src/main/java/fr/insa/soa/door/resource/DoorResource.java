package fr.insa.soa.door.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.soa.door.model.Door;



@RestController
@RequestMapping("/porte")
public class DoorResource {
	
	Door porte = new Door(1); 
	
	@GetMapping("/all")
	public Door getDoor() {
		return porte; 
	}
	
	@GetMapping("/idValue")
	public int test() {
		return porte.getPorte(); 
	}
	
	@GetMapping("/status")
	public String getStatus() {
		if(porte.getPorte() == 1) {
			return " <p>  Porte <strong> bloquée </strong> </p> " ; 
		}else {
			return " <p> Porte <strong> ouverte </strong> </p> " ; 
		} 
	}
	
	@PostMapping("/order")
	public void setOrder(@RequestParam int value) {
		porte.setPorte(value); 
		System.out.println(" New value = " + value) ; 
	}
	
	@PutMapping("/")
	public void updateAlarme(@RequestBody Door porte) {
		this.porte.setPorte(porte.getPorte()) ; 
	}
	
	

}
