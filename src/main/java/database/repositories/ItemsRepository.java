package database.repositories;

import java.util.List;

import database.dao.Item;

public interface ItemsRepository {

	public Item getItemByName(String name);
	
	public Item getItemByTag(String tag);
	
	public List<Item> getItemsByDay(int dayOfWeek);
	
	public List<Item> getAllItems();

}
