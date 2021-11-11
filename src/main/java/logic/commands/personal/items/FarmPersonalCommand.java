package logic.commands.personal.items;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import database.DbItemsMethods;
import database.DbUsersMethods;
import logic.commands.personal.AbsPersonalCommand;

public class FarmPersonalCommand extends AbsPersonalCommand {
	
	private final int dayOfWeek;
	
	public FarmPersonalCommand(long userId, int dayOfWeek) {
		super(userId);
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * Calculate result of a command to get the goals of today's personal farm
	 * 
	 * @return Russian String with response message text 
	 */
	@Override
	public String execute() {
		StringBuilder answer = new StringBuilder();
		
		DbUsersMethods databaseUsers = new DbUsersMethods(); 
		Document user = databaseUsers.getOrCreateUserByTelegramId(userId);
		
		if (user.getList("items", String.class).isEmpty()) {
			answer.append("Я не знаю, по каким предметам тебе нужна информация.");
		} else {
			List<String> userItems = user.getList("items", String.class);
			List<String> todayFarmUserItems = new LinkedList<>();
			
			DbItemsMethods databaseItems = new DbItemsMethods();
			databaseItems.getFarmableItemsByDay(userItems, dayOfWeek)
				         .forEach(item -> todayFarmUserItems.add(item.getString("name")));
			
			if (todayFarmUserItems.isEmpty()) {
				answer.append("Сегодня тебе ничего не нужно фармить!");
				answer.append(System.lineSeparator());
				
				List<String> tomorrowFarmUserItems = new LinkedList<>();
				
				/* get next day of week */
				int nextDayOfWeek = dayOfWeek%7+1;
				
				databaseItems.getFarmableItemsByDay(userItems, nextDayOfWeek)
						     .forEach(item -> tomorrowFarmUserItems.add(item.getString("name")));
				
				if (tomorrowFarmUserItems.isEmpty()) {
					answer.append("И завтра, кстати, тоже.");
				} else {
					answer.append("Зато завтра можно будет фармить ресурсы для: ");
					answer.append(String.join(", ", tomorrowFarmUserItems));
					answer.append(".");
				}
				
			} else {
				answer.append("Сегодня можно фармить ресурсы для: ");
				answer.append(String.join(", ", todayFarmUserItems));
				answer.append(".");
			}
		}
		return answer.toString();
	}

}
