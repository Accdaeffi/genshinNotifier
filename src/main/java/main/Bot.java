package main;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import database.DbBase;
import logic.MessageParser;
import logic.commands.AbsCommand;


public class Bot extends TelegramLongPollingBot {
	
	private final static Logger logger = LoggerFactory.getLogger(Bot.class);
	
	private MessageParser commandParser = MessageParser.getParser(); 

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
				String authorId = (author.getUserName() == null) ? author.getFirstName() 
															     : author.getUserName();
				logger.info("Command {} from {}", messageText, authorId);
			}
			
			/* Parsing command */
			AbsCommand commandHandler = commandParser.parseMessage(messageText, author);
			
			/* Executing command */
			if (commandHandler != null) {
				try {
					Object result = commandHandler.execute();
					
					/* XXX: Animation and Video is file too */
					if (result instanceof String) {
						sendMessage((String) result, chatId);
					} else {
						if (result instanceof File) {
							sendPhoto((File) result, chatId);
						}
					}
				} 
				catch (Exception ex) {
					logger.error("Error during executing command {}!", messageText, ex);
				}
			}

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
	
	private void sendMessage(String answerText, Long chatId) {
		SendMessage outMsg = new SendMessage();
		outMsg.setChatId(Long.toString(chatId));
		outMsg.setText(answerText);
		try {
			execute(outMsg);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	private void sendPhoto(File answerPhoto, Long chatId) {
		SendPhoto photo = new SendPhoto();
		photo.setPhoto(new InputFile(answerPhoto));
		photo.setChatId(Long.toString(chatId));
		try {
			execute(photo);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
