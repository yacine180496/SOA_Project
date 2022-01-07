package fr.insa.soa.supervision.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import fr.insa.soa.supervision.controller.*; 

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
	private final String AlarmeURI= "http://localhost:4204/alarme/" ; 
	private final String MoveURI= "http://localhost:4205/movement/" ; 
	private final String TemperatureURI= "http://localhost:4206/temperature/" ; 
	private final String PorteURI= "http://localhost:4207/porte/" ; 
	private final String HeatingURI= "http://localhost:4208/hs/" ; 
	private final String LightURI= "http://localhost:4209/light/" ; 
	private final String ComputerURI= "http://localhost:4210/computer/" ; 
	
	
	// Donner  random --> Heure  
	
	public int getTime(){
		return (int)(Math.random()*24);
	}
	
	
//----------------Scénario 1 - Luminosité, Volet et C02 ------------------------------------------------------------------
	@GetMapping("/run")
	public String run() {
		
		RestTemplate restTemplate = new RestTemplate(); 
	
		String msg=""; 
		
		// Récupération de la valeur du volet 
		int volet = restTemplate.getForObject(shutterURI+"idValue",Integer.class) ; 
		
		// Récupération de l'object Shutter
		Shutter shutter= restTemplate.getForObject(shutterURI,Shutter.class) ; 
		
		// Récupération de la valeur du CO2
		int C02 = restTemplate.getForObject(C02URI + "idC02", Integer.class);
		msg += "<p> --- Le  <strong> taux de C02 est de </strong>" + C02 + " ppm \n </p>" ; 
		
		// Récupération de la valeur de luminosité 
		int luminosity = restTemplate.getForObject(luminosityURI + "idValue", Integer.class);
		msg += "<p> --- La  <strong>luminosité est de </strong>" + luminosity + "\n </p>" ; 
		
		// Scénario avec la Luminosité et le taux de C02
		if (luminosity > 600 && C02 < 1000) {
			msg += " <p>  | Priorité au C02 mais ici <strong>taux de C02 normal</strong> donc on laisse la main à la luminosité </p>" ; 
			msg +=" <p> | <strong> Forte luminosité </strong> -> il faut fermer les volets </p>"; 
			volet = 0 ; 
			restTemplate.postForObject(shutterURI + "/order?value="+ volet, "", String.class) ; 
			msg += " <p> Volet = " + volet + " -> <strong> fermer </strong></p>" ; 
			
		} else if ( (luminosity >= 100 && luminosity <600) && (C02 >= 1000 && C02 <1500) ) {
			msg += " <p>  | Priorité au C02 on laisse ouvert à moitié les volets </p>" ;
			msg +="<p> | <strong> Niveau de luminosité normal </strong> </p>";
			volet = -1  ; 
			restTemplate.postForObject(shutterURI + "/order?value="+ volet, "", String.class) ; 
			msg += " <p> Volet = " + volet + " -> <strong> ouvert à moitié  </strong></p>" ;
			
		} else if (C02 >= 1500 && luminosity <= 600){
			
			msg +="<p> | <strong> Niveau de CO2 dangereux </strong> -> il faut ouvrir les volets </p>"; 
			msg +="<p> | <strong> Faible luminosité </strong> -> priorité au C02 </p>"; 
			volet = 1 ; 
			restTemplate.postForObject(shutterURI + "/order?value="+ volet, "", String.class) ; 
			msg += " <p> Volet = " + volet + " -> <strong> ouvrir </strong></p>" ; 
		}
		else if (C02 >= 1500 && luminosity >= 600) {
			msg +="<p> | <strong> Niveau de CO2 dangereux </strong> -> il faut ouvrir les volets </p>"; 
			msg +="<p> | <strong> Forte luminosité </strong> -> Priorité au C02 </p>"; 
			volet = 1 ; 
			restTemplate.postForObject(shutterURI + "/order?value="+ volet, "", String.class) ; 
			msg += " <p> Volet = " + volet + " -> <strong> ouvrir </strong>" ; 
			
		} else if (C02<1000 && (luminosity >= 100 && luminosity <600)  ) {
			msg +="<p> | <strong> Niveau de CO2 normal </strong>  </p>"; 
			msg +="<p> | <strong> Niveau de luminosité normal </strong> </p>"; 
			volet = -1; 
			restTemplate.postForObject(shutterURI + "/order?value="+ volet, "", String.class) ; 
			msg += " <p> Volet = " + volet + " -> <strong> ouvert à moitié  </strong> </p>" ;
		} else if ( (C02>=1000 && C02 < 1500) && luminosity >= 600) {
			msg +="<p> | <strong> Niveau de CO2 important mais sans danger </strong> </p>"; 
			msg +="<p> | <strong> Niveau de luminosité élévé  </strong> -> priorité au C02 </p>"; 
			volet = -1; 
			restTemplate.postForObject(shutterURI + "/order?value="+ volet, "", String.class) ; 
			msg += " <p> Volet = " + volet + " -> <strong> ouvert à moitié  </strong> </p>" ;
		}
		return msg ; 
	}
	
	
	
//-------------------------------------------------------------------------------------------------------------------------
	



//-------Scénario 2 -  Une personne entre dans une salle, le capteur de présence la détecte, la lumière s’allume automatiquement et la porte se bloque --------------------------
	
	@GetMapping("/run2")
	public String run2() {
		RestTemplate restTemplate = new RestTemplate(); 
		
		String msg=""; 
		
		// Récupération de la valeur du faisceau de laser 
		int Movement = restTemplate.getForObject(MoveURI + "idValue", Integer.class);
		msg += "<p> --- <strong>  Valeur du faisceau Laser </strong> : " + Movement + "  </p>" ;
		
		// Récupération de la valeur de l'alarme
		int alarm = restTemplate.getForObject(AlarmeURI + "idValue", Integer.class); 
		
		//Récupération de l'objet Alarme 
		Alarme al = restTemplate.getForObject(AlarmeURI + "all", Alarme.class); 
		
		//Récupération de la valeur de la porte 
		int porte  = restTemplate.getForObject(PorteURI + "idValue", Integer.class); 
		
		//Récupération de la valeur de la lumière 
		int lumiere=restTemplate.getForObject(LightURI + "idValue", Integer.class); 
		
		// Scénario Détecteur de mouvement 
		
		// Si la valeur du faisceau est comprise entre 0 et 10 alors on déclenche la lumière et on vient vérifier, on actionne pas l'alarme mais on verrouille la porte
		if (Movement > 5 && Movement <30 ) {
			msg +=" <p> | <strong> Présence d'un objet pas humain </strong> -> On allume la lumière, on bloque la porte et on vient vérifier </p>";	
			lumiere = 1 ; 
			restTemplate.postForObject(LightURI + "/order?value="+ lumiere, "", String.class) ; 
			alarm=0 ; 
			restTemplate.postForObject(AlarmeURI + "/order?value="+ alarm, "", String.class) ; 
			porte=1; 
			restTemplate.postForObject(PorteURI + "/order?value="+ porte, "", String.class) ; 
			msg += "<p> Lumière = " + lumiere + " -> <strong> ON </strong> </p>" ; 
			msg += "<p> Alarm = " + alarm + " -> <strong>OFF</strong> </p> ";
			msg += "<p> Porte  = " + porte + " -> <strong>Bloquée</strong> </p> ";
		} //si on détecte quelqu'un dans la salle, on allume la lumière, on actionne l'Alarme et on verrouille la porte
		else if (Movement >= 30) {
			msg +=" <p> | <strong> Présence d'un humain </strong> -> On allume la lumière et on bloque la salle </p>";
			lumiere = 1 ; 
			restTemplate.postForObject(LightURI + "/order?value="+ lumiere, "", String.class) ; 
			alarm=1; 
			porte=1; 
			restTemplate.postForObject(PorteURI + "/order?value="+ porte, "", String.class) ; 
			restTemplate.postForObject(AlarmeURI + "/order?value="+ alarm, "", String.class) ; 
			msg += "<p> Lumière = " + lumiere + " -> <strong> ON </strong> </p>" ; 
			msg += "<p> Alarm =  " + alarm + " -> ON  -> <strong> Redirection direct à la <strong> POLICE</strong></p> ";
			msg += "<p> Porte  = " + porte + " -> <strong>Bloquée</strong> </p> ";
		} else if (Movement <= 5 ) { //on ne fait rien 
			msg += "<p> Aucune présence à l'horizon   -> On éteint les lumières et on repars dodo :) ! </p>"; 
			lumiere = 0 ; 
			restTemplate.postForObject(LightURI + "/order?value="+ lumiere, "", String.class) ;
			alarm = 0 ; 
			porte=0; 
			restTemplate.postForObject(PorteURI + "/order?value="+ porte, "", String.class) ; 
			restTemplate.postForObject(AlarmeURI + "/order?value="+ alarm, "", String.class) ; 
			msg += "<p> Lumière = " + lumiere + " -> <strong> ON </strong> </p>" ; 
			msg += "<p> Alarm = " + alarm + " -> <strong>OFF</strong></p> ";
			msg += "<p> Porte  = " + porte + " -> <strong>Ouverte</strong> </p> ";
		}
		return msg ;  

	}
//-------------------------------------------------------------------------------------------------------------------------


//----------Scénario 3 - Température  ---------------------------------------------------------------------------------------
	@GetMapping("/run3")
	public String run3() {
		
		
		RestTemplate restTemplate = new RestTemplate(); 
			
		String msg=""; 
		
		// Récupération de la temperature
		int Temperature = restTemplate.getForObject(TemperatureURI + "idValue", Integer.class);
		msg += "<p> --- <strong>  Valeur de la température </strong> : " + Temperature + "  </p>" ;
		
		// Récupération du système HeatingSystem 
		
		// Récupération id clim 
		int ac = restTemplate.getForObject(HeatingURI + "idClim", Integer.class);
		
		// Récupération id chauffage
		int heating = restTemplate.getForObject(HeatingURI + "idChauffage", Integer.class);
		
		// Récupération id extincteur 
		int eau = restTemplate.getForObject(HeatingURI + "idExtincteur", Integer.class); 
		
		//Récupération de l'objet HeatingSystem
		HeatingSystem heat = restTemplate.getForObject(HeatingURI + "all", HeatingSystem.class); 
		
		int [] tab = new int[3] ; 
		
		// Si la temperature est en dessous de 16 degré 
		if( Temperature <16) {
			msg +=" <p> |  Activer le <strong>chauffage</strong>  </p>";
			heating = 1 ; 
			eau = 0 ; 
			ac = 0 ; 
			
			msg +=" <p> Clim = " + ac + "</p>" ;
			msg +=" <p> <strong>Chauffage </strong> = " + heating + "</p>" ;
			msg +=" <p> Extincteur d'eau = " + eau + "</p>" ;
			
			//Mise à jour de la Clim, du Chauffage et de l'eau d'extinction 
			restTemplate.postForObject(HeatingURI + "/order?value="+ ac, "", String.class) ; 
			restTemplate.postForObject(HeatingURI + "/order2?value="+ heating, "", String.class) ; 
			restTemplate.postForObject(HeatingURI + "/order3?value="+ eau, "", String.class) ; 
			
			
			
			
		} else if ( Temperature >= 16 && Temperature <23.5) {
			msg +=" <p> |  On est en <strong> température ambiante </strong> </p>";
			heating = 0; 
			eau = 0 ; 
			ac = 0 ; 
		
			msg +=" <p> Clim  = " + ac + "</p>" ;
			msg +=" <p> Chauffage  = " + heating + "</p>" ;
			msg +=" <p> Extincteur d'eau = " + eau + "</p>" ;
			
			//Mise à jour de la Clim, du Chauffage et de l'eau d'extinction 
			restTemplate.postForObject(HeatingURI + "/order?value="+ ac, "", String.class) ; 
			restTemplate.postForObject(HeatingURI + "/order2?value="+ heating, "", String.class) ; 
			restTemplate.postForObject(HeatingURI + "/order3?value="+ eau, "", String.class) ; 
			
			
			
		} else if (Temperature > 23 && Temperature <60){
			msg +=" <p> |  La <strong>climatisation </strong> a été activé. </p>";
			heating = 0; 
			eau = 0 ; 
			ac = 1; 
			
			msg +=" <p> <strong>Clim</strong> = " + ac + "</p>" ;
			msg +=" <p> Chauffage  = " + heating + "</p>" ;
			msg +=" <p> Extincteur d'eau = " + eau + "</p>" ;
			
			//Mise à jour de la Clim, du Chauffage et de l'eau d'extinction 
			restTemplate.postForObject(HeatingURI + "/order?value="+ ac, "", String.class) ; 
			restTemplate.postForObject(HeatingURI + "/order2?value="+ heating, "", String.class) ; 
			restTemplate.postForObject(HeatingURI + "/order3?value="+ eau, "", String.class) ; 
			
			
		} else if (Temperature > 60){
			msg +=" <p> | Risque d'incendie, <strong>activer l'extinction d'eau</strong> ! </p>";
			heating = 0; 
			eau = 1 ; 
			ac = 0 ; 
			
			msg +=" <p> Clim<strong> = " + ac + "</p>" ;
			msg +=" <p> Chauffage  = " + heating + "</p>" ;
			msg +=" <p> <strong>Extincteur d'eau<strong> = " + eau + "</p>" ;
			
			//Mise à jour de la Clim, du Chauffage et de l'eau d'extinction  
			restTemplate.postForObject(HeatingURI + "/order?value="+ ac, "", String.class) ; 
			restTemplate.postForObject(HeatingURI + "/order2?value="+ heating, "", String.class) ; 
			restTemplate.postForObject(HeatingURI + "/order3?value="+ eau, "", String.class) ; 
			
			
		}
		
		return msg ; 
	}
	
	
//------------Scénario 4 - Ordinateurs Allumé / Eteints.---------------------------------------------------------------
	
	@GetMapping("/run4")
	public String run4() {
		
		String msg = "" ;
		int nowTime = getTime(); 
		
		RestTemplate restTemplate = new RestTemplate(); 
		
		
		// Récupération de la valeurs qui permet de savoir si les ordinateurs sont allumées on éteints
		int computer = restTemplate.getForObject(ComputerURI + "idValue", Integer.class); 
		
		//Récupération de l'objet Computers 
		Computers cp = restTemplate.getForObject(ComputerURI + "all", Computers.class); 
		
		System.out.println(nowTime);
		
		if(nowTime >= 10 && nowTime < 20) {
			msg += "<p> L'heure est comprise <strong> entre 10h du matin et 20h </strong> </p>";
			msg += "<p> Ordinateurs <strong>en marche</strong>";
			computer = 1;
			restTemplate.postForObject(ComputerURI + "/order?value="+ computer, "", String.class) ;
			msg +=" <p> Computers = " + computer + "</p>" ;
			
		} else if( (nowTime >= 20 && nowTime < 24)) {
			msg += "<p> L'heure est comprise entre <strong>20h du soir et 10h du matin</strong> </p>";
			msg+= "<p>Eteindre tous les <strong>ordinateurs </strong> .</p>";
			computer = 0;
			restTemplate.postForObject(ComputerURI + "/order?value="+ computer, "", String.class) ;
			msg +=" <p> Computers = " + computer + "</p>" ;
		}else if( nowTime>=0 && nowTime<10) {
			msg += "<p> L'heure est comprise entre <strong>00h et 10h du matin</strong>  </p>";
			msg+= "<p>Eteindre tous les <strong>ordinateurs </strong> .</p>";
			computer = 0;
			restTemplate.postForObject(ComputerURI + "/order?value="+ computer, "", String.class) ;
			msg +=" <p> Computers = " + computer + "</p>" ;
		}
		
		 return msg;
	}
	
	
	
}
