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
		if ((dayOfWeek < Calendar.SUNDAY) || 	   // because Sunday is the start of the week
				(dayOfWeek > Calendar.SATURDAY)) { // because Saturday is the end of the week
			throw new IllegalArgumentException();
		} else {
			this.dayOfWeek = dayOfWeek;
		}
	}

	@Override
	public Response<?> execute() {
		Response<?> result;
		
		if (dayOfWeek != Calendar.SUNDAY) {
			String nameOfFile = Util.GetPictureFileNameByDay(dayOfWeek);
					
			result = new FileResponse(new File(getClass()
												 .getClassLoader()
												 .getResource(nameOfFile)
												 .getFile()));
			
			
		} else {
			result = new StringResponse("Фарми что угодно - сегодня воскресенье!"); // TODO: вынести строки в resource-файлы
		}
		
		return result;
	}

}
