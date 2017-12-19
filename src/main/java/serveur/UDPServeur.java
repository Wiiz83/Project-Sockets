package serveur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import program.Main;
import services_auth.GestionProtocole;
import services_log.JsonLogger;

public class UDPServeur implements Runnable {
	private GestionProtocole gp;
	private int port;
	private static Logger logger = Logger.getLogger(UDPServeur.class);

	/*** Server Constructor ****/
	public UDPServeur(GestionProtocole gp, int port) {
		this.gp = gp;
		this.port = port;
	}
	
	@Override
	public void run() {
		logger.info("***** UDP Server starting *****");
		byte[] tampon = new byte[Main.taille];
		try {
			DatagramSocket ds = new DatagramSocket(port);
			while (true) {
				DatagramPacket dp = new DatagramPacket(tampon, tampon.length);
				try {
					ds.receive(dp);
					//JsonLogger.logInfo("Le serveur UDP accepte une connexion");
					String req = new String(tampon, 0, dp.getLength()); // extraction de la requete
					String rep = gp.traiterReq(req); // appel gestionprotocol pour traiter la requete
					dp.setData(rep.getBytes());
					
					String[] tabUDP = req.split(" ");
					JsonLogger.log(dp.getAddress().toString(), port, "UDP", tabUDP[0], tabUDP[1], rep);
					
					ds.send(dp);
				} catch (IOException e) {
					e.printStackTrace();
					ds.close();
				}
			}
		} catch (SocketException e) {
			logger.error("An error creating or accessing a Socket has occurred", e);
		}
	}
}
