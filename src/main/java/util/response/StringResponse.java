package util.response;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StringResponse extends Response<String> {
	
	public StringResponse(String responseContent) {
		super(responseContent);
	}
	
	public String getContent() {
		return responseContent;
	}

	@Override
	public void send(AbsSender sender, Long chatId) throws TelegramApiException {
		SendMessage outMsg = new SendMessage();
		outMsg.setChatId(Long.toString(chatId));
		outMsg.setText(this.getContent());
		sender.execute(outMsg);
	}
}
