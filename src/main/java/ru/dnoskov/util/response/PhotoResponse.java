package ru.dnoskov.util.response;

import java.io.InputStream;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.dnoskov.util.filework.FileMapper;
import ru.dnoskov.util.filework.FileReader;

public class PhotoResponse extends Response<String> {
	
	private final InputFile file;

	public PhotoResponse(String fileName, String caption) {
		super(caption);
		
		String fileId = FileMapper.FileNameFileIdMap.get(fileName);
		
		if (fileId != null) {
			
			file = new InputFile(fileId);
		} else {
			FileReader fr = new FileReader();
			file = new InputFile(fr.readFileFromDirectory("", fileName));
		}
		
	}
	
	public PhotoResponse(InputStream fileStream, String fileName, String caption) {
		super(caption);
		
		file = new InputFile(fileStream, fileName);
		
	}
	
	public String getContent() {
		return responseContent;
	}
	
	@Override
	public void send(AbsSender sender, Long chatId) throws TelegramApiException {
		SendPhoto photo = new SendPhoto();
		
		photo.setPhoto(file);
		photo.setCaption(this.getContent());
		photo.setChatId(Long.toString(chatId));
		
		sender.execute(photo);	
	}
}
