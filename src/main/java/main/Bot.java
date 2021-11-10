package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import database.DbBase;
import logic.MessageParser;


public class Bot extends TelegramLongPollingBot {
	
	final static Logger logger = LoggerFactory.getLogger(Bot.class);

	private final String BOT_USERNAME;
	private final String BOT_TOKEN;
	
	Bot(String botUserName, String botToken, String dbUser, String dbPass) {
		this.BOT_USERNAME = botUserName;
		this.BOT_TOKEN = botToken;
		
		DbBase.getDatabase(dbUser, dbPass);
	}

	@Override
	public void onUpdateReceived(Update update) {
		
		if (update.hasMessage() && update.getMessage().hasText()) {
			
			Message message = update.getMessage();
			
			String messageText = message.getText();
			Long chatId = message.getChatId();
			User author = message.getFrom();
			
			if (messageText.startsWith("/")) {
				String authorId = author.getUserName() == null ? author.getFirstName() 
															   : author.getUserName();
				logger.info("Command {} from {}", messageText, authorId);
			}
			
			MessageParser parser = new MessageParser(messageText, chatId, author, this); 
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
