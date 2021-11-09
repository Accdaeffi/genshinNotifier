package database;

import java.util.Calendar;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import util.Util;

public class DatabaseItemsMethods {

	private static MongoCollection<Document> items = DatabaseBase.getDatabase().getItems();
	
	/**
	 * Retrieve items from the collection 
	 * that are listed in the list and that can be farmed on the specified day
	 * 
	 * @param allItems collection with all items
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
	 * @param input - tag or name of item
	 * @return item
	 */
	
	public Document getItemByNameOrTag(String input) {

		String fieldName = "name"; // поле для поиска - "tag" или "name"
		
		/* Если текст размером с тег и большими буквами - то это тег */
		if ((input.length() == 3)&&(input.toUpperCase().equals(input))) {
		fieldName = "tag";
		}
		
		return items.find(Filters.eq(fieldName, input)).first();
	}
}
