package ru.dnoskov.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.dnoskov.database.sources.MongoDataSource;

import java.util.Optional;

import ru.dnoskov.logic.CallbackParser;
import ru.dnoskov.logic.MessageParser;
import ru.dnoskov.logic.commands.AbsCommand;
import ru.dnoskov.util.response.Response;

public class Bot extends TelegramLongPollingBot {
	
	private final static Logger logger = LoggerFactory.getLogger(Bot.class);
	
	private final MessageParser commandParser = MessageParser.getParser(); 
	private final CallbackParser callbackParser = CallbackParser.getParser(); 

	private final String BOT_USERNAME;
	private final String BOT_TOKEN;
	
	Bot(String botUserName, String botToken, String dbUser, String dbPass) {
		this.BOT_USERNAME = botUserName;
		this.BOT_TOKEN = botToken;
		
		Properties.setDataSource(MongoDataSource.getDatabase(dbUser, dbPass));
	}

	@Override
	public void onUpdateReceived(Update update) {
		
		if (update.hasMessage() && update.getMessage().hasText()) {
			
			Message message = update.getMessage();
			
			String messageText = message.getText();
			Long chatId = message.getChatId();
			User author = message.getFrom();
			
			if ((messageText.startsWith("/"))&& // if it is command
				((messageText.contains(getBotUsername()))||(!messageText.contains("@")))) { // and for us or for all bots in group
				String authorId = (author.getUserName() == null) ? author.getFirstName() 
															     : author.getUserName();
				logger.info("Command {} from {}", messageText, authorId);
			
			
				/* Parsing command */
				Optional<AbsCommand> optionalCommandHandler = commandParser.parseMessage(messageText, author);
				
				optionalCommandHandler.ifPresent(handler -> {
					try {
						
						/* Executing command */
						Response<?> result = handler.execute();
						
						/* Sending result of command */
						result.send(this, chatId);
					}
					catch (TelegramApiException ex) {
						logger.error("Error sending result of command {} from {}!", messageText, author.getId(), ex);
					}
					catch (Exception ex) {
						logger.error("Error during processing command {}!", messageText, ex);
					}
				});
			}
		
		} else if (update.hasCallbackQuery()) {
			
			CallbackQuery callback = update.getCallbackQuery();
			
			String messageText = callback.getData();
			long chatId = callback.getMessage().getChatId();
			int messageId = callback.getMessage().getMessageId();
			User author = callback.getFrom();
			
			String authorId = (author.getUserName() == null) ? author.getFirstName() 
															 : author.getUserName();
			logger.info("Callback \"{}\" from {}", messageText, authorId);
			
			/* Parsing callback */
			Optional<AbsCommand> optionalCallbackHandler = callbackParser.parseCallback(messageText, messageId, author);
		
			optionalCallbackHandler.ifPresent(handler -> {
				try {
					
					/* Executing command */
					Response<?> result = handler.execute();
					
					/* Sending result of command */
					result.send(this, chatId);
				}
				catch (TelegramApiException ex) {
					logger.error("Error sending result of callback {} from {}!", messageText, author.getId(), ex);
				}
				catch (Exception ex) {
					logger.error("Error during processing callback {}!", messageText, ex);
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

}
