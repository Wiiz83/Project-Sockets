package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import program.Main;
import serveur.LOGServeur;

public class Checker extends Client implements Runnable {

	private static Logger logger = Logger.getLogger(LOGServeur.class);
	String reponse;
	Socket leSocket;
	PrintStream fluxSortieSocket;
	BufferedReader fluxEntreeSocket;
	String mode;
	byte tampon[] = new byte[Main.taille];
	
	public Checker(int i) {
		super(i);
	}
	
	public void setMode(String m){
		this.mode = m;
	}
	
	public String getMode(){
		return this.mode;
	}

	@Override
	public void run() {
		switch (mode) {
		
		/*
		 * Brique UDP
		 */
		case "UDP":
			logger.info("Le checker " + nomClient + " se connecte en UDP");
			try {
				InetAddress serveur = InetAddress.getByName(Main.ipServeur);
				DatagramPacket dataSent = new DatagramPacket(this.requeteClient.getBytes(), this.requeteClient.getBytes().length, serveur, Main.portServeurChecker);
				DatagramSocket socket = new DatagramSocket();
				socket.send(dataSent);
				DatagramPacket dataRecieved = new DatagramPacket(new byte[tampon.length], tampon.length);
				socket.receive(dataRecieved);
				logger.info("Le checker " + nomClient + " reçoit : " + new String(dataRecieved.getData()));
				socket.close();
			} catch (SocketException ex) {
				logger.error("Problème avec la socket du checker " + nomClient, ex);
			} catch (UnknownHostException ex) {
				logger.error("Machine du checker " + nomClient + " inconnue", ex);
			} catch (IOException ex) {
				logger.error("Erreur pendant la connexion entre " + nomClient + " et le server", ex);
			}
			break;
			
		/*
		 * Brique TCP
		 */
		case "TCP":
			logger.info("Le checker " + nomClient + " se connecte en TCP");	
			try {
				leSocket = new Socket(Main.ipServeur, Main.portServeurChecker);
				BufferedReader br = new BufferedReader(new InputStreamReader(leSocket.getInputStream()));
				PrintStream ps = new PrintStream(leSocket.getOutputStream());
				ps.println(getRequete());
				String rep = br.readLine();
				logger.info("Le checker " + nomClient + " reçoit : " + rep);
				leSocket.close();
			} 
			catch (UnknownHostException ex) {
				logger.error("Machine du checker " + nomClient + " inconnue", ex);
			} catch (IOException ex) {
				logger.error("Erreur pendant la connexion entre " + nomClient + " et le server", ex);
			}
			break;
			
		/*
		 * Brique de mode de connexion inconnue
		 */
		default:
			logger.error("Le mode de connexion du checker " + nomClient + " n'est pas reconnu");
			break;
		}
		
		
		

	}
}
