package ru.dnoskov.database.repositories;

import java.util.List;

import ru.dnoskov.database.dao.Item;

public interface ItemsRepository {

	public Item getItemByName(String name);
	
	public Item getItemByTag(String tag);
	
	public List<Item> getItemsByDay(int dayOfWeek);
	
	public List<Item> getAllItems();
	
	public List<Item> getItemsByMaterialTags(List<String> materialTags);

}
