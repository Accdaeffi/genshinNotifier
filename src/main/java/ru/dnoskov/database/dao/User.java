package ru.dnoskov.database.dao;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class User {

	private long id;
	
	private List<String> items;
	
	private Map<String, String> notes;
	
	private Server server;
	
	public User (long id, List<String> items, Map<String, String> notes, String server) {
		this.id = id;
		this.items = items;
		this.notes = notes;
		this.server = Server.fromString(server);
	}
}
