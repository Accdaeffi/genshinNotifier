package logic.commands;

import java.io.File;
import java.util.Calendar;

import util.Response;
import util.Util;

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
					
			result = new Response<File>(new File(getClass()
												 .getClassLoader()
												 .getResource(nameOfFile)
												 .getFile()));
			
			
		} else {
			result = new Response<String>("Фарми что угодно - сегодня воскресенье!");
		}
		
		return result;
	}

}
