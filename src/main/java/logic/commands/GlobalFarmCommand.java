package logic.commands;

import java.io.File;
import java.util.Calendar;

import util.Util;
import util.response.FileResponse;
import util.response.Response;
import util.response.StringResponse;

public class GlobalFarmCommand extends AbsCommand {

	private final int dayOfWeek;
	
	public GlobalFarmCommand(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public Response<?> execute() {
		Response<?> result;
		
		if (dayOfWeek != Calendar.SUNDAY) {
			String nameOfFile = Util.GetPictureFileNameByDay(dayOfWeek);
					
			result = new FileResponse<>(new File(getClass()
												 .getClassLoader()
												 .getResource(nameOfFile)
												 .getFile()));
			
			
		} else {
			result = new StringResponse<>("Фарми что угодно - сегодня воскресенье!");
		}
		
		return result;
	}

}
