package ru.dnoskov.main;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import ru.dnoskov.util.filework.InitialFileUploader;

public class GenshinNotifierApplication {
	private static final Map<String, String> getenv = System.getenv();
	
	final static Logger logger = LoggerFactory.getLogger(GenshinNotifierApplication.class);

	public static void main(String[] args) {
		try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot(getenv.get("BOT_USERNAME"), 
					getenv.get("BOT_TOKEN"), 
					getenv.get("DB_USER"),
					getenv.get("DB_PASS"));
            
            botsApi.registerBot(bot);
            
            InitialFileUploader uploader = new InitialFileUploader(bot);
            uploader.uploadFilesToTelegramServer();
            
            logger.info("Bot started!");
            
        } catch (TelegramApiException e) {
        	logger.error("Critical error!", e);
        }
		
	}

}
