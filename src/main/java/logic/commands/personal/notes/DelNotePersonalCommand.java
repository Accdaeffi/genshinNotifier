package logic.commands.personal.notes;

import org.bson.Document;

import database.DbUsersMethods;
import logic.commands.personal.AbsPersonalCommand;
import util.response.StringResponse;

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
				
			DbUsersMethods databaseUsers = new DbUsersMethods(); 
			Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
				
			if (databaseUsers.deleteNote(user, key)) {
				answer = "Успешно удалено!";
			} else {
				answer = "Хотел бы я сказать, что успешно удалено, но такой записи вообще не было.";
			}
		}
		
		return new StringResponse(answer);
	}
}
