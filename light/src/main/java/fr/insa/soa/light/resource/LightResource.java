package fr.insa.soa.light.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.soa.light.model.Light;


@RestController
@RequestMapping("/light")

public class LightResource {
	
	Light lt = new Light(0);
	
	@GetMapping("/all")
	public Light getLight() {
		return lt; 
	}
	
	@GetMapping("/idValue")
	public int test1() {
		return lt.getLight(); 
	}
	
	@GetMapping("status")
	public String getStatus() {
		String msg = ""; 
		
		if(lt.getLight()==1) {
			msg += " <p>Lumière <strong> allumée </strong> </p> " ; 
		} else {
			msg += " <p> Lumière <strong> éteinte </strong> </p> " ; 
		}
		return msg ; 
	}
	
	@PostMapping("/order")
	public void setOrder(@RequestParam int value) {
		lt.setLight(value); 
		System.out.println(" New value = " + value) ; 
	}
	
	@PutMapping("/")
	public void updateAlarme(@RequestBody Light lt){
		this.lt.setLight(lt.getLight()); 
	}
	
	
	


}
