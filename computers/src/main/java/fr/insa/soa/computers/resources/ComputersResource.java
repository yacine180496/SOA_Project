package fr.insa.soa.computers.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fr.insa.soa.computers.model.Computers;

@RestController
@RequestMapping("/computer")


public class ComputersResource {
	
	Computers cp = new Computers(1);
	
	@GetMapping("/all")
	public Computers get() {
		return cp;
	}
	
	@GetMapping("/idValue")
	public int test1() {
		return cp.getComputer(); 
	}
	
	@GetMapping("status")
	public String getStatus() {
		String msg = ""; 
		
		if(cp.getComputer()==1) {
			msg += " <p>Ordinateurs <strong> allumées </strong> </p> " ; 
		} else {
			msg += " <p> Ordinateurs <strong> éteints </strong> </p> " ; 
		}
		return msg ; 
	}
	
	@PostMapping("/order")
	public void setOrder(@RequestParam int value) {
		cp.setComputer(value); 
		System.out.println(" New value = " + value) ; 
	}
	
	@PutMapping("/")
	public void updateAlarme(@RequestBody Computers cp){
		this.cp.setComputer(cp.getComputer()); 
	}
	
}
