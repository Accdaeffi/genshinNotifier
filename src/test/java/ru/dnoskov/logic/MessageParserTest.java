package ru.dnoskov.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.User;

import ru.dnoskov.logic.commands.personal.items.*;
import ru.dnoskov.logic.commands.personal.notes.*;
import ru.dnoskov.logic.commands.*;

public class MessageParserTest {

	MessageParser parser;
	User user;
	
	@BeforeEach
	void setUp() {
		user = new User();
		user.setId((long) 1);
		user.setUserName("@test");
		
		parser = MessageParser.getParser();
	}
	
	@Test
	void testCreation() {
		assertNotNull(parser);
		
		MessageParser parser2 = MessageParser.getParser();
		assertEquals(parser, parser2);
	}
	
	@Test
	void testParsingMessagesIncorrectArguments() {
		assertThrows(NullPointerException.class, () -> parser.parseMessage(null, user));
		
		assertEquals(Optional.ofNullable(null), parser.parseMessage("", user));
		assertEquals(Optional.ofNullable(null), parser.parseMessage("test", user));
		
		assertThrows(NullPointerException.class, () -> parser.parseMessage("", null));
		
		
	}
	
	@Test
	void testParsingMessageHelp() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/help", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/help@GenshinNotifierBot", user);
		
		assertTrue(commandHandler.get().getClass().equals(HelpCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(HelpCommand.class));
		
	}
	
	@Test
	void testParsingMessageGFarm() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/gfarm", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/gfarm@GenshinNotifierBot", user);
		
		assertTrue(commandHandler.get().getClass().equals(GlobalFarmCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(GlobalFarmCommand.class));
		
		
		Optional<AbsCommand> commandHandler3 = parser.parseMessage("/global_farm", user);
		Optional<AbsCommand> commandHandler4 = parser.parseMessage("/global_farm@GenshinNotifierBot", user);
		
		assertTrue(commandHandler3.get().getClass().equals(GlobalFarmCommand.class));
		assertTrue(commandHandler4.get().getClass().equals(GlobalFarmCommand.class));
		
	}
	
	@Test
	void testParsingMessagePFarm() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/pfarm", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/pfarm@GenshinNotifierBot", user);
		
		assertTrue(commandHandler.get().getClass().equals(FarmPersonalCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(FarmPersonalCommand.class));
		
		
		Optional<AbsCommand> commandHandler3 = parser.parseMessage("/personal_farm", user);
		Optional<AbsCommand> commandHandler4 = parser.parseMessage("/personal_farm@GenshinNotifierBot", user);
		
		assertTrue(commandHandler3.get().getClass().equals(FarmPersonalCommand.class));
		assertTrue(commandHandler4.get().getClass().equals(FarmPersonalCommand.class));
		
	}
	
	@Test
	void testParsingMessageList() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/list", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/list@GenshinNotifierBot", user);
		
		assertTrue(commandHandler.get().getClass().equals(ListPersonalCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(ListPersonalCommand.class));
	}
	
	@Test
	void testParsingMessageAddItem() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/add@GenshinNotifierBot", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/add test", user);
	
		assertTrue(commandHandler.get().getClass().equals(AddItemPersonalCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(AddItemPersonalCommand.class));
	}
	
	@Test
	void testParsingMessageDelItem() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/del@GenshinNotifierBot", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/del test", user);
	
		assertTrue(commandHandler.get().getClass().equals(DelItemPersonalCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(DelItemPersonalCommand.class));
	}
	
	@Test
	void testParsingMessageAddNote() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/add_note@GenshinNotifierBot", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/add_note test", user);
	
		assertTrue(commandHandler.get().getClass().equals(AddNotePersonalCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(AddNotePersonalCommand.class));
	}
	
	@Test
	void testParsingMessageDelNote() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/del_note@GenshinNotifierBot", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/del_note test", user);
	
		assertTrue(commandHandler.get().getClass().equals(DelNotePersonalCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(DelNotePersonalCommand.class));
	}
	
	@Test
	void testParsingMessageGetNote() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/get_note@GenshinNotifierBot", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/get_note test", user);
	
		assertTrue(commandHandler.get().getClass().equals(GetNotePersonalCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(GetNotePersonalCommand.class));
	}
	
	@Test
	void testParsingMessageGetAllNotes() {
		
		Optional<AbsCommand> commandHandler = parser.parseMessage("/get_all_notes@GenshinNotifierBot", user);
		Optional<AbsCommand> commandHandler2 = parser.parseMessage("/get_all_notes test", user);
	
		assertTrue(commandHandler.get().getClass().equals(GetAllNotesPersonalCommand.class));
		assertTrue(commandHandler2.get().getClass().equals(GetAllNotesPersonalCommand.class));
	}
}
