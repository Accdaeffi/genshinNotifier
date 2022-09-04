package ru.dnoskov.database.repositories;

import java.util.List;

import ru.dnoskov.database.dao.Material;

public interface MaterialsRepository {

	public Material getMaterialByName(String name);
	
	public Material getMaterialByTag(String tag);
	
	public List<Material> getMaterialsByDay(int dayOfWeek);
	
	public List<Material> getAllMaterials();

}
