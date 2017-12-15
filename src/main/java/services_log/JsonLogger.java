package services_log;

import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.log4j.Logger;

import serveur.LOGServeur;

/**
 * Classe Singleton qui permet de logger des requ�tes vers un serveur de log sur le port 3244 de la machine locale
 * 
 * @author torguet
 *
 */
public class JsonLogger {
	
	private static int port;
	private static Logger log = Logger.getLogger(JsonLogger.class);
	
	
	/**
	 * Constructeur � compl�ter
	 */
	private JsonLogger(int port) {
		this.port = port;
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
			logger = new JsonLogger(3244);
		}
		return logger;
	}
	
	/**
	 * m�thode pour logger
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
		
		String req = json.toString();
		
		/*
		 * Creer une socket comme le client
		 * et envoi sans accept
		 * 
		 * 
		 */
		
		
		//connexion serveur
		//envoi de req au serveur
		
	}
}