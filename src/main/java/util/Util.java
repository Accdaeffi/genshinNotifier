package util;
import java.util.Calendar;

public class Util {
	
	/** Convert Thursday to Monday, Friday to Tuesday and Saturday to Wednesday
	 *  
	 * @param dayOfWeek from Calendar.DAY_OF_WEEK
	 * @return 0, if Sunday,
	 * 		   1, if Monday or Thursday
	 * 		   2, if Tuesday or Friday
	 * 		   3, if Wednesday or Saturday
	 */
	public static int ConvertWeekDayToFarmDay(int dayOfWeek) {
		int result;
		
		switch (dayOfWeek) {
			case Calendar.MONDAY:
			case Calendar.THURSDAY:	result = 1;
				break;
			case Calendar.TUESDAY:
			case Calendar.FRIDAY: result = 2;
				break;
			case Calendar.WEDNESDAY:
			case Calendar.SATURDAY: result = 3;
				break;
			case Calendar.SUNDAY: result = 0;
				break;
			default: result = 0;
		}
		
		return result;
	}
	
	/**
	 * Get name of file with picture for the day of farm
	 * 
	 * @param dayOfWeek from Calendar.DAY_OF_WEEK
	 * @return name of file with extension
	 */
	public static String GetPictureFileNameByDay(int dayOfWeek) {
		String nameOfFile;
		
		if (dayOfWeek != Calendar.SATURDAY) {
			nameOfFile = Util.ConvertWeekDayToFarmDay(dayOfWeek)+".jpg";
		} else {
			nameOfFile = "6.jpg";
		}
		
		return nameOfFile;
	}
}
