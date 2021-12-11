package ru.dnoskov.logic.commands.personal.notes;

import ru.dnoskov.database.dao.User;
import ru.dnoskov.database.services.UsersService;
import ru.dnoskov.logic.commands.personal.AbsPersonalCommand;
import ru.dnoskov.util.response.StringResponse;

public class DelNotePersonalCommand extends AbsPersonalCommand {

	private final String keyString;
	
	public DelNotePersonalCommand(long userId, String keyString) {
		super(userId);
		this.keyString = keyString;
	}

	/**
	 * Delete note by its key
	 *  
	 * @return Russian String with response message text 
	 */
	@Override
	public StringResponse execute() {
		String answer;
		
		if (keyString == null) {
			answer = "Что за ключ-то?";
		} else {
			String key = keyString.trim();
				
			UsersService usersService = new UsersService();
			User user = usersService.getOrCreateUserByTelegramId(userId);
				
			String previousValue = usersService.deleteNote(user, key);
			
			if (previousValue == null) {
				answer = "Успешно удалено!";
			} else {
				answer = "Хотел бы я сказать, что успешно удалено, но такой записи вообще не было.";
			}
		}
		
		return new StringResponse(answer);
	}
}
