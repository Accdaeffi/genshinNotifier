package util.response;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class EditMessageResponse extends StringResponse {

	private int editingMessageId;
	
	public EditMessageResponse(String responseContent, int editingMessageId) {
		super(responseContent);
		this.editingMessageId = editingMessageId;
	}
	
	@Override
	public void send(AbsSender sender, Long chatId) throws TelegramApiException {
		EditMessageText outMsg = new EditMessageText();
		outMsg.setChatId(Long.toString(chatId));
		outMsg.setMessageId(editingMessageId);
		outMsg.setText(this.getContent());
		sender.execute(outMsg);
	}

}
