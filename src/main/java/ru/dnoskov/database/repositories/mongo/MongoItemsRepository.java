package ru.dnoskov.database.repositories.mongo;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import ru.dnoskov.database.dao.Item;
import ru.dnoskov.database.dao.Material;
import ru.dnoskov.database.repositories.ItemsRepository;
import ru.dnoskov.database.sources.DataSource;
import ru.dnoskov.util.NoSuchItemException;
import ru.dnoskov.util.Util;

public class MongoItemsRepository implements ItemsRepository {
	
	private MongoCollection<Document> items;
	private List<Material> materials;
	
	public MongoItemsRepository(DataSource dataSource, MongoCollection<Document> itemsCollection) {
		this.items = itemsCollection;
		this.materials = dataSource.getMaterialsRepository().getAllMaterials();
	}

	@Override
	public Item getItemByName(String name) {
		Document itemFromDatabase = items.find(Filters.eq("name", name)).first();
		return itemFromDocument(itemFromDatabase);
	}

	@Override
	public Item getItemByTag(String tag) {
		Document itemFromDatabase = items.find(Filters.eq("tag", tag)).first();
		return itemFromDocument(itemFromDatabase);
	}
	
	@Override
	public List<Item> getItemsByDay(int dayOfWeek) {
		int dayOfFarm = Util.ConvertWeekDayToFarmDay(dayOfWeek);
		LinkedList<Item> allItems = new LinkedList<>(); 
		
		items.find(Filters.eq("day", dayOfFarm))
				.forEach(itemFromDatabase -> allItems.add(itemFromDocument(itemFromDatabase)));
		
		return allItems;
	}
	
	@Override
	public List<Item> getAllItems() {
		LinkedList<Item> allItems = new LinkedList<>(); 
		
		items.find()
				.forEach(itemFromDatabase -> allItems.add(itemFromDocument(itemFromDatabase)));
		
		return allItems;
	}
	
	private Item itemFromDocument(Document itemFromDatabase) {
		Item item = null;
		
		if (itemFromDatabase != null) {	
			String name = itemFromDatabase.getString("name");
			String tag = itemFromDatabase.getString("tag");
			int day = itemFromDatabase.getInteger("day");
			String type = itemFromDatabase.getString("type");
			
			String materialTag = itemFromDatabase.getString("materialTag");
			Material material = materials
					.stream()
					.filter(m -> m.getTag().equals(materialTag))
					.findFirst()
					.orElseThrow(() -> new NoSuchItemException());
			
			item = new Item(name, tag, day, material, type);
		}
		
		return item;
	}

}
