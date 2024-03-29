package ru.dnoskov.database.services;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ru.dnoskov.database.dao.Item;
import ru.dnoskov.database.dao.Material;
import ru.dnoskov.database.repositories.ItemsRepository;
import ru.dnoskov.database.repositories.MaterialsRepository;
import ru.dnoskov.main.Properties;
import ru.dnoskov.util.NoSuchItemException;

public class ItemsService {
	
	ItemsRepository itemsRepository;
	MaterialsRepository materialsRepository;
	
	public ItemsService() {
		itemsRepository = Properties.getDataSource().getItemsRepository();
		materialsRepository = Properties.getDataSource().getMaterialsRepository();
	}
	
	/**
	 * Retrieve items from the collection 
	 * that are listed in the list and that can be farmed on the specified day
	 * 
	 * @param neededItems list of names of required items
	 * @param dayOfWeek day of farm
	 * @return list of required items, that can be farmed on the specified day 
	 */
	public List<Item> getFarmableItemsByDay(List<String> neededItems,
													int dayOfWeek) {
		List<Item> farmableItems = new LinkedList<>();
		
		if (dayOfWeek != Calendar.SUNDAY) {
			List<Material> farmableMaterials = materialsRepository.getMaterialsByDay(dayOfWeek);
			List<String> farmableMaterialsTags = farmableMaterials.stream().map(Material::getTag).collect(Collectors.toList());
			
			farmableItems = itemsRepository.getItemsByMaterialTags(farmableMaterialsTags);
		} else {
			farmableItems = itemsRepository.getAllItems();
		}
		
		List<Item> result = farmableItems
				.stream()
				.filter(item -> neededItems.contains(item.getName()))
				.collect(Collectors.toList());
		
		return result;
	}
	
	/**
	 * Retrieve item from database by it name or tag 
	 * 
	 * @param itemNameOrTag tag or name of item
	 * @return item
	 * @throws NoSuchItemException if unknown name or tag
	 */

	public Item getItemByNameOrTag(String itemNameOrTag) throws NoSuchItemException {

		Item result;
		
		/* Если текст размером с тег и большими буквами - то это тег */
		if ((itemNameOrTag.length() == 3)&&(itemNameOrTag.toUpperCase().equals(itemNameOrTag))) {
			result = itemsRepository.getItemByTag(itemNameOrTag);
		} else {
			result = itemsRepository.getItemByName(itemNameOrTag);
		}
		
		if (result == null) {
			throw new NoSuchItemException();
		}
				
		return result;
	}
}
