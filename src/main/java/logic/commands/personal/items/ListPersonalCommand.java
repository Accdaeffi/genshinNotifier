package logic.commands.personal.items;

import database.dao.User;
import database.services.UsersService;
import logic.commands.personal.AbsPersonalCommand;
import util.response.StringResponse;

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
	public StringResponse execute() {
		StringBuilder answer = new StringBuilder();
		
		UsersService usersService = new UsersService();
		User user = usersService.getOrCreateUserByTelegramId(userId);
		
		if (user.getItems().isEmpty()) {
			answer.append("Ты пока не нацелен на прокачку ни одного предмета!.");
		} else {			
			answer.append("Ты нацелен на фарм ресурсов для: ");
			answer.append(String.join(", ", user.getItems()));
			answer.append(".");
		}
		
		return new StringResponse(answer.toString());
	}

}
