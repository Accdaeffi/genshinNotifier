package ru.dnoskov.logic;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.User;

import ru.dnoskov.logic.commands.*;
import ru.dnoskov.logic.commands.personal.SetServerPersonalCommand;
import ru.dnoskov.logic.commands.personal.items.*;
import ru.dnoskov.logic.commands.personal.notes.*;
import lombok.NonNull;

public class MessageParser {
	
	private final static Logger logger = LoggerFactory.getLogger(MessageParser.class);
	
	private static MessageParser instance;
	
	public static MessageParser getParser() {
		if (instance == null) {
			instance = new MessageParser();
		}
		return instance;
	}
	
	private MessageParser() {}
	
	/**
	 * Decide, which message was sent and execute necessary operations. 
	 * Main method of the class.
	 */
	public Optional<AbsCommand> parseMessage(@NonNull String messageText, 
											 @NonNull User messageAuthor) {
		
		try {
			String arr[] = messageText.split(" ", 2);
			String command = arr[0];
			if (command.contains("@")) {
					command = arr[0].substring(0, arr[0].indexOf("@"));
			}
			String argument = (arr.length > 1) ? arr[1] : null;
	
			AbsCommand commandHandler;
			
			
			switch (command) {
				case "/help": {
					commandHandler = new HelpCommand();
				}
				break;
				
				case "/gfarm":
				case "/global_farm": {
					commandHandler = new GlobalFarmCommand(messageAuthor.getId());
				}
				break;
				
				case "/pfarm":
				case "/personal_farm":
				{
					commandHandler = new FarmPersonalCommand(messageAuthor.getId());
				}
				break;
				
				
				/*Work with personal farm targets*/
				case "/list":
				{
					commandHandler = new ListPersonalCommand(messageAuthor.getId());
				}
				break;
				
				case "/add":
				{
					commandHandler = new AddItemPersonalCommand(messageAuthor.getId(), argument);
				}
				break;
				
				case "/del":
				{
					commandHandler = new DelItemPersonalCommand(messageAuthor.getId(), argument);
				}
				break;
				
				
				/*Work with personal notes*/
				case "/get_note":
				{
					commandHandler = new GetNotePersonalCommand(messageAuthor.getId(), argument);
				}
				break;
				
				case "/get_all_notes":
				{
					commandHandler = new GetAllNotesPersonalCommand(messageAuthor.getId());
				}
				break;
				
				case "/add_note":
				{
					commandHandler = new AddNotePersonalCommand(messageAuthor.getId(), argument);
				}
				break;
				
				case "/del_note":
				{
					commandHandler = new DelNotePersonalCommand(messageAuthor.getId(), argument);
				}
				break;
				
				case "/set_server":
				{
					commandHandler = new SetServerPersonalCommand(messageAuthor.getId());
				}
				break;
				
				default: {
					commandHandler = null;
				}
			}
			
			return Optional.ofNullable(commandHandler);
		}
		catch (Exception ex) {
			logger.error("Error during parsing command {}!", messageText, ex);
			return Optional.ofNullable(null);
		}
	}
}
