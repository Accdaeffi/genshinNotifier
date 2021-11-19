package database.dao;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class User {

	@Getter
	@Setter
	private long id;
	
	@Getter
	@Setter
	private List<String> items;
	
	@Getter
	@Setter
	private Map<String, String> notes;
	
	@Getter
	@Setter
	private Server server;
	
	public User (long id, List<String> items, Map<String, String> notes, String server) {
		this.id = id;
		this.items = items;
		this.notes = notes;
		this.server = Server.fromString(server);
	}
}
