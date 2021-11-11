package logic.commands;

import java.io.File;
import java.util.Calendar;

import util.Util;

public class GlobalFarmCommand extends AbsCommand {

	private final int dayOfWeek;
	
	public GlobalFarmCommand(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public Object execute() {
		if (dayOfWeek != Calendar.SUNDAY) {
			String nameOfFile = Util.GetPictureFileNameByDay(dayOfWeek);
					
			return new File(getClass()
								.getClassLoader()
								.getResource(nameOfFile)
								.getFile());
			
			
		} else {
			return new String("Фарми что угодно - сегодня воскресенье!");
		}
	}

}
