package ru.dnoskov.logic.postprocessors;

import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.Data;
import ru.dnoskov.model.sendedmessagedto.AbsSendedMessageDto;

@Data
public abstract class AbsPostProcessor<T extends AbsSendedMessageDto> {

	Message sendedMessage;
	Object additionalInfo;
	
	public abstract void doPostProcess();

	public AbsPostProcessor(T dataToPostProcess) {
		this.sendedMessage = dataToPostProcess.getMessage();
		this.additionalInfo = dataToPostProcess.getAdditionalInfo();
	}
	
}
