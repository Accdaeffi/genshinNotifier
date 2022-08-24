package ru.dnoskov.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Util {

	/**
	 * Convert Thursday to Monday, Friday to Tuesday and Saturday to Wednesday
	 * 
	 * @param dayOfWeek from Calendar.DAY_OF_WEEK
	 * @return 0, if Sunday, 1, if Monday or Thursday 2, if Tuesday or Friday 3, if
	 *         Wednesday or Saturday
	 */
	public static int ConvertWeekDayToFarmDay(int dayOfWeek) {
		int result;

		switch (dayOfWeek) {
		case Calendar.MONDAY:
		case Calendar.THURSDAY:
			result = 1;
			break;
		case Calendar.TUESDAY:
		case Calendar.FRIDAY:
			result = 2;
			break;
		case Calendar.WEDNESDAY:
		case Calendar.SATURDAY:
			result = 3;
			break;
		default:
			result = 0;
		}

		return result;
	}

	/**
	 * Get name of file with picture for the day of farm
	 * 
	 * @param dayOfWeek from Calendar.DAY_OF_WEEK
	 * @return name of file with extension
	 */
	public static List<String> GetFileNamesByDay(int dayOfWeek) {
		List<String> names = new ArrayList<String>();

		switch (dayOfWeek) {
		case Calendar.MONDAY:
		case Calendar.THURSDAY:
			names.add("1_char.jpg");
			names.add("1_weap.jpg");
			break;
		case Calendar.TUESDAY:
		case Calendar.FRIDAY:
			names.add("2_char.jpg");
			names.add("2_weap.jpg");
			break;
		case Calendar.WEDNESDAY:
			names.add("3_char.jpg");
			names.add("3_weap.jpg");
			break;
		case Calendar.SATURDAY:
			names.add("6_char.jpg");
			names.add("6_weap.jpg");
			break;
		default:
			names.add("error"); // TODO: replace with custom exception
		}

		return names;
	}

	// TODO: rewrite on English
	/**
	 * Возвращает, дроп с какого дня недели сейчас падает с подземелий.
	 * 
	 * @return day of week from Calendar.DAY_OF_WEEK
	 */
	public static int GetDayOfWeek(TimeZone timezone) {
		Calendar today = Calendar.getInstance(timezone);

		return today.get(Calendar.DAY_OF_WEEK);
	}
}
