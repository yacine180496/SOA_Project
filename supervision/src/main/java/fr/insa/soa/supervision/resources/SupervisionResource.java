package fr.insa.soa.supervision.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import fr.insa.soa.supervision.controller.Shutter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;

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
	private final String MoveURI= "http://localhost:4205/movement/" ; 
	private final String TemperatureURI= "http://localhost:4206/temperature/" ; 
	
	
	// Donner Heure  
	
	public static int getTime(){
		Date date = new Date();   // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date);   // assigns calendar to given date 
		return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
	}
	
	int nowTime = getTime() ; 
	
	
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
		
		// Récupération de la valeur de luminosité 
		int C02 = restTemplate.getForObject(C02URI + "idC02", Integer.class);
		msg += "<p> --- Le  <strong> taux de C02 est de </strong>" + C02 + " ppm \n </p>" ; 
		

		// Scénario C02 
		
		if (C02 > 1500) {
			msg +=" <p> | <strong> Fort taux de CO2, danger d'intoxication </strong> -> il faut ouvrir les volets <br> ET SURTOUT SORTIR DE LA SALLE AU PLUS VITE </p>";	
		} else if (C02 < 1500 && C02>= 1000) {
			msg +=" <p> On est en bonne santé mais attention à ne pas dépasser le seuil max. </p>";
		} else {
			msg+= "<p> Le taux de CO2 est normal !"; 
		}
		
		
		return msg ; 
				
	}
//-----------------------------------------------------------------------------------------------------------------------


//-------Scénario 3 -  Une personne entre dans une salle, le capteur de présence la détecte, la lumière s’allume automatiquement. --------------------------
	
	@GetMapping("/run3")
	public String run3() {
		RestTemplate restTemplate = new RestTemplate(); 
		
		String msg=""; 
		
		// Récupération de la valeur de luminosité 
		int luminosity = restTemplate.getForObject(luminosityURI + "idValue", Integer.class);
		
		// Récupération de la valeur du faisceau de laser 
		int Movement = restTemplate.getForObject(MoveURI + "idValue", Integer.class);
		msg += "<p> --- <strong>  Valeur du faisceau Laser </strong> : " + Movement + "  </p>" ;
		
		// Scénario Détecteur de mouvement 
		
		// Si la valeur du faisceau est comprise entre 0 et 10 alors on déclence la lumière et on vient vérifier 
		if (Movement > 0 && Movement <30 ) {
			msg +=" <p> | <strong> Présence d'un objet pas humain </strong> -> On allume la lumière et on vient vérifier </p>";	
			msg += "<p> Lumière : ON </p>"; 
		} //si on détecte quelqu'un dans la salle, on allume la lumière et on bloque la salle 
		else if (Movement >= 30) {
			msg +=" <p> | <strong> Présence d'un humain </strong> -> On allume la lumière et on bloque la salle </p>";
			msg += "<p> Lumière : ON </p>";
		} else if (Movement <= 0 ) {
			msg += "<p> Aucune présence à l'horizon   -> On éteint les lumières et on repars dodo :) ! </p>"; 
			msg += "<p> Lumière : OFF </p>";
		}
		return msg ; 

	}
//-------------------------------------------------------------------------------------------------------------------------


//----------Scénario 4 - Température   ---------------------------------------------------------------------------------------
	@GetMapping("/run4")
	public String run4() {
		
		RestTemplate restTemplate = new RestTemplate(); 
			
		String msg=""; 
		
		// Récupération de la temperature
		int Temperature = restTemplate.getForObject(TemperatureURI + "idValue", Integer.class);
		msg += "<p> --- <strong>  Valeur de la température </strong> : " + Temperature + "  </p>" ;
		
		// Si la temperature est en dessous de 16 degré 
		if( Temperature <16) {
			msg +=" <p> | <strong> Activer </strong> le chauffage </p>";	
		} else if ( Temperature >= 16 && Temperature <23.5) {
			msg +=" <p> |  On est en <strong> température ambiante </strong> </p>";
		} else {
			msg +=" <p> | Activer la <strong>climatisation </strong> </p>";
		}
		return msg ; 
	}
	
	

	
}
