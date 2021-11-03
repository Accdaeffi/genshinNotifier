package logic;

import java.io.File;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.mongodb.client.MongoDatabase;

import lombok.Getter;
import lombok.Setter;

public class MessageParser {
	
	// Server time GMT+1, Domain restart time - 4:00 at server time
	// So in GMT-3 timezone domain get restarted at 0:00
	private final String TIME_OFFSET = "-3"; 

	@Setter
	@Getter
	private MongoDatabase database;

	@Setter
	@Getter
	private User messageAuthor;
	
	@Setter
	@Getter
	private Long messageChatId;
	
	@Setter
	@Getter
	private String messageText;
	
	@Setter
	@Getter
	private int dayOfWeek;
	
	@Setter
	@Getter
	private AbsSender sender;
	
	public MessageParser(MongoDatabase database, 
						 String messageText, 
						 Long chatId, 
						 User author,
						 AbsSender sender) {
		
		this.setDatabase(database);
		this.setMessageText(messageText);
		this.setMessageChatId(chatId);
		this.setMessageAuthor(author);
		this.sender = sender;
		
		Calendar today = Calendar.getInstance(
				TimeZone.getTimeZone(ZoneId.of(TIME_OFFSET))); 
		
		dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
		
		// if today not Sunday
		if (dayOfWeek != 1) {
			
			// convert Thursday to Monday, Friday to Tuesday and Saturday to Wednesday 
			dayOfWeek = (dayOfWeek-2)%3+1;
		}
	}
	
	public void parseMessage() {
		String arr[] = messageText.split(" ", 2);
		String command = arr[0];
		String argument = arr.length > 1 ? arr[1] : null;

		switch (command) {
			case "/globalFarm": {
				try {
					if (dayOfWeek != 0) {
						String nameOfFile = dayOfWeek+".jpg";
										
						SendPhoto photo = new SendPhoto();
						photo.setPhoto( new InputFile( new File(
								getClass().
								getClassLoader().
								getResource(nameOfFile).
								getFile())));
						photo.setChatId(getMessageChatId().toString());
						try {
							sender.execute(photo);
						} catch (TelegramApiException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						SendMessage outMsg = new SendMessage();
						outMsg.setChatId(getMessageChatId().toString());
						outMsg.setText("Фарми что угодно - сегодня воскресенье!");
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			break;
			default: {
				
			}
			
		}
		
	}
}
