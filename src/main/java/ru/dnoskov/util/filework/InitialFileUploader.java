package ru.dnoskov.util.filework;

import java.io.File;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import lombok.extern.log4j.Log4j;

@Log4j
public class InitialFileUploader {
	
	AbsSender sender;
	
	String storageChatId = "138066272";
	
	public InitialFileUploader(AbsSender sender) {
		this.sender = sender;
	}

	FileReader reader = new FileReader();
	
	public void uploadFilesToTelegramServer() {
		List<File> files = reader.readAllFilesFromDirectory(new String());
		
		for (File f : files) {
			try {
				SendPhoto photo = new SendPhoto();
				photo.setChatId(storageChatId);
				photo.setPhoto(new InputFile(f));
				Message sendedMessage = sender.execute(photo);
				
				String photoId = sendedMessage.getPhoto().get(0).getFileId();
				
				FileMapper.FileNameFileIdMap.put(f.getName(), photoId);
			}
			catch (Exception e) {
				log.error(e);
			}
		}
		
		log.info("Photos cashed!");
		
	}
}
