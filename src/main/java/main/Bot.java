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

import java.util.Optional;

import database.DbBase;
import logic.MessageParser;
import logic.commands.AbsCommand;
import util.response.FileResponse;
import util.response.Response;
import util.response.StringResponse;


public class Bot extends TelegramLongPollingBot {
	
	private final static Logger logger = LoggerFactory.getLogger(Bot.class);
	
	private final MessageParser commandParser = MessageParser.getParser(); 

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
			Optional<AbsCommand> optionalCommandHandler = commandParser.parseMessage(messageText, author);
			
			optionalCommandHandler.ifPresent(handler -> {
				try {
					
					/* Executing command */
					Response<?> result = handler.execute();
					
					/* Sending result of command */
					try {
						if (result instanceof StringResponse) {
							send((String) result.getContent(), chatId);
						} else if (result instanceof FileResponse) {
							
							/* XXX: Animation and Video is file too */
							send((File) result.getContent(), chatId);
						}
					}
					catch (Exception ex) {
						logger.error("Error sending result of command {} from {}!", messageText, author.getId(), ex);
					}
				}
				catch (Exception ex) {
					logger.error("Error during executing command {}!", messageText, ex);
				}
			});
		
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
	
	private void send(String answerText, Long chatId) {
		SendMessage outMsg = new SendMessage();
		outMsg.setChatId(Long.toString(chatId));
		outMsg.setText(answerText);
		try {
			execute(outMsg);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	private void send(File answerPhoto, Long chatId) {
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
