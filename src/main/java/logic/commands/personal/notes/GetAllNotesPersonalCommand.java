package logic.commands.personal.notes;

import org.bson.Document;

import database.DbUsersMethods;
import logic.commands.personal.AbsPersonalCommand;

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
	public String execute() {
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
	

}
