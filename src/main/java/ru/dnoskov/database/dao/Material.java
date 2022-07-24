package ru.dnoskov.database.dao;

import lombok.Data;

@Data
public class Material {

	int sortingPriority;
	int day;
	String name;
	String tag;
	MaterialType type;
	
	public Material(int sortingPriority, int day, String name, String tag, String materialTypeString) {
		this.sortingPriority = sortingPriority;
		this.day = day;
		this.name = name;
		this.tag = tag;
		
		switch (materialTypeString) {
			case "talent": this.type = MaterialType.TALENT_MATERIAL; break;
			case "weapon": this.type = MaterialType.WEAPON_MATERIAL; break;
			default: this.type = MaterialType.UNKNOWN; break;
	}
		
		
	}
	
	public static enum MaterialType {
		TALENT_MATERIAL,
		WEAPON_MATERIAL,
		UNKNOWN
	}
}
