package ru.dnoskov.logic.commands;

import java.util.Calendar;
import java.util.List;

import ru.dnoskov.database.dao.User;
import ru.dnoskov.database.services.UsersService;
import ru.dnoskov.util.Util;
import ru.dnoskov.util.response.MediaGroupResponse;
import ru.dnoskov.util.response.PhotoResponse;
import ru.dnoskov.util.response.Response;
import ru.dnoskov.util.response.StringResponse;

public class GlobalFarmCommand extends AbsCommand {
	
	protected final long userId;
	
	public GlobalFarmCommand(long userId) {
		this.userId = userId;
	}

	@Override
	public Response<?> execute() {
		Response<?> result;
		
		UsersService usersService = new UsersService();
		User user = usersService.getOrCreateUserByTelegramId(userId);
		
		int dayOfWeek = Util.GetDayOfWeek(user.getServer().getServerTimeZone());
		
		if (dayOfWeek != Calendar.SUNDAY) {
			List<String> fileNames = Util.GetFileNamesByDay(dayOfWeek);
			String caption = String.format("Сервер: %s", user.getServer().getRussianName());
					
			if (fileNames.size() == 1) {
				result = new PhotoResponse(fileNames.get(0), caption); 
			} else {
				result = new MediaGroupResponse(fileNames, caption);
			}
			
			
		} else {
			result = new StringResponse("Фарми что угодно - сегодня воскресенье!"); // TODO: вынести строки в resource-файлы
		}
		
		return result;
	}

}
