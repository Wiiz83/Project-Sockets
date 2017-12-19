package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
		logger.info("***** LOG Server Starting *****");
		while (keepProcessing) {
			try {
				socketEcoute = new ServerSocket(port);
				Socket socket = socketEcoute.accept();
				logger.info("LOG Server got client");
				process(socket);
			} catch (Exception e) {
				handle(e);
			}
		}
	}
	
	void process(Socket socket) {
		if (socket == null)
			return;

		Runnable clientHandler = new Runnable() {
			public void run() {
				try {
					logger.info("LOG Server getting message");
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String req = br.readLine();
					logger.info("LOG Server got message : " + req);
					Thread.sleep(1000);
					closeIgnoringException(socket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Thread clientConnection = new Thread(clientHandler);
		clientConnection.start();
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

	
	
	/*** Server Launcher 
	public void run() {
		logger.info("***** LOG Server starting *****");
		try {
			socketEcoute = new ServerSocket(this.port);
			while (keepProcessing) {
				Socket socket = socketEcoute.accept();
				Runnable clientHandler = new Runnable() {
					public void run() {
						try {
							logger.info("LOG Server accepts a connection");
							BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							String req = br.readLine();
							logger.info(req);
							socket.close();
						} catch(IOException e) {
							logger.error("An I/O exception has occurred", e);
						}
					}
				};
				Thread clientConnection = new Thread(clientHandler);
				Thread.sleep(1000);
				clientConnection.start();
			}
		} catch (IOException e) {
			logger.error("An I/O exception has occurred", e);
		} catch (InterruptedException e) {
			logger.error("A thread has been interrupted before or during its activity", e);
		} 
	}
****/
	
}