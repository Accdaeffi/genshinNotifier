package main;

import java.util.Map;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class GenshinNotifierApplication {
	private static final Map<String, String> getenv = System.getenv();

	public static void main(String[] args) {
		try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot(getenv.get("BOT_USERNAME"), 
            							getenv.get("BOT_TOKEN"), 
            							getenv.get("DB_USER"),
            							getenv.get("DB_PASS")));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
		
	}

}
