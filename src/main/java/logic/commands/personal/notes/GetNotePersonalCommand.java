package logic.commands.personal.notes;

import org.bson.Document;

import database.DbUsersMethods;
import logic.commands.personal.AbsPersonalCommand;
import util.response.Response;
import util.response.StringResponse;

public class GetNotePersonalCommand extends AbsPersonalCommand {

	private final String keyString;
	
	public GetNotePersonalCommand(long userId, String keyString) {
		super(userId);
		this.keyString = keyString;
	}
	
	/**
	 * Get note by its key
	 * 
	 * @return Russian String with response message text 
	 */
	@Override
	public Response<String> execute() {
		String answer;
		
		if (keyString == null) {
			answer = "Что за ключ-то?";
		} else {
			String key = keyString.trim();
				
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
		
		return new StringResponse(answer);
	}

}
