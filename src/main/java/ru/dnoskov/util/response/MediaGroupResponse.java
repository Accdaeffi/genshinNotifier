package ru.dnoskov.util.response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.dnoskov.util.filework.FileMapper;
import ru.dnoskov.util.filework.FileReader;

public class MediaGroupResponse extends Response<List<String>> {

	private final String caption;

	public MediaGroupResponse(List<String> responseContent, String caption) {
		super(responseContent);
		this.caption = caption;
	}

	public List<String> getContent() {
		return responseContent;
	}

	@Override
	public void send(AbsSender sender, Long chatId) throws TelegramApiException {
		SendMediaGroup mediaGroup = new SendMediaGroup();

		List<InputMedia> medias = new ArrayList<InputMedia>();
		boolean captionAdded = false;

		for (String fileName : this.getContent()) {
			InputMediaPhoto photo = new InputMediaPhoto();
			
			if (FileMapper.FileNameFileIdMap.containsKey(fileName)) {
				photo.setMedia(FileMapper.FileNameFileIdMap.get(fileName));
			} else {
				FileReader reader = new FileReader();
				File filePhoto = reader.readFileFromDirectory("", fileName);

				photo.setMedia(filePhoto, fileName);
			}

			if (!captionAdded) {
				photo.setCaption(caption);
				captionAdded = true;
			}

			medias.add(photo);

		}

		mediaGroup.setMedias(medias);
		mediaGroup.setChatId(Long.toString(chatId));
		sender.execute(mediaGroup);
	}

}
