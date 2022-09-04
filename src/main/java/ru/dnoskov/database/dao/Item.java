package ru.dnoskov.database.dao;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Item {

	private String name;
	private String tag;
	private int day;
	private Material material;
	private ItemType type;
	
	public Item (String name, String tag, int day, Material material, String type) {
		this.name = name;
		this.tag = tag;
		this.day = day;
		this.material = material;
		switch (type) {
			case "character": this.type = ItemType.CHARACTER; break;
			case "weapon": this.type = ItemType.WEAPON; break;
			default: this.type = ItemType.UNKNOWN; break;
		}
	}
	
	public enum ItemType {
		UNKNOWN,
		CHARACTER,
		WEAPON
	}

}
