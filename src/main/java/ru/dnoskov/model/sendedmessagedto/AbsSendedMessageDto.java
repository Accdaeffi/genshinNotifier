package ru.dnoskov.model.sendedmessagedto;

import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.Data;

@Data
public abstract class AbsSendedMessageDto<T> {

	Message message;
	T additionalInfo;
}
