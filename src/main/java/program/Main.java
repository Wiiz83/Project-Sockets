package program;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import client.*;
import serveur.*;
import services_auth.GestionProtocole;
import services_auth.ListeAuth;
import services_log.JsonLogger;

public class Main {
	
	
	public final static int portServeurChecker = 28414;
	public final static int portServeurManager = 28415;
	public final static int portServeurLog = 3244;
	public final static String ipServeur = "127.0.0.1";
	public final static int taille = 1024;

	public static void main(String[] args) {

		ListeAuth listauth = new ListeAuth("bd");
		GestionProtocole gestionprotoc = new GestionProtocole(listauth);
		JsonLogger jsonlog = JsonLogger.getLogger();
		
		TCPServeur tcpservchecker = new TCPServeur(gestionprotoc,portServeurChecker,jsonlog);
		UDPServeur udpserv = new UDPServeur(gestionprotoc,portServeurChecker,jsonlog);
		TCPServeur tcpservmanager = new TCPServeur(gestionprotoc,portServeurManager,jsonlog);
		LOGServeur logserv = new LOGServeur(portServeurLog);
		
		int nombreManagers = 0;
		int nombreCheckers = 0;
		ArrayList<Manager> nomsManagers = new ArrayList<Manager>();
		ArrayList<Checker> nomsCheckers = new ArrayList<Checker>();
		int i = 0;
		boolean isEntier = true;
		boolean isCheck = true;
		
		
		/***  NOMBRE DE MANAGERS  ***/
		do {
			System.out.print("Veuillez saisir le nombre de managers : ");
			Scanner s = new Scanner(System.in);
			try {
				nombreManagers = s.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("La valeur saisie n'est pas un entier");
				isEntier = false;
			}
		} while (isEntier != true);

		
		/***  NOMBRE DE CHECKERS  ***/
		isEntier = true;
		do {
			System.out.print("Veuillez saisir le nombre de checkers : ");
			Scanner s = new Scanner(System.in);
			try {
				nombreCheckers = s.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("La valeur saisie n'est pas un entier");
				isEntier = false;
			}
		} while (isEntier != true);
		
		
		/***  IDENTIFICATION DES MANAGERS  ***/
		for(i=0; i<nombreManagers; i++) {
			Manager manager = new Manager(i+1);
			Scanner s = new Scanner(System.in);
			System.out.print("\n");
			System.out.print("Veuillez saisir le nom du Manager numéro " + (i+1) +" :");
			manager.setNom(s.nextLine());
			System.out.print("Veuillez saisir la requête de " + manager.getNom() +" :");
			manager.setRequete(s.nextLine());
			nomsManagers.add(manager);
		}
		
		
		/***  IDENTIFICATION DES CHECKERS  ***/
		for(i=0; i<nombreCheckers; i++) {
			Checker checker = new Checker(i+1);
			Scanner s = new Scanner(System.in);
			System.out.print("\n");
			System.out.print("Veuillez saisir le nom du Checker numéro " + (i+1) +" :");
			checker.setNom(s.nextLine());
			
			isCheck = true;
			do {
				System.out.print("UDP ou TCP ?");		
				String c = s.nextLine();
				if (c.startsWith("UDP")){
					checker.setMode("UDP");
					isCheck = true;
				} else if(c.startsWith("TCP")){
					checker.setMode("TCP");
					isCheck = true;
				} else {
					System.out.print("Commande inconnue. Veuillez ressayer.");
					isCheck = false;
				}
			} while (isCheck != true);
			nomsCheckers.add(checker);
			
			isCheck = true;
			do {
				System.out.print("Veuillez saisir la requête de " + checker.getNom() +" :");		
				String c = s.nextLine();
				if (c.startsWith("CHK")){
					checker.setRequete(c);
					isCheck = true;
				} else {
					System.out.print("Seules les commandes CHK sont acceptées. Veuillez ressayer.");
					isCheck = false;
				}
			} while (isCheck != true);
			nomsCheckers.add(checker);
		}
		
		
		/***  LANCEMENT DES SERVEURS  ***/
		System.out.print("\n");
		Thread tudp = new Thread(udpserv);
		Thread ttcpm = new Thread(tcpservmanager);
		Thread ttcpc = new Thread(tcpservchecker);
		Thread tlog = new Thread(logserv);
		tudp.start();
		ttcpm.start();
		ttcpc.start();
		tlog.start();
		
		
		/***  LANCEMENT DES MANAGERS  ***/
		for(i=0; i<nombreManagers; i++) {
			Thread m = new Thread(nomsManagers.get(i));
			m.start();
		}
		
		/***  LANCEMENT DES CHECKERS  ***/
		for(i=0; i<nombreCheckers; i++) {
			Thread c = new Thread(nomsCheckers.get(i));
			c.start();
		}

	}
}
