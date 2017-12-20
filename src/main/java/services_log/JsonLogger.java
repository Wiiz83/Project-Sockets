package services_log;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import program.Main;

public class JsonLogger {
	
	private JsonLogger() {

	}
	
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
	
	private static JsonLogger logger = null;
	
	public static JsonLogger getLogger() {
		if (logger == null) {
			logger = new JsonLogger();
		}
		return logger;
	}

	public static void log(String host, int port, String proto, String type, String login, String result) {
		JsonLogger logger = getLogger();
		JsonObject json = logger.reqToJson(host, port, proto, type, login, result);
		
		Socket socket;
		try {
			socket = new Socket(Main.ipServeur, Main.portServeurLog);
			PrintStream ps = new PrintStream(socket.getOutputStream());
			ps.println(json);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}