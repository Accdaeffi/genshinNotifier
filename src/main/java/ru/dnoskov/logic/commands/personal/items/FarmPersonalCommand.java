package ru.dnoskov.logic.commands.personal.items;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j;
import ru.dnoskov.database.dao.Item;
import ru.dnoskov.database.dao.User;
import ru.dnoskov.database.services.ItemsService;
import ru.dnoskov.database.services.UsersService;
import ru.dnoskov.logic.commands.personal.AbsPersonalCommand;
import ru.dnoskov.main.DebugOperations;
import ru.dnoskov.util.Util;
import ru.dnoskov.util.collage.CollageMaker;
import ru.dnoskov.util.response.PhotoResponse;
import ru.dnoskov.util.response.Response;
import ru.dnoskov.util.response.StringResponse;

@Log4j
public class FarmPersonalCommand extends AbsPersonalCommand {
	
	CollageMaker collageMaker;
	
	public FarmPersonalCommand(long userId, CollageMaker collageMaker) {
		super(userId);
		this.collageMaker = collageMaker;
	}

	/**
	 * Calculate result of a command to get the goals of today's personal farm
	 * 
	 * @return Russian String with response message text 
	 */
	@Override
	public Response execute() {
		Response result;

		StringBuilder answer = new StringBuilder();
		
		UsersService usersService = new UsersService();
		User user = usersService.getOrCreateUserByTelegramId(userId);
		
		int dayOfWeek = Util.GetDayOfWeek(user.getServer().getServerTimeZone());
		
		if (user.getItems().isEmpty()) {
			result = new StringResponse("Я не знаю, по каким предметам тебе нужна информация.");
		} else {
			ItemsService itemsService = new ItemsService();
			
			List<Item> todayFarmUserItems = 
					itemsService.getFarmableItemsByDay(user.getItems(), dayOfWeek);
			
			if (todayFarmUserItems.isEmpty()) {
				answer.append("Сегодня тебе ничего не нужно фармить!");
				answer.append(System.lineSeparator());
				
				/* get next day of week */
				int nextDayOfWeek = dayOfWeek%7+1;
				
				List<Item> tomorrowFarmUserItems = 
						itemsService.getFarmableItemsByDay(user.getItems(), nextDayOfWeek);
	
				
				if (tomorrowFarmUserItems.isEmpty()) {
					answer.append("И завтра, кстати, тоже.");
				} else {
					answer.append("Зато завтра можно будет фармить ресурсы для: ");
					answer.append(String.join(", ", 
							tomorrowFarmUserItems.stream()
								.map(i -> i.getName())
								.collect(Collectors.toList()))
							);
					answer.append(".");
				}
				
				result = new StringResponse(answer.toString());
				
			} else {
				try {
					InputStream collageStream = collageMaker.makeCollage(dayOfWeek, todayFarmUserItems);
					
					result = new PhotoResponse(collageStream, "generated_for_"+user.getId(), null);
				} 
				catch (Exception e) {
					log.error("Error during making collage!", e);
					
					answer.append("Сегодня можно фармить ресурсы для: ");
					answer.append(String.join(", ", 
							todayFarmUserItems.stream()
								.map(i -> i.getName())
								.collect(Collectors.toList()))
							);
					answer.append(".");
					
					result = new StringResponse(answer.toString());
				}
				
			}
		}
		return result;
	}

}
