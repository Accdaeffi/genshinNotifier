package logic.commands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import util.response.Response;
import util.response.StringResponse;

public class HelpCommandTest {

	@Test
	void testCreation() {
		HelpCommand command = new HelpCommand();
		assertNotNull(command);
	}

	@Test
	void testExecution() {
		HelpCommand command = new HelpCommand();
		assertNotNull(command);
		
		StringBuilder builder = new StringBuilder();
		builder.append("Список команд бота:");
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		builder.append("https://telegra.ph/Spisok-komand-GenshinNotifierBot-11-06");
		
		Response<?> response = command.execute();
		
		assertTrue(response.getClass().equals(StringResponse.class));
		assertEquals(builder.toString(), (String) response.getContent());
	}
	
}
