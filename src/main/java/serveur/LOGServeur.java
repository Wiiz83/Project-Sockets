package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import program.Main;
import services_auth.GestionProtocole;
import services_log.JsonLogger;

public class LOGServeur implements Runnable {
	private int port;
	volatile boolean keepProcessing = true;
	private ServerSocket socketEcoute;
	private static Logger logger = Logger.getLogger(LOGServeur.class);
	
	/*** Server Constructor ****/
	public LOGServeur(int port) {
		this.port = port;
	}
	
	/*** Server Launcher ****/
	public void run() {
		System.out.printf("******** Server LOG Starting ******** \n");
		try {
			socketEcoute = new ServerSocket(Main.portAuthServeur);
		} catch (IOException e) {
			
		}
		
		Runnable TCPHandler = new Runnable() {
			public void run() {
				processTCP();
			}
		};
		Thread TCPConnection = new Thread(TCPHandler);
		TCPConnection.start();
	}

	public void processTCP() {
		while (keepProcessing) {
			try {
				Socket socket = socketEcoute.accept();
				Runnable clientHandler = new Runnable() {
					public void run() {
						try {
							System.out.printf("******** Server Accepting A New Client TCP ******** \n");
							BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							String req = br.readLine();
							logger.info(req);
							socket.close();
						} catch(IOException ex) {
							logger.error("BLABLA",ex);
						}
					}
				};
				Thread clientConnection = new Thread(clientHandler);
				Thread.sleep(1000); 
				clientConnection.start();
			} catch (Exception e) {
				handle(e);
			}
		}
	}
	
	private void handle(Exception e) {
		if (!(e instanceof SocketException)) {
			e.printStackTrace();
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