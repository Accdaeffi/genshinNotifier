package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import logic.MessageParser;

import lombok.Getter;
import lombok.Setter;

public class Bot extends TelegramLongPollingBot {
	
	final static Logger logger = LoggerFactory.getLogger(Bot.class);

	@Setter
	private String BOT_USERNAME;
	
	@Setter 
	private String BOT_TOKEN;
	
	@Setter
	@Getter
	private MongoDatabase database;
	
	
	Bot(String botUserName, String botToken, String dbUser, String dbPass) {
		this.setBOT_USERNAME(botUserName);
		this.setBOT_TOKEN(botToken);
		
		ConnectionString connectionString = new ConnectionString("mongodb+srv://"+dbUser+":"+dbPass+"@dnoskov.emlnn.mongodb.net/DNoskov?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder()
		        .applyConnectionString(connectionString)
		        .build();
		MongoClient mongoClient = MongoClients.create(settings);
		this.setDatabase(mongoClient.getDatabase("genshinNotifier"));
	}

	@Override
	public void onUpdateReceived(Update update) {
		
		if (update.hasMessage() && update.getMessage().hasText()) {
			
			Message message = update.getMessage();
			
			String messageText = message.getText();
			Long chatId = message.getChatId();
			User author = message.getFrom();
			
			if (messageText.startsWith("/")) {
				String author_id = author.getUserName() == null ? author.getFirstName() 
															    : author.getUserName();
				logger.info("Command {} from {}", messageText, author_id);
			}
			
			MessageParser parser = new MessageParser(database, messageText, chatId, author, this); 
			parser.parseMessage();

		}
		
	}

	@Override
	public String getBotUsername() {
		return BOT_USERNAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

}
