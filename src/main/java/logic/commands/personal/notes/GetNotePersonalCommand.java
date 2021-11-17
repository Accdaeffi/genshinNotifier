package logic.commands.personal.notes;

import database.dao.User;
import database.services.UsersService;
import logic.commands.personal.AbsPersonalCommand;
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
	public StringResponse execute() {
		String answer;
		
		if (keyString == null) {
			answer = "Что за ключ-то?";
		} else {
			String key = keyString.trim();
				
			UsersService usersService = new UsersService();
			User user = usersService.getOrCreateUserByTelegramId(userId);			
				
			String value = user.getNotes().get(key);
			
			if (value != null) {
				answer = String.format("Твоя запись: %s", value);
			} else {
				answer = "Не нашёл такой записи!";
			}
		}
		
		return new StringResponse(answer);
	}

}
