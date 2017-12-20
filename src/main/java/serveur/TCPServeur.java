package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
import services_auth.GestionProtocole;
import services_log.JsonLogger;

public class TCPServeur implements Runnable {
	private GestionProtocole gp;
	private int port;
	private ServerSocket socketEcoute;
	volatile boolean keepProcessing = true;
	private static Logger logger = Logger.getLogger(TCPServeur.class);
	

	/*** Server Constructor ****/
	public TCPServeur(GestionProtocole gp, int port) {
		this.gp = gp;
		this.port = port;
	}
	
	public void run() {
		try {
			socketEcoute = new ServerSocket(port);
			logger.info("***** TCP Server starting *****");
			processTCP();
		} catch (IOException e) {
			logger.error("An I/O exception has occurred", e);
		}
	}

	public void processTCP() {
		while (keepProcessing) {
			try {
				Socket socket = socketEcoute.accept();
				//logger.info("TCP Server accepts a connection");
				Runnable clientHandler = new Runnable() {
					public void run() {
						try {
							//logger.info("TCP Server getting message");
							BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							PrintStream ps = new PrintStream(socket.getOutputStream());
							String req = br.readLine();
							//logger.info("TCP Server got message : " + req);
							//Thread.sleep(1000);
							String rep = gp.traiterReq(req);
							//logger.info("TCP Server sending reply : " + rep);
							String[] tab = req.split(" ");
							JsonLogger.log(socket.getInetAddress().toString(), port, "TCP", tab[0], tab[1], rep);
							ps.println(rep);
							//logger.info("TCP Server reply sent");
							closeIgnoringException(socket);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				Thread serveurASConnection = new Thread(clientHandler);
				Thread.sleep(1000);
				serveurASConnection.start();

			} catch (IOException e) {
				logger.error("An I/O exception has occurred", e);
			} catch (InterruptedException e) {
				logger.error("A thread has been interrupted before or during its activity", e);
			}
		}
	}


	public void stopProcessing() {
		keepProcessing = false;
		closeIgnoringException(socketEcoute);
	}

	private void closeIgnoringException(Socket socket) {
		if (socket != null)
			try {
				socket.close();
			} catch (IOException ignore) {
			}
	}

	private void closeIgnoringException(ServerSocket serverSocket) {
		if (serverSocket != null)
			try {
				serverSocket.close();
			} catch (IOException ignore) {
				
			}
	}

	
}