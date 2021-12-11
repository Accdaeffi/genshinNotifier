package ru.dnoskov.database.dao;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Item {

	private String name;

	private String tag;

	private int day;

	private ItemType type;
	
	public Item (String name, String tag, int day, String type) {
		this.name = name;
		this.tag = tag;
		this.day = day;
		switch (type) {
			case "character": this.type = ItemType.CHARACTER; break;
			case "weapon": this.type = ItemType.WEAPON; break;
			default: this.type = ItemType.UNKNOWN; break;
		}
	}

}
