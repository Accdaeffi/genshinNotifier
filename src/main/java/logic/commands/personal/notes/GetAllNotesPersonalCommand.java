package logic.commands.personal.notes;

import database.dao.User;
import database.services.UsersService;
import logic.commands.personal.AbsPersonalCommand;
import util.response.StringResponse;

public class GetAllNotesPersonalCommand extends AbsPersonalCommand {
	
	public GetAllNotesPersonalCommand(long userId) {
		super(userId);
	}
	
	/**
	 * Get all notes from user
	 * 
	 * @return Russian String with response message text 
	 */
	@Override
	public StringResponse execute() {
		StringBuilder answer = new StringBuilder();
		
		UsersService usersService = new UsersService();
		User user = usersService.getOrCreateUserByTelegramId(userId);
				
		if (user.getNotes().isEmpty()) {
			answer.append("У тебя пока нет никаких заметок!");
		} else {
			answer.append(String.format("Твои заметки:%s%s", System.lineSeparator(), 
															 System.lineSeparator()));
			
			user.getNotes().forEach((key,value) -> answer.append(
					String.format("%s:%s%s", key, 
											 value, 
											 System.lineSeparator())));
		}
		
		return new StringResponse(answer.toString());
	}
	

}
