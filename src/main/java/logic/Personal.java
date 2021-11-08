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

public class Personal {

	@Getter
	@Setter
	private Long userId;
	
	@Setter
	@Getter
	private MongoDatabase database;
	
	protected Personal(Long id,  MongoDatabase database) {
		this.userId = id;
		this.database = database;
	}
	
	/* Personal farm */
	
	/**
	 * Calculate result of a command to get the goals of today's personal farm
	 * 
	 * @param dayOfWeek farm day as a day of the week - according to Calendar.DAY_OF_WEEK
	 * @return Russian String with response message text 
	 */
	protected String getPersonalFarm(int dayOfWeek) {
		StringBuilder answer = new StringBuilder();
		
		MongoCollection<Document> users = database.getCollection("users");
		Document user = getOrCreateUserByTelegramId(users, userId);
		
		if (user.getList("items", String.class).isEmpty()) {
			answer.append("Я не знаю, по каким предметам тебе нужна информация.");
		} else {
			List<String> userItems = user.getList("items", String.class);
		
			MongoCollection<Document> items = database.getCollection("items");
			List<String> todayFarmUserItems = new LinkedList<>();
			
			getFarmableItemsByDay(items, userItems, dayOfWeek)
			.forEach(item -> todayFarmUserItems.add(item.getString("name")));
			
			if (todayFarmUserItems.isEmpty()) {
				answer.append("Сегодня тебе ничего не нужно фармить!");
				answer.append(System.lineSeparator());
				
				List<String> tomorrowFarmUserItems = new LinkedList<>();
				
				int nextDayOfWeek = dayOfWeek%7+1;
				getFarmableItemsByDay(items, userItems, nextDayOfWeek)
					.forEach(item -> tomorrowFarmUserItems.add(item.getString("name")));
				
				if (tomorrowFarmUserItems.isEmpty()) {
					answer.append("И завтра, кстати, тоже.");
				} else {
					answer.append("Зато завтра можно будет фармить ресурсы для: ");
					answer.append(String.join(", ", tomorrowFarmUserItems));
					answer.append(".");
				}
				
			} else {
				answer.append("Сегодня можно фармить ресурсы для: ");
				answer.append(String.join(", ", todayFarmUserItems));
				answer.append(".");
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
	
	/* Work with list of personal farm's targets */
	
	/**
	 * Get list with targets of farm
	 * 
	 * @return Russian String with response message text 
	 */
	protected String list() {
		StringBuilder answer = new StringBuilder();
		
		MongoCollection<Document> users = database.getCollection("users");
		Document user = getOrCreateUserByTelegramId(users, userId);
		
		if (user.getList("items", String.class).isEmpty()) {
			answer.append("Ты пока не нацелен на прокачку ни одного предмета!.");
		} else {
			List<String> userItems = user.getList("items", String.class);
			
			answer.append("Ты нацелен на фарм ресурсов для: ");
			answer.append(String.join(", ", userItems));
			answer.append(".");
		}
		
		return answer.toString();
	}
	
	/**
	 * Add new item as a target to farm
	 * 
	 * @param userInput - name or tag of item
	 * @return Russian String with response message text 
	 */
	protected String add(String userInput) {
		String answer;
		
		if (userInput == null) {
			answer = "Ты не указал, что тебе нужно. Названия, если что - с английского HHW или из /help.";
		} else {
			Document item = getItemByNameOrTag(database.getCollection("items"), userInput);
			
			if (item == null) {
				answer = "Ты неправильно указал, что тебе нужно. Названия бери с английского HHW или из /help.";
			} else {
				String itemName = item.getString("name");
				MongoCollection<Document> users = database.getCollection("users");
				Document user = getOrCreateUserByTelegramId(users, userId);
				
				if (user.getList("items", String.class).contains(itemName)) {
					answer = "Ты уже добавил этот предмет!";
				} else {
					Bson update = Updates.push("items", itemName);
					users.updateOne(Filters.eq("id", userId), update);
					answer = "Успешно добавлено!";
				}
			}
		}
		
		return answer;
	}
	
	/**
	 * Delete item from list of targets to farm
	 * 
	 * @param userInput - name or tag of item
	 * @return Russian String with response message text 
	 */
	protected String del(String userInput) {
		String answer;
		
		if (userInput == null) {
			answer = "Ты не указал, что тебе нужно. Названия, если что - с английского HHW или из /help.";
		} else {
			Document item = getItemByNameOrTag(database.getCollection("items"), userInput);
			
			if (item == null) {
				answer = "Ты неправильно указал, что тебе нужно. Названия бери с английского HHW или из /help.";
			} else {
				String itemName = item.getString("name");
				MongoCollection<Document> users = database.getCollection("users");
				Document user = getOrCreateUserByTelegramId(users, userId);
				
				if (!user.getList("items", String.class).contains(itemName)) {
					answer = "Нельзя удалить то, что уже удалено!";
				} else {
					Bson update = Updates.pull("items", itemName);
					users.updateOne(Filters.eq("id", userId), update);
					answer = "Успешно удалено!";
				}
			}
		}
		
		return answer;
	}
	
	private Document getItemByNameOrTag(MongoCollection<Document> items, 
										String input) {
		
		String fieldName = "name"; // поле для поиска - "tag" или "name"
		
		/* Если текст размером с тег и большими буквами - то это тег */
		if ((input.length() == 3)&&(input.toUpperCase().equals(input))) {
			fieldName = "tag";
		}
		
		return items.find(Filters.eq(fieldName, input)).first();
	}
	
	/* Notes */
	
	/**
	 * Get note by its key
	 * 
	 * @param key of the required note
	 * @return Russian String with response message text 
	 */
	protected String getNote(String key) {
		String answer;
		
		if (key == null) {
			answer = "Что за ключ-то?";
		} else {
			key = key.trim();
				
			MongoCollection<Document> users = database.getCollection("users");
			Document user = getOrCreateUserByTelegramId(users, userId);
				
			Document userNotes = (Document) user.get("notes");
				
			String value = userNotes.getString(key);
			
			if (value != null) {
				answer = String.format("Твоя запись: %s", value);
			} else {
				answer = "Не нашёл такой записи!";
			}
		}
		
		return answer;
	}
	
	/**
	 * Get all notes from user
	 * 
	 * @return Russian String with response message text 
	 */
	protected String getAllNotes() {
		StringBuilder answer = new StringBuilder();
		
		MongoCollection<Document> users = database.getCollection("users");		
		Document user = getOrCreateUserByTelegramId(users, userId);
				
		Document userNotes = (Document) user.get("notes");
				
		if (userNotes.size() == 0) {
			answer.append("У тебя пока нет никаких заметок!");
		} else {
			answer.append(String.format("Твои заметки:%s%s", System.lineSeparator(), 
															 System.lineSeparator()));
			
			userNotes.forEach((key,value) -> answer.append(
					String.format("%s:%s%s", key, 
											 value, 
											 System.lineSeparator())));
		}
		
		return answer.toString();
	}
	
	/**
	 * Add new note or replace old own
	 * 
	 * @param note to add in format key:value
	 * @return Russian String with response message text 
	 */
	protected String addNote(String note) {
		String answer;
		
		if (note == null) {
			answer = "А какая заметка-то?";
		} else {
			String arr[] = note.trim().split(":", 2);
			if (arr.length < 2) {
				answer = "Неправильный формат записи! Используй формат key:value!";
			} else {
				String key = arr[0].trim();
				String value = arr[1].trim();
				
				MongoCollection<Document> users = database.getCollection("users");
				Document user = getOrCreateUserByTelegramId(users, userId);
				
				Document userNotes = (Document) user.get("notes");
				
				String result = userNotes.getString(key);
				
				if (result != null) {
					userNotes.put(key, value);
					users.replaceOne(Filters.eq("id", userId), user);
					answer = "Успешно заменено!";
				} else {
					userNotes.put(key, value);
					users.replaceOne(Filters.eq("id", userId), user);
					answer = "Успешно добавлено!";
				}
			}
		}
		
		return answer;
	}
	
	/**
	 * Delete note by its key
	 * 
	 * @param key 
	 * @return Russian String with response message text 
	 */
	protected String delNote(String key) {
		String answer;
		
		if (key == null) {
			answer = "Что за ключ-то?";
		} else {
			key = key.trim();
				
			MongoCollection<Document> users = database.getCollection("users");
			Document user = getOrCreateUserByTelegramId(users, userId);
				
			Document userNotes = (Document) user.get("notes");
				
			String value = userNotes.getString(key);
			
			if (value != null) {
				userNotes.remove(key);
				users.replaceOne(Filters.eq("id", userId), user);
				answer = "Успешно удалено!";
			} else {
				answer = "Хотел бы я сказать, что успешно удалено, но такой записи вообще не было.";
			}
		}
		
		return answer;
	}
	
	private Document getOrCreateUserByTelegramId(MongoCollection<Document> users, 
												 Long userId) {
		Document user = users.find(Filters.eq("id", userId)).first();
		if (user == null) {
			user = createUser(userId);
			users.insertOne(user);
		}
		
		return user;
	}
	
	private Document createUser(Long userId) {
		return new Document().append("id", userId)
    			 			 .append("items", new ArrayList<String>())
    			 			 .append("notes", new Document());
	}
}
