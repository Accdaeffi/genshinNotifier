package ru.dnoskov.util.response;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.dnoskov.util.filework.FileMapper;
import ru.dnoskov.util.filework.FileReader;

public class PhotoResponse extends Response<String> {
	
	private final String caption; 

	public PhotoResponse(String fileName, String caption) {
		super(fileName);
		this.caption = caption;
	}
	
	public String getContent() {
		return responseContent;
	}
	
	@Override
	public void send(AbsSender sender, Long chatId) throws TelegramApiException {
		SendPhoto photo = new SendPhoto();
		
		String fileId = FileMapper.FileNameFileIdMap.get(this.getContent());
		
		if (fileId != null) {
			photo.setPhoto(new InputFile(fileId));
		} else {
			FileReader fr = new FileReader();
			photo.setPhoto(new InputFile(fr.readFileFromDirectory(this.getContent())));
		}
		
		photo.setCaption(caption);
		photo.setChatId(Long.toString(chatId));
		sender.execute(photo);	
	}
}
