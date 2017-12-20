package serveur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.log4j.Logger;

import program.Main;
import services_auth.GestionProtocole;
import services_log.JsonLogger;

public class UDPServeur implements Runnable {
	private GestionProtocole gp;
	private int port;
	volatile boolean keepProcessing = true;
	private static Logger logger = Logger.getLogger(UDPServeur.class);

	/*** Server Constructor ****/
	public UDPServeur(GestionProtocole gp, int port) {
		this.gp = gp;
		this.port = port;
	}
	
	public void run() {
		logger.info("***** UDP Server Starting *****");
		try {
			DatagramSocket serverSocket = new DatagramSocket(this.port);
			process(serverSocket);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	void process(DatagramSocket serverSocket ) {
		if (serverSocket == null)
			return;

        byte[] receiveData = new byte[Main.taille];
        byte[] sendData = new byte[Main.taille];
        while(keepProcessing)
           {
              
              try {
            	 DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				 serverSocket.receive(receivePacket);
				// logger.info("UDP Server got client");
	  //           String sentence = new String( receivePacket.getData());
	             InetAddress IPAddress = receivePacket.getAddress();
	             
	            // int port = receivePacket.getPort();    // port du client / change pour chaque client
	             
	             String req = new String(receiveData, 0, receivePacket.getLength()); 
	             String rep = gp.traiterReq(req);
	             receivePacket.setData(rep.getBytes());
				 String[] tabUDP = req.split(" ");
				 JsonLogger.log(receivePacket.getAddress().toString(), port, "UDP", tabUDP[0], tabUDP[1], rep);
	             
	             String capitalizedSentence = rep;
	             sendData = capitalizedSentence.getBytes();
	             DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	             serverSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
 
           }
		
	}

	
	
	
}
