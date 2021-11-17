package logic.commands.personal.items;

import java.util.LinkedList;
import java.util.List;

import database.dao.User;
import database.services.ItemsService;
import database.services.UsersService;
import logic.commands.personal.AbsPersonalCommand;
import util.response.StringResponse;

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
	public StringResponse execute() {
		StringBuilder answer = new StringBuilder();
		
		UsersService usersService = new UsersService();
		User user = usersService.getOrCreateUserByTelegramId(userId);
		
		if (user.getItems().isEmpty()) {
			answer.append("Я не знаю, по каким предметам тебе нужна информация.");
		} else {
			ItemsService itemsService = new ItemsService();
			
			List<String> todayFarmUserItems = new LinkedList<>();
			
			itemsService.getFarmableItemsByDay(user.getItems(), dayOfWeek)
					.forEach(item -> todayFarmUserItems.add(item.getName()));
			
			if (todayFarmUserItems.isEmpty()) {
				answer.append("Сегодня тебе ничего не нужно фармить!");
				answer.append(System.lineSeparator());
				
				List<String> tomorrowFarmUserItems = new LinkedList<>();
				
				/* get next day of week */
				int nextDayOfWeek = dayOfWeek%7+1;
				
				itemsService.getFarmableItemsByDay(user.getItems(), nextDayOfWeek)
					.forEach(item -> tomorrowFarmUserItems.add(item.getName()));
				
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
		return new StringResponse(answer.toString());
	}

}
