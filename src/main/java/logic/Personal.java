package logic;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import database.DbItemsMethods;
import database.DbUsersMethods;
import lombok.Getter;
import lombok.Setter;
import util.NoSuchItemException;

public class Personal {

	@Getter
	@Setter
	private Long userId;
	
	protected Personal(Long id) {
		this.userId = id;
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
		
		DbUsersMethods databaseUsers = new DbUsersMethods(); 
		Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
		
		if (user.getList("items", String.class).isEmpty()) {
			answer.append("Я не знаю, по каким предметам тебе нужна информация.");
		} else {
			List<String> userItems = user.getList("items", String.class);
			List<String> todayFarmUserItems = new LinkedList<>();
			
			DbItemsMethods databaseItems = new DbItemsMethods();
			databaseItems.getFarmableItemsByDay(userItems, dayOfWeek)
				         .forEach(item -> todayFarmUserItems.add(item.getString("name")));
			
			if (todayFarmUserItems.isEmpty()) {
				answer.append("Сегодня тебе ничего не нужно фармить!");
				answer.append(System.lineSeparator());
				
				List<String> tomorrowFarmUserItems = new LinkedList<>();
				
				/* get next day of week */
				int nextDayOfWeek = dayOfWeek%7+1;
				
				databaseItems.getFarmableItemsByDay(userItems, nextDayOfWeek)
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
	
	/* Work with list of personal farm's targets */
	
	/**
	 * Get list with targets of farm
	 * 
	 * @return Russian String with response message text 
	 */
	protected String list() {
		StringBuilder answer = new StringBuilder();
		
		DbUsersMethods databaseUsers = new DbUsersMethods(); 
		Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
		
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
			try {
				DbUsersMethods databaseUsers = new DbUsersMethods(); 
				Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
					
				if (databaseUsers.addItem(user, userInput)) {
					answer = "Успешно добавлено!";
				} else {
					answer = "Ты уже добавил этот предмет!";
				}
			}
			catch (NoSuchItemException e) {
				answer = "Ты неправильно указал, что тебе нужно. Названия бери с английского HHW или из /help.";
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
			try {
				DbUsersMethods databaseUsers = new DbUsersMethods(); 
				Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
					
				if (databaseUsers.delItem(user, userInput)) {
					answer = "Успешно удалено!";
				} else {
					answer = "Нельзя удалить то, чего нет!";
				}
			}
			catch (NoSuchItemException e) {
				answer = "Ты неправильно указал, что тебе нужно. Названия бери с английского HHW или из /help.";
			}
		}
		
		return answer;
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
				
			DbUsersMethods databaseUsers = new DbUsersMethods(); 
			Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
				
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
		
		DbUsersMethods databaseUsers = new DbUsersMethods(); 
		Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
				
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
				
				DbUsersMethods databaseUsers = new DbUsersMethods(); 
				Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
				
				String previousValue = databaseUsers.addOrReplaceNote(user, key, value);
				
				if (previousValue == null) {
					answer = "Успешно добавлено!";
				} else {
					answer = "Успешно заменено!";
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
				
			DbUsersMethods databaseUsers = new DbUsersMethods(); 
			Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
				
			if (databaseUsers.deleteNote(user, key)) {
				answer = "Успешно удалено!";
			} else {
				answer = "Хотел бы я сказать, что успешно удалено, но такой записи вообще не было.";
			}
		}
		
		return answer;
	}
}
