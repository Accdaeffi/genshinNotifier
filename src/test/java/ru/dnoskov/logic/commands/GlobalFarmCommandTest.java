package ru.dnoskov.logic.commands;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

import ru.dnoskov.util.response.FileResponse;
import ru.dnoskov.util.response.Response;
import ru.dnoskov.util.response.StringResponse;

public class GlobalFarmCommandTest {

	@Test
	void testCreation() {
		AbsCommand command = new GlobalFarmCommand(Calendar.SUNDAY);
		assertNotNull(command);
	}
	
	@Test
	void testCreationWithIncorrectArgument() {
		assertThrows(IllegalArgumentException.class,
				() -> new GlobalFarmCommand(8));
		
		assertThrows(IllegalArgumentException.class,
				() -> new GlobalFarmCommand(0));
	}
	
	@Test
	void testExecution() {
		AbsCommand command;
		
		for (int dayOfWeek=Calendar.SUNDAY; dayOfWeek<Calendar.SATURDAY; dayOfWeek++) {
			command = new GlobalFarmCommand(dayOfWeek); 
			
			Response<?> response = command.execute();
			
			if (dayOfWeek == Calendar.SUNDAY) {
				assertTrue(response.getClass().equals(StringResponse.class));
				assertEquals("Фарми что угодно - сегодня воскресенье!", (String) response.getContent());
			} else {
				assertTrue(response.getClass().equals(FileResponse.class));
				
				switch (dayOfWeek) {
					case Calendar.MONDAY:
					case Calendar.THURSDAY: {
						assertEquals(new File(getClass()
								 			.getClassLoader()
								 			.getResource("1.jpg")
								 			.getFile()), 
									response.getContent());
					}
						break;
					case Calendar.TUESDAY:
					case Calendar.FRIDAY: {
						assertEquals(new File(getClass()
					 			.getClassLoader()
					 			.getResource("2.jpg")
					 			.getFile()), 
						response.getContent());
					}
						break;
					case Calendar.WEDNESDAY: {
						assertEquals(new File(getClass()
					 			.getClassLoader()
					 			.getResource("3.jpg")
					 			.getFile()), 
						response.getContent());
					}
					break;
					case Calendar.SATURDAY: {
						assertEquals(new File(getClass()
					 			.getClassLoader()
					 			.getResource("6.jpg")
					 			.getFile()), 
						response.getContent());
					}
					break;
					default:
						fail();
				}
			}

		}
	}

}
