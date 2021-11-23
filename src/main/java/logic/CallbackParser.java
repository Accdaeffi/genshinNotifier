package logic;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.User;

import logic.commands.*;
import logic.commands.personal.ChangeServerPersonalCommand;
import lombok.NonNull;

public class CallbackParser {
	
	private final static Logger logger = LoggerFactory.getLogger(CallbackParser.class);
	
	private static CallbackParser instance;
	
	public static CallbackParser getParser() {
		if (instance == null) {
			instance = new CallbackParser();
		}
		return instance;
	}
	
	private CallbackParser() {}
	
	/**
	 * Decide, which message was sent and execute necessary operations. 
	 * Main method of the class.
	 */
	public Optional<AbsCommand> parseCallback(@NonNull String messageText,
											  int messageId,
											  @NonNull User messageAuthor) {
		
		try {
			
			String arr[] = messageText.split(" ", 2);
			String command = arr[0];
			String argument = (arr.length > 1) ? arr[1] : null;
	
			AbsCommand commandHandler;
			
			
			switch (command) {
				case "server_change": {
					commandHandler = new ChangeServerPersonalCommand(messageAuthor.getId(), argument, messageId);
				}
				break;
				
				case "cancel": {
					commandHandler = new CancelCommand(messageId);
				}
				break;
				
				default: {
					commandHandler = null;
				}
			}
			
			return Optional.ofNullable(commandHandler);
		}
		catch (Exception ex) {
			logger.error("Error during parsing callback {}!", messageText, ex);
			return Optional.ofNullable(null);
		}
	}
}
