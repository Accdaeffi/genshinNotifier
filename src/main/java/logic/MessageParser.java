package logic;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.User;

import logic.commands.*;
import logic.commands.personal.items.*;
import logic.commands.personal.notes.*;
import lombok.Getter;

public class MessageParser {
	
	private final static Logger logger = LoggerFactory.getLogger(MessageParser.class);
	
	// Server time GMT+1, Domain restart time - 4:00 at server time
	// So in GMT-3 timezone domain get changed at 0:00
	private static final String TIME_OFFSET = "-3"; 

	// who sent message with command
	@Getter
	private final User messageAuthor;
	
	// where message was sent
	@Getter
	private final Long messageChatId;
	
	// text of the message
	@Getter
	private final String messageText;
	
	// today day of week
	@Getter
	private final int dayOfWeek;
	
	
	public MessageParser(String messageText, 
						 Long chatId, 
						 User author) {
		
		this.messageText = messageText;
		this.messageChatId = chatId;
		this.messageAuthor = author;
		
		Calendar today = Calendar.getInstance(
				TimeZone.getTimeZone(ZoneId.of(TIME_OFFSET))); 
		
		dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * Decide, which message was sent and execute necessary operations. 
	 * Main method of the class.
	 */
	public AbsCommand parseMessage() {
		
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
					commandHandler = new GlobalFarmCommand(dayOfWeek);
				}
				break;
				
				case "/pfarm":
				case "/personal_farm":
				{
					commandHandler = new FarmPersonalCommand(messageAuthor.getId(), dayOfWeek);

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
				
				default: {
					commandHandler = null;
				}
			}
			
			
			return commandHandler;
		}
		catch (Exception ex) {
			String logString = String.format("Error during parsing command \"{}\"!", messageText);
			logger.error(logString, ex);
			return null;
		}
	}
}
