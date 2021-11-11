package util.response;

import java.io.File;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class FileResponse extends Response<File> {

	public FileResponse(File responseContent) {
		super(responseContent);
	}
	
	public File getContent() {
		return responseContent;
	}
	
	@Override
	public void send(AbsSender sender, Long chatId) throws TelegramApiException {
		SendPhoto photo = new SendPhoto();
		photo.setPhoto(new InputFile(this.getContent()));
		photo.setChatId(Long.toString(chatId));
		sender.execute(photo);	
	}
}
