package ru.dnoskov.main;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.log4j.Log4j;
import ru.dnoskov.util.response.PhotoResponse;

@Log4j
public class DebugOperations {

	static Long testChatId = 138066272L;
	
	static void doDebug(Bot bot) {
		
		List<String> files = new ArrayList<>();
		files.add("1.jpg");
		
		PhotoResponse response = new PhotoResponse("AMB.png", "test caption");
		
		try {
			response.send(bot, testChatId);
		} catch (TelegramApiException e) {
			log.error(e);
		}
		
	}
}
