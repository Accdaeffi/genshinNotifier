package util.response;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StringWithKeyboardResponse extends StringResponse {
	
	ReplyKeyboard keyboard;

	public StringWithKeyboardResponse(String text, ReplyKeyboard keyboard) {
		super(text);
		this.keyboard = keyboard;
	}
	
	@Override
	public void send(AbsSender sender, Long chatId) throws TelegramApiException {
		SendMessage outMsg = new SendMessage();
		outMsg.setChatId(Long.toString(chatId));
		outMsg.setText(this.getContent());
		outMsg.setReplyMarkup(keyboard);
		sender.execute(outMsg);
	}
}
