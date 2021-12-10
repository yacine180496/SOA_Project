package fr.insa.soa.supervision.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import fr.insa.soa.supervision.controller.Shutter;
import org.springframework.web.bind.annotation.GetMapping ;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController 
@RequestMapping("/supervision")
public class SupervisionResource {
	
	private final String luminosityURI = "http://localhost:4200/luminosity/";
	private final String shutterURI= "http://localhost:4202/shutter/";
	private final String C02URI= "http://localhost:4203/C02/" ; 
	

//----------------Scénario 1 - Luminosité et Volet ------------------------------------------------------------------
	@GetMapping("/run")
	public String run() {
		
		RestTemplate restTemplate = new RestTemplate(); 
	
		String msg=""; 
		
		// Récupération de l'ouverture du volet
		
		System.out.println("<p> Scénario 1 : Luminosité & Volets </p>"); 
		double currentOpeningPercent = restTemplate.getForObject(shutterURI+"opening?unit=percent",Double.class) ; 
		msg +=  "<p> <strong>Ouverture actuelle </strong>: " + currentOpeningPercent + "% </p>" ;
		
		// Récupération de l'object Shutter
		Shutter shutter= restTemplate.getForObject(shutterURI,Shutter.class) ; 
		msg += " <p> ### <strong>Commande actuelle : </strong>" + shutter.getOrder() + "</p>"; 
		
		// Récupération de la valeur de luminosité 
		int luminosity = restTemplate.getForObject(luminosityURI + "idValue", Integer.class);
		msg += "<p> --- La  <strong>luminosité est de </strong>" + luminosity + "\n </p>" ; 
		
		
		// Scénario avec la Luminosité
		if (luminosity > 50) {
			msg +=" <p> | <strong> Forte luminosité </strong> -> il faut fermer les volets </p>"; 
			double order = (100 - luminosity) / 100.0 ; 
			currentOpeningPercent=0.1; 
			shutter.setOrder(order); 
			shutter.setCurrentOpening(currentOpeningPercent) ;
		} else {
			msg +="<p> | <strong> Faible luminosité </strong> -> il faut ouvrir les volets </p>"; 
			double order = 0.005 + (100 - luminosity) / 100.0 ; 
			shutter.setOrder(order) ; 
			currentOpeningPercent=1; 
			shutter.setCurrentOpening(currentOpeningPercent) ;
		}
		
		//Requête de mise à jour du Shutter 
		restTemplate.put(shutterURI, shutter); 
		
		
		// Récupération du new statut de l'objet Shutter 
		
		String newStatus = restTemplate.getForObject(shutterURI + "status" , String.class) ; 
		msg+= " <p>*** Le <strong>statut du volet</strong> est : " + newStatus +"</p>"; 
		
		
		return msg ; 
		
	}
	
	
//---------------------------------------------------------------------------------------------------------------------
	
//-------Scénario 2 -  C02  et Window --------------------------------------------------------------------------------------

	@GetMapping("/run2")
	public String run2() {
		
		RestTemplate restTemplate = new RestTemplate(); 
		
		String msg=""; 
		
		// Récupération de l'ouverture du volet

		System.out.println("<p> Scénario 1 : Luminosité & Volets </p>"); 
		double currentOpeningPercent = restTemplate.getForObject(shutterURI+"opening?unit=percent",Double.class) ; 
		msg +=  "<p> <strong>Ouverture actuelle </strong>: " + currentOpeningPercent + "% </p>" ;
		
		// Récupération de l'object Shutter
		Shutter shutter= restTemplate.getForObject(shutterURI,Shutter.class) ; 
		msg += " <p> ### <strong>Commande actuelle : </strong>" + shutter.getOrder() + "</p>"; 
		
		// Récupération de la valeur de luminosité 
		int C02 = restTemplate.getForObject(C02URI + "idC02", Integer.class);
		msg += "<p> --- Le  <strong> taux de C02 est de </strong>" + C02 + " ppm \n </p>" ; 
		

		// Scénario C02 
		
		if (C02 > 1500) {
			msg +=" <p> | <strong> Fort taux de CO2, danger d'intoxication </strong> -> il faut fermer les volets </p>";
			double order = (100 - C02) / 100.0 ;
			currentOpeningPercent=0.1; 
			shutter.setOrder(order); 
			shutter.setCurrentOpening(currentOpeningPercent) ;			
		} else if (C02 < 1500 && C02>= 1000) {
			msg +=" <p> On est bien les freros !!!! Aucun danger : no stress ya point S -> Tahia Djazzair</p>";
		} else {
			msg+= "<p> Level of CO2 is normal !"; 
		}
		//Requête de mise à jour du Shutter 
		restTemplate.put(shutterURI, shutter); 
		
		
		return msg ; 
				
	}
//-----------------------------------------------------------------------------------------------------------------------


//-------Scénario 3 -  Une personne entre dans une salle, le capteur de présence la détecte, la lumière s’allume automatiquement. --------------------------

	
//-------------------------------------------------------------------------------------------------------------------------
	
	
}
