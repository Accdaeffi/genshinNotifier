package database.dao;

import lombok.Getter;
import lombok.Setter;

public class Item {

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String tag;

	@Getter
	@Setter
	private int day;

	@Getter
	@Setter
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
