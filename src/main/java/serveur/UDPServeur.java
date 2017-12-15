package serveur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;

import program.Main;
import services_auth.GestionProtocole;
import services_log.JsonLogger;

public class UDPServeur implements Runnable {
	private GestionProtocole gp;
	private int port;
	private JsonLogger jl;

	/*** Server Constructor ****/
	public UDPServeur(GestionProtocole gp, int port, JsonLogger jl) {
		this.gp = gp;
		this.port = port;
		this.jl = jl;
	}
	
	@Override
	public void run() {
		System.out.printf("******** UDP Server Starting ******** \n");
		
		Runnable UDPHandler = new Runnable() {
			public void run() {
				processUDP();
			}
		};
		Thread UDPConnection = new Thread(UDPHandler);
		UDPConnection.start();
	}
	
	/*** SERVEUR UDP ****/
	public void processUDP() {
		byte[] tampon = new byte[Main.taille];
		try {
			DatagramSocket ds = new DatagramSocket(Main.portAuthServeur);
			while (true) {
				DatagramPacket dp = new DatagramPacket(tampon, tampon.length);
				try {
					ds.receive(dp);
					System.out.printf("******** Server Accepting A New Client UDP ******** \n");
					String req = new String(tampon, 0, dp.getLength()); // extraction de la requete
					String rep = gp.traiterReq(req); // appel gestionprotocol pour traiter la requete
					dp.setData(rep.getBytes());
					System.out.println("Adresse destinataire : " + dp.getAddress());
					
					/*
					 * Creer une socket comme le client
					 * 
					 * 
					 */
					
					ds.send(dp);
				} catch (IOException e) {
					e.printStackTrace();
					ds.close();
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
	}
}
