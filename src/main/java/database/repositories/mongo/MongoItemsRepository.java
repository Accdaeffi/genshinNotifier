package database.repositories.mongo;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import database.dao.Item;
import database.repositories.IncorrectDataSourceException;
import database.repositories.ItemsRepository;
import database.sources.DataSource;
import database.sources.MongoDataSource;
import main.Properties;
import util.Util;

public class MongoItemsRepository implements ItemsRepository {
	
	private MongoCollection<Document> items;
	
	public MongoItemsRepository () throws IncorrectDataSourceException {
		DataSource dataSource = Properties.dataSource;
		
		if (dataSource instanceof MongoDataSource) {
			items = ((MongoDataSource) dataSource).getItems();
		} else {
			throw new IncorrectDataSourceException();
		}
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
			
			item = new Item(name, tag, day, type);
		}
		
		return item;
	}

}
