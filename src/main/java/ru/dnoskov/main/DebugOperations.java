package ru.dnoskov.main;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.log4j.Log4j;
import ru.dnoskov.util.response.MediaGroupResponse;

@Log4j
public class DebugOperations {

	static Long testChatId = 138066272L;
	
	static void doDebug(Bot bot) {
		
		List<String> files = new ArrayList<>();
		files.add("1.jpg");
		files.add("2.jpg");
		files.add("3.jpg");
		
		MediaGroupResponse response = new MediaGroupResponse(files, "test caption");
		
		try {
			response.send(bot, testChatId);
		} catch (TelegramApiException e) {
			log.error(e);
		}
		
	}
}
