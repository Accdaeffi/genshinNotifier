package ru.dnoskov.database.services;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ru.dnoskov.database.dao.Item;
import ru.dnoskov.database.dao.Material;
import ru.dnoskov.database.repositories.MaterialsRepository;
import ru.dnoskov.main.Properties;

public class MaterialsService {

	MaterialsRepository repository;
	
	public MaterialsService() {
		repository = Properties.getDataSource().getMaterialsRepository();
	}
	
	/**
	 * Retrieve materials from the collection that can be farmed on the specified day
	 * 
	 * @param dayOfWeek day of farm
	 * @return list of materials, that can be farmed on the specified day 
	 */
	public List<Material> getMaterialsByDay(int dayOfWeek) {
		List<Material> farmableMaterials = new LinkedList<>();
		
		if (dayOfWeek != Calendar.SUNDAY) {
			farmableMaterials = repository.getMaterialsByDay(dayOfWeek);
		} else {
			farmableMaterials = repository.getAllMaterials();
		}
		
		return farmableMaterials;
	}
	
}
