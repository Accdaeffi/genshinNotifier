package database;

import java.util.Calendar;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import util.NoSuchItemException;
import util.Util;

public class DbItemsMethods {

	private MongoCollection<Document> items = DbBase.getDatabase().getItems();
	
	/**
	 * Retrieve items from the collection 
	 * that are listed in the list and that can be farmed on the specified day
	 * 
	 * @param neededItems list of names of required items
	 * @param dayOfWeek day of farm
	 * @return required items, that can be farmed on the specified day 
	 */
	public FindIterable<Document> getFarmableItemsByDay(List<String> neededItems,
														int dayOfWeek) {
		Bson filters;
		
		if (dayOfWeek != Calendar.SUNDAY) {
			filters = Filters.and(Filters.in("name", neededItems), 
								  Filters.eq("day", Util.ConvertWeekDayToFarmDay(dayOfWeek)));
		} else {
			filters = Filters.in("name", neededItems);
		}
		return items.find(filters);
	}
	
	/**
	 * Retrieve item from database by it name or tag 
	 * 
	 * @param itemNameOrTag tag or name of item
	 * @return item
	 * @throws NoSuchItemException if unknown name or tag
	 */
	
	public Document getItemByNameOrTag(String itemNameOrTag) throws NoSuchItemException {

		String fieldName = "name"; // поле для поиска - "tag" или "name"
		
		/* Если текст размером с тег и большими буквами - то это тег */
		if ((itemNameOrTag.length() == 3)&&(itemNameOrTag.toUpperCase().equals(itemNameOrTag))) {
			fieldName = "tag";
		}
		
		Document item = items.find(Filters.eq(fieldName, itemNameOrTag)).first();
		
		if (item == null) {
			throw new NoSuchItemException();
		}
				
		return item;
	}
}
