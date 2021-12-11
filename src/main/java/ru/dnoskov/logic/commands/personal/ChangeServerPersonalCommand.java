package ru.dnoskov.logic.commands.personal;

import ru.dnoskov.database.dao.Server;
import ru.dnoskov.database.dao.User;
import ru.dnoskov.database.services.UsersService;
import ru.dnoskov.util.response.EditMessageResponse;
import ru.dnoskov.util.response.Response;

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
