package client;

public class Client {
	
	int id;
	String nomClient;
	String requeteClient;
	
	public Client(int i){
		this.id = i;
	}
	
	public void setNom(String n) {
		this.nomClient = n;
	}
	
	public void setRequete(String r) {
		this.requeteClient = r;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getNom() {
		return this.nomClient;
	}
	
	public String getRequete() {
		return this.requeteClient;
	}

}
