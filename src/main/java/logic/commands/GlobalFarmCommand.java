package logic.commands;

import java.io.File;
import java.util.Calendar;

import database.dao.User;
import database.services.UsersService;
import util.Util;
import util.response.FileResponse;
import util.response.Response;
import util.response.StringResponse;

public class GlobalFarmCommand extends AbsCommand {
	
	public GlobalFarmCommand(long userId) {
		this.userId = userId;
	}

	@Override
	public Response<?> execute() {
		Response<?> result;
		
		UsersService usersService = new UsersService();
		User user = usersService.getOrCreateUserByTelegramId(userId);
		
		int dayOfWeek = Util.GetDayOfWeek(user.getServer().getTimezone());
		
		if (dayOfWeek != Calendar.SUNDAY) {
			String nameOfFile = Util.GetPictureFileNameByDay(dayOfWeek);
			String caption = String.format("Сервер: %s", user.getServer().toRussian());
					
			result = new FileResponse(new File(getClass()
												 .getClassLoader()
												 .getResource(nameOfFile)
												 .getFile()),
									  caption);
			
			
		} else {
			result = new StringResponse("Фарми что угодно - сегодня воскресенье!"); // TODO: вынести строки в resource-файлы
		}
		
		return result;
	}

}
