package ru.dnoskov.logic.postprocessors;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import ru.dnoskov.model.sendedmessagedto.SendedMediaGroupMessageDto;

public class CashFileIdFromPhotoGroupPostProcessor extends AbsPostProcessor<SendedMediaGroupMessageDto> {

	public CashFileIdFromPhotoGroupPostProcessor(SendedMediaGroupMessageDto sendedMessageWithAdditionalInfo) {
		super(sendedMessageWithAdditionalInfo);
	}
	
	@Override
	public void doPostProcess() {
		List<PhotoSize> photos = sendedMessage.getPhoto();
		List<String> photoNames = (List<String>) additionalInfo; 
		
		// TODO complete
		
	}

	
}
