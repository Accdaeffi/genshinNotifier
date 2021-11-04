package logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import lombok.Getter;
import lombok.Setter;

public class PersonalFarm {

	@Getter
	@Setter
	private Long userId;
	
	@Setter
	@Getter
	private MongoDatabase database;
	
	protected PersonalFarm(Long id,  MongoDatabase database) {
		this.userId = id;
		this.database = database;
	}
	
	/**
	 * Calculate result of a command to get the goals of today's personal farm
	 * 
	 * @param dayOfWeek farm day as a day of the week - according to Calendar.DAY_OF_WEEK
	 * @return Russian String with response message text 
	 */
	protected String getPersonalFarm(int dayOfWeek) {
		StringBuilder answer = new StringBuilder();
		
		MongoCollection<Document> users = database.getCollection("users");
		Document user = users.find(Filters.eq("id", userId)).first();
		
		if (user == null || user.getList("items", String.class).isEmpty()) {
			answer.append("Я не знаю, по каким предметам тебе нужна информация\\.");
		} else {
			List<String> userItems = user.getList("items", String.class);
		
			MongoCollection<Document> items = database.getCollection("items");
			List<String> todayFarmUserItems = new LinkedList<>();
			
			getFarmableItemsByDay(items, userItems, dayOfWeek)
			.forEach(item -> todayFarmUserItems.add(item.getString("name")));
			
			if (todayFarmUserItems.isEmpty()) {
				answer.append("Сегодня тебе ничего не нужно фармить\\!");
				answer.append(System.lineSeparator());
				
				List<String> tomorrowFarmUserItems = new LinkedList<>();
				
				int nextDayOfWeek = dayOfWeek%7+1;
				getFarmableItemsByDay(items, userItems, nextDayOfWeek)
					.forEach(item -> tomorrowFarmUserItems.add(item.getString("name")));
				
				if (tomorrowFarmUserItems.isEmpty()) {
					answer.append("И завтра, кстати, тоже\\.");
				} else {
					answer.append("Зато завтра можно будет фармить ресурсы для: ");
					answer.append(String.join(", ", tomorrowFarmUserItems));
					answer.append("\\.");
				}
				
			} else {
				answer.append("Сегодня можно фармить ресурсы для: ");
				answer.append(String.join(", ", todayFarmUserItems));
				answer.append("\\.");
			}
		}
		return answer.toString();
	}
	
	/**
	 * Retrieves items from the collection 
	 * that are listed in the list and that can be farmed on the specified day
	 * 
	 * @param allItems collection with all items
	 * @param neededItems list of names of required items
	 * @param dayOfWeek day of farm
	 * @return required items, that can be farmed on the specified day 
	 */
	private FindIterable<Document> getFarmableItemsByDay(MongoCollection<Document> allItems, 
														List<String> neededItems,
														int dayOfWeek) {
		Bson filters;
		
		if (dayOfWeek != Calendar.SUNDAY) {
			filters = Filters.and(Filters.in("name", neededItems), 
								  Filters.eq("day", Util.ConvertWeekDayToFarmDay(dayOfWeek)));
		} else {
			filters = Filters.in("name", neededItems);
		}
		return allItems.find(filters);
	}
	
	/**
	 * Get list with targets of farm
	 * 
	 * @return Russian String with response message text 
	 */
	protected String list() {
		StringBuilder answer = new StringBuilder();
		
		MongoCollection<Document> users = database.getCollection("users");
		Document user = users.find(Filters.eq("id", userId)).first();
		
		if (user == null || user.getList("items", String.class).isEmpty()) {
			answer.append("Ты пока не нацелен на прокачку ни одного предмета!\\.");
		} else {
			List<String> userItems = user.getList("items", String.class);
			
			answer.append("Ты нацелен на фарм ресурсов для: ");
			answer.append(String.join(", ", userItems));
			answer.append("\\.");
		}
		
		return answer.toString();
	}
	
	/**
	 * Add new item as a target to farm
	 * 
	 * @param itemName name of item
	 * @return Russian String with response message text 
	 */
	protected String add(String itemName) {
		String answer;
		
		if (itemName == null) {
			answer = "Ты не указал, что тебе нужно\\. Названия, если что \\- с английского HHW\\.";
		} else {
			MongoCollection<Document> items = database.getCollection("items");
			if (items.countDocuments(Filters.eq("name", itemName)) == 0) {
				answer = "Ты неправильно указал, что тебе нужно\\. Названия бери с английского HHW\\.";
			} else {
				MongoCollection<Document> users = database.getCollection("users");
				
				Document user = users.find(Filters.eq("id", userId)).first();
				if (user == null) {
					user = new Document().append("id", userId)
				  		     			 .append("items", new ArrayList<String>());
					users.insertOne(user);
				}
				
				if (user.getList("items", String.class).contains(itemName)) {
					answer = "Ты уже добавил этот предмет\\!";
				} else {
					Bson update = Updates.push("items", itemName);
					users.updateOne(Filters.eq("id", userId), update);
					answer = "Успешно добавлено\\!";
				}
			}
		}
		
		return answer;
	}
	
	/**
	 * Delete item from list of targets to farm
	 * 
	 * @param itemName name of item
	 * @return Russian String with response message text 
	 */
	protected String del(String itemName) {
		String answer;
		
		if (itemName == null) {
			answer = "Ты не указал, что тебе нужно\\. Названия, если что \\- с английского HHW\\.";
		} else {
			MongoCollection<Document> items = database.getCollection("items");
			if (items.countDocuments(Filters.eq("name", itemName)) == 0) {
				answer = "Ты неправильно указал, что тебе нужно\\. Названия бери с английского HHW\\.";
			} else {
				MongoCollection<Document> users = database.getCollection("users");
				
				Document user = users.find(Filters.eq("id", userId)).first();
				if (user == null) {
					user = new Document().append("id", userId)
							  		     .append("items", new ArrayList<String>());
					users.insertOne(user);
				}
				
				if (!user.getList("items", String.class).contains(itemName)) {
					answer = "Нельзя удалить то, что уже удалено\\!";
				} else {
					Bson update = Updates.pull("items", itemName);
					users.updateOne(Filters.eq("id", userId), update);
					answer = "Успешно удалено\\!";
				}
			}
		}
		
		return answer;
	}
}
