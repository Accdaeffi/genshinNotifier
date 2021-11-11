package logic.commands.personal.notes;

import org.bson.Document;

import database.DbUsersMethods;
import logic.commands.personal.AbsPersonalCommand;
import util.Response;

public class AddNotePersonalCommand extends AbsPersonalCommand {

	private final String keyValueString;
	
	public AddNotePersonalCommand(long userId, String keyValueString) {
		super(userId);
		this.keyValueString = keyValueString;
	}

	/**
	 * Add new note or replace old own
	 * 
	 * @return Russian String with response message text 
	 */
	@Override
	public Response<String> execute() {
		String answer;
		
		if (keyValueString == null) {
			answer = "А какая заметка-то?";
		} else {
			String arr[] = keyValueString.trim().split(":", 2);
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
		
		return new Response<>(answer);
	}
	
}
