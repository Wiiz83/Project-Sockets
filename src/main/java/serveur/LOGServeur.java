package serveur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

public class LOGServeur implements Runnable {
	private int port;
	volatile boolean keepProcessing = true;
	private ServerSocket socketEcoute;
	private static Logger logger = Logger.getLogger(LOGServeur.class);

	/*** Server Constructor ****/
	public LOGServeur(int port) {
		this.port = port;
	}

	public void run() {
		try {
			socketEcoute = new ServerSocket(port);
			logger.info("***** LOG Server starting *****");
			runServeurLog();
		} catch (IOException e) {
			logger.error("An I/O exception has occurred", e);
		}
	}

	public void runServeurLog() {
		while (keepProcessing) {
			try {
				Socket socket = socketEcoute.accept();
				//logger.info("LOG Server accepts a connection");
				Runnable serveurASHandler = new Runnable() {
					public void run() {
						try {
							BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							String req = br.readLine();
							logger.info("LOG Server receives : " + req);
							
							BufferedWriter bw = new BufferedWriter(new FileWriter("../resources/logFile.txt",true)); 
							bw.write(req); 
							bw.newLine(); 
							bw.flush(); 
							bw.close();
							 
						} catch (IOException e) {
							logger.error("An I/O exception has occurred", e);
						}
					}
				};

				Thread serveurASConnection = new Thread(serveurASHandler);
				Thread.sleep(1000);
				serveurASConnection.start();

			} catch (IOException e) {
				logger.error("An I/O exception has occurred", e);
			} catch (InterruptedException e) {
				logger.error("A thread has been interrupted before or during its activity", e);
			}
		}
	}
}