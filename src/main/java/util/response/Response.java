package util.response;

import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class Response<T> {

	protected final T responseContent;
	
	public Response(T responseContent) {
		this.responseContent = responseContent;
	}
	
	public T getContent() {
		return responseContent;
	}
	
	public abstract void send(AbsSender sender, Long chatId) throws TelegramApiException;
}
