package logic.commands.personal.items;

import database.dao.User;
import database.services.UsersService;
import logic.commands.personal.AbsPersonalCommand;
import util.NoSuchItemException;
import util.response.StringResponse;

public class AddItemPersonalCommand extends AbsPersonalCommand {

	private final String itemNameOrTag;
	
	public AddItemPersonalCommand(long userId, String input) {
		super(userId);
		this.itemNameOrTag = input;
	}
	
	/**
	 * Add new item as a target to farm
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
					
				if (usersService.addItem(user, itemNameOrTag)) {
					answer = "Успешно добавлено!";
				} else {
					answer = "Ты уже добавил этот предмет!";
				}
			}
			catch (NoSuchItemException e) {
				answer = "Ты неправильно указал, что тебе нужно. Названия бери с английского HHW или из /help.";
			}
		}
		
		return new StringResponse(answer);
	}

}
