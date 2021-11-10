package logic.commands.personal.items;

import java.util.List;

import org.bson.Document;

import database.DbUsersMethods;
import logic.commands.personal.AbsPersonalCommand;

public class ListPersonalCommand extends AbsPersonalCommand {

	public ListPersonalCommand(long userId) {
		super(userId);
	}

	/**
	 * Get list with targets of farm
	 * 
	 * @return Russian String with response message text 
	 */
	@Override
	public String execute() {
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

}
