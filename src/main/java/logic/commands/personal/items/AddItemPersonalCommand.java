package logic.commands.personal.items;

import org.bson.Document;

import database.DbUsersMethods;
import logic.commands.personal.AbsPersonalCommand;
import util.NoSuchItemException;

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
	public String execute() {
		String answer;
		
		if (itemNameOrTag == null) {
			answer = "Ты не указал, что тебе нужно. Названия, если что - с английского HHW или из /help.";
		} else {
			try {
				DbUsersMethods databaseUsers = new DbUsersMethods(); 
				Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
					
				if (databaseUsers.addItem(user, itemNameOrTag)) {
					answer = "Успешно добавлено!";
				} else {
					answer = "Ты уже добавил этот предмет!";
				}
			}
			catch (NoSuchItemException e) {
				answer = "Ты неправильно указал, что тебе нужно. Названия бери с английского HHW или из /help.";
			}
		}
		
		return answer;
	}

}
