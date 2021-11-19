package logic.commands.personal;

import database.dao.Server;
import database.dao.User;
import database.services.UsersService;
import util.response.EditMessageResponse;
import util.response.Response;

public class ChangeServerPersonalCommand extends AbsPersonalCommand {
	
	private final String newServerString;
	private final int messageId;

	public ChangeServerPersonalCommand(long userId, String newServerString, int messageId) {
		super(userId);
		this.newServerString = newServerString;
		this.messageId = messageId;
	}

	@Override
	public Response<?> execute() {
		String result;
		
		UsersService usersService = new UsersService();
		User user = usersService.getOrCreateUserByTelegramId(userId);
		
		Server newServer = Server.fromString(newServerString);
		
		if (usersService.setServer(user, newServer)) {
			result = "Успешно изменено!";
		} else {
			result = "Что-то пошло не так";
		}
		
		return new EditMessageResponse(result, messageId);
	}

}
