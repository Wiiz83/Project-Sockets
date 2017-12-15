package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import program.Main;
import services_auth.GestionProtocole;
import services_log.JsonLogger;
import services_message.MessageServices;

public class TCPServeur implements Runnable {
	private GestionProtocole gp;
	private int port;
	private JsonLogger jl;
	private ServerSocket socketEcoute;
	volatile boolean keepProcessing = true;

	/*** Server Constructor ****/
	public TCPServeur(GestionProtocole gp, int port, JsonLogger jl) {
		this.gp = gp;
		this.port = port;
		this.jl = jl;
	}

	/*** Server Launcher ****/
	public void run() {
		System.out.printf("******** Server TCP Starting ******** \n");
		try {
			socketEcoute = new ServerSocket(Main.portAuthServeur);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
							PrintStream ps = new PrintStream(socket.getOutputStream());
							String req = br.readLine();
							System.out.println("Req  test : " + req);
							String rep = gp.traiterReq(req); // appel de gestionprotocol pour traiter la requete
							ps.println(rep); // r�ponse de la requ�te du client
							socket.close();
						} catch(Exception ex) {
							// erreur de connexion
							System.err.println("Une erreur est survenue : "+ex);
							ex.printStackTrace();
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