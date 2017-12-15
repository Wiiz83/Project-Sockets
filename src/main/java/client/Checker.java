package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import program.Main;

public class Checker extends Client implements Runnable {

	String reponse;
	Socket leSocket;
	PrintStream fluxSortieSocket;
	BufferedReader fluxEntreeSocket;
	
	public Checker(int i) {
		super(i);
	}

	@Override
	public void run() {
		try {
			System.out.println("******** Checker " + getNom() + " Starting ******** \n");
			leSocket = new Socket(Main.ipAuthServeur, Main.portAuthServeur);
			BufferedReader br = new BufferedReader(new InputStreamReader(leSocket.getInputStream()));
			PrintStream ps = new PrintStream(leSocket.getOutputStream());
			ps.println(getRequete());
			String rep = br.readLine();
			System.out.println("******** Server Response For Checker " + getNom() + " : " + rep + "  ******** \n");
			leSocket.close();
		} 
		catch (UnknownHostException ex) {
			System.err.println("Machine inconnue : " + ex);
			ex.printStackTrace();
		} catch (IOException ex) {
			System.err.println("Erreur : " + ex);
			ex.printStackTrace();
		}
	}
}
