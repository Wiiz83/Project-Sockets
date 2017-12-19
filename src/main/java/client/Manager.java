package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;
import program.Main;

public class Manager extends Client implements Runnable {
	
	private static Logger logger = Logger.getLogger(Manager.class);
	String reponse;
	Socket leSocket;
	PrintStream fluxSortieSocket;
	BufferedReader fluxEntreeSocket;
	String mode;
	byte tampon[] = new byte[Main.taille];
	
	public Manager(int i) {
		super(i);
	}

	@Override
	public void run() {
		logger.info("Le manager " + nomClient + " se connecte en TCP");	
		try {
			leSocket = new Socket(Main.ipServeur, Main.portServeurChecker);
			BufferedReader br = new BufferedReader(new InputStreamReader(leSocket.getInputStream()));
			PrintStream ps = new PrintStream(leSocket.getOutputStream());
			ps.println(getRequete());
			String rep = br.readLine();
			logger.info("Le manager " + nomClient + " reçoit : " + rep);
			leSocket.close();
		} 
		catch (UnknownHostException ex) {
			logger.error("Machine du manager " + nomClient + " inconnue", ex);
		} catch (IOException ex) {
			logger.error("Erreur pendant la connexion entre " + nomClient + " et le serveur", ex);
		}
	}

}