package ru.dnoskov.util.collage.rows;

import ru.dnoskov.database.dao.Material;

public class Util {

	private static final int MAX_CHARACTER_IN_ROW = 10;
	private static final int MAX_WEAPON_IN_ROW = 15;
	
	static int getMaximumInTheRow(Material.MaterialType type) {
		int result = 0;
		
		switch (type) {
			case TALENT_MATERIAL:
				result = MAX_CHARACTER_IN_ROW;
				break;
			case WEAPON_MATERIAL:
				result = MAX_WEAPON_IN_ROW;
				break;
		}
		
		return result;
	}
}
