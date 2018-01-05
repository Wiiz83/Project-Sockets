package services_auth;

public class GestionProtocole {
	private ListeAuth auth;
	
	public GestionProtocole(ListeAuth la) {
		this.auth = la;
	}

	synchronized public String traiterReq(String req) {
		
		String[] reqElem = req.split(" ");
		String login = null;
		String passwd = null;
		
		if(reqElem.length != 3) {
			System.out.println("Erreur, requête incorrecte");
		} else {
			login = reqElem[1];
			passwd = reqElem[2];
		}
		
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
					return "ERROR";
				}
			case "DEL":
				if (auth.supprimer(login, passwd)) {
					return "DONE";
				} else {
					return "ERROR";
				}
			case "MOD":
				if (auth.mettreAJour(login, passwd)) {
					return "DONE";
				} else {
					return "ERROR";
				}
			default:
				return "ERROR";
		}
	}
}