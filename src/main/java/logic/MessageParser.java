package logic;

import java.io.File;
import java.time.ZoneId;
import java.util.Calendar;
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
	// So in GMT-3 timezone domain get changed at 0:00
	private final String TIME_OFFSET = "-3"; 

	@Setter
	@Getter
	private MongoDatabase database;

	// who sent message with command
	@Setter
	@Getter
	private User messageAuthor;
	
	// where message was sent
	@Setter
	@Getter
	private Long messageChatId;
	
	// text of the message
	@Setter
	@Getter
	private String messageText;
	
	// today day of week
	@Setter
	@Getter
	private int dayOfWeek;
	
	// use this to send messages 
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
	}
	
	/**
	 * Decide, which message was sent and execute necessary operations. 
	 * Main method of the class.
	 */
	public void parseMessage() {
		try {
			String arr[] = messageText.split(" ", 2);
			String command = arr[0];
			if (command.contains("@")) {
					command = arr[0].substring(0, arr[0].indexOf("@"));
			}
			String argument = arr.length > 1 ? arr[1] : null;
	
			switch (command) {
			
				case "/help": {
					StringBuilder builder = new StringBuilder();
					builder.append("/global\\_farm \\- для общей картины");
					builder.append(System.lineSeparator());
					builder.append("/personal\\_farm \\(или /pfarm\\) \\- для того, чтобы узнать, что именно тебе надо фармить сегодня");
					builder.append(System.lineSeparator());
					builder.append("/add \\*имя персонажа на английском \\(с HHW\\)\\* \\- чтобы сказать боту, что тебе это надо");
					builder.append(System.lineSeparator());
					builder.append("/del \\*имя персонажа на английском \\(с HHW\\)\\* \\- чтобы сказать боту, что тебе это больше не надо");
					sendMessage(builder.toString());
				}
				break;
				
				case "/gfarm":
				case "/global_farm": {
					try {
						if (dayOfWeek != Calendar.SUNDAY) {
							
							String nameOfFile = Util.ConvertWeekDayToFarmDay(dayOfWeek)+".jpg";
									
							sendPhoto(new File(getClass()
												.getClassLoader()
												.getResource(nameOfFile)
												.getFile()));
							
							
						} else {
							sendMessage("Фарми что угодно \\- сегодня воскресенье\\!");
						}
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				break;
				
				case "/pfarm":
				case "/personal_farm":
				{
					String answerText;
					
					if (messageAuthor.getId()<0) { 	// если это пишется от лица канала или чата
						answerText = "Ты кто такой вообще?";
					} else {
						PersonalFarm personalFarm = new PersonalFarm(messageAuthor.getId(), database);
						answerText = personalFarm.getPersonalFarm(dayOfWeek);
					}
				
					sendMessage(answerText);
				}
				break;
				
				case "/list":
				{
					String answerText;
					
					if (messageAuthor.getId()<0) { 	// если это пишется от лица канала или чата
						answerText = "Ты кто такой вообще?";
					} else {
						PersonalFarm personalFarm = new PersonalFarm(messageAuthor.getId(), database);
						answerText = personalFarm.list();
					}
					
					sendMessage(answerText);
				}
				break;
				
				case "/add":
				{
					String answerText;
					
					if (messageAuthor.getId()<0) { 	// если это пишется от лица канала или чата
						answerText = "Ты кто такой вообще?";
					} else {
						if (messageChatId<0) {		// если это пишется в чате
							answerText = "Давай в личку, нечего чат засорять";
						} else {
							PersonalFarm personalFarm = new PersonalFarm(messageAuthor.getId(), database);
							answerText = personalFarm.add(argument);
						}
					}
					
					sendMessage(answerText);
				}
				break;
				
				case "/del":
				{
					String answerText;
					
					if (messageAuthor.getId()<0) { 	// если это пишется от лица канала или чата
						answerText = "Ты кто такой вообще?";
					} else {
						if (messageChatId<0) {		// если это пишется в чате
							answerText = "Давай в личку, нечего чат засорять";
						} else {
							PersonalFarm personalFarm = new PersonalFarm(messageAuthor.getId(), database);
							answerText = personalFarm.del(argument);
						}
					}
					
					sendMessage(answerText);
				}
				break;
				
				default: {
					
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	private void sendMessage(String answerText) {
		SendMessage outMsg = new SendMessage();
		outMsg.setChatId(getMessageChatId().toString());
		outMsg.setText(answerText);
		outMsg.enableMarkdownV2(true);
		try {
			sender.execute(outMsg);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	private void sendPhoto(File answerPhoto) {
		SendPhoto photo = new SendPhoto();
		photo.setPhoto(new InputFile(answerPhoto));
		photo.setChatId(getMessageChatId().toString());
		try {
			sender.execute(photo);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
