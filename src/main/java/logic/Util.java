package logic;

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
	protected static int ConvertWeekDayToFarmDay(int dayOfWeek) {
		return dayOfWeek>Calendar.WEDNESDAY ? dayOfWeek - 4 
				 							: dayOfWeek - 1;
	}
}
