package ru.dnoskov.logic.commands.personal.notes;

import ru.dnoskov.database.dao.User;
import ru.dnoskov.database.services.UsersService;
import ru.dnoskov.logic.commands.personal.AbsPersonalCommand;
import ru.dnoskov.util.response.StringResponse;

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
	public StringResponse execute() {
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
				
				UsersService usersService = new UsersService();
				User user = usersService.getOrCreateUserByTelegramId(userId);
				
				String previousValue = usersService.addOrReplaceNote(user, key, value);
				
				if (previousValue == null) {
					answer = "Успешно добавлено!";
				} else {
					answer = "Успешно заменено!";
				}
			}
		}
		
		return new StringResponse(answer);
	}
	
}
