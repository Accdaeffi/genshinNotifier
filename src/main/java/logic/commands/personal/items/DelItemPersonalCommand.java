package logic.commands.personal.items;

import database.dao.User;
import database.services.UsersService;
import logic.commands.personal.AbsPersonalCommand;
import util.NoSuchItemException;
import util.response.StringResponse;

public class DelItemPersonalCommand extends AbsPersonalCommand {

	private final String itemNameOrTag;
	
	public DelItemPersonalCommand(long userId, String input) {
		super(userId);
		this.itemNameOrTag = input;
	}

	/**
	 * Delete item from list of targets to farm
	 * 
	 * @return Russian String with response message text 
	 */
	@Override
	public StringResponse execute() {
		String answer;
		
		if (itemNameOrTag == null) {
			answer = "Ты не указал, что тебе нужно. Названия, если что - с английского HHW или из /help.";
		} else {
			try {
				UsersService usersService = new UsersService();
				User user = usersService.getOrCreateUserByTelegramId(userId);
					
				if (usersService.delItem(user, itemNameOrTag)) {
					answer = "Успешно удалено!";
				} else {
					answer = "Нельзя удалить то, чего нет!";
				}
			}
			catch (NoSuchItemException e) {
				answer = "Ты неправильно указал, что тебе нужно. Названия бери с английского HHW или из /help.";
			}
		}
		
		return new StringResponse(answer);
	}
}
