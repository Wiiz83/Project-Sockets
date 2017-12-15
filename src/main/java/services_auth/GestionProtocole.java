package services_auth;

public class GestionProtocole {
	private ListeAuth auth;
	
	public GestionProtocole(ListeAuth la) {
		this.auth = la;
	}

	synchronized public String traiterReq(String req) {
		String[] reqElem = req.split(" ");
		String login = reqElem[1];
		String passwd = reqElem[2];
		
		switch (reqElem[0]) {
		case "CHK":
			if (auth.tester(login, passwd)) {
				return "GOOD";
			} else {
				return "BAD";
			}
		case "ADD":
			if (auth.creer(login, passwd)) {
				return "DONE";
			} else {
				return "ERROR login/pwd existants";
			}
		case "DEL":
			if (auth.supprimer(login, passwd)) {
				return "GOOD";
			} else {
				return "BAD";
			}
		case "MOD":
			if (auth.mettreAJour(login, passwd)) {
				return "GOOD";
			} else {
				return "BAD";
			}
		default:
			return "Error in request";
		}
	}
}