package services_log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.apache.log4j.Logger;

import program.Main;

/**
 * Classe Singleton qui permet de logger des requ�tes vers un serveur de log sur le port 3244 de la machine locale
 * 
 * @author torguet
 *
 */
public class JsonLogger {
	
	private static Logger log = Logger.getLogger(JsonLogger.class);
	
	
	private JsonLogger() {

	}
	
	/**
	 * Transforme une requ�te en Json
	 * 
	 * @param host machine client
	 * @param port port sur la machine client
	 * @param proto protocole de transport utilis�
	 * @param type type de la requ�te
	 * @param login login utilis�
	 * @param result r�sultat de l'op�ration
	 * @return un objet Json correspondant � la requ�te
	 */
	private JsonObject reqToJson(String host, int port, String proto, String type, String login, String result) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("host", host)
		   	   .add("port", port)
		   	   .add("proto", proto)
			   .add("type", type)
			   .add("login", login)
			   .add("result", result)
			   .add("date", new Date().toString());

		return builder.build();
	}
	
	
	/**
	 *  singleton
	 */
	private static JsonLogger logger = null;
	
	/**
	 * r�cup�ration du logger qui est cr�� si n�cessaire
	 * 
	 * @return le logger
	 */
	public static JsonLogger getLogger() {
		if (logger == null) {
			logger = new JsonLogger();
		}
		return logger;
	}
	
	/**
	 * méthode pour logger
	 * Creer une socket comme le client, y insérer la requete, connexion au serveur et envoi sans accept
	 * 
	 * @param host machine client
	 * @param port port sur la machine client
	 * @param proto protocole de transport utilis�
	 * @param type type de la requ�te
	 * @param login login utilis�
	 * @param result r�sultat de l'op�ration
	 */
	public static void log(String host, int port, String proto, String type, String login, String result) {
		JsonLogger logger = getLogger();
		JsonObject json = logger.reqToJson(host, port, proto, type, login, result);
		Socket socket;
		PrintWriter emission;
		try {
			socket = new Socket(InetAddress.getByName(Main.ipServeur), Main.portServeurLog);
			emission = new PrintWriter(socket.getOutputStream());
			emission.println(json);
			emission.flush();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	/*
	public static void logInfo(String description) {
		Socket socket;
		PrintWriter emission;
		try {
			socket = new Socket(InetAddress.getByName(Main.ipServeur), port);
			emission = new PrintWriter(socket.getOutputStream());
			emission.println(description);
			emission.flush();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	*/
	
}