package logic.commands;

import util.response.EditMessageResponse;
import util.response.Response;

public class CancelCommand extends AbsCommand {
	
	private final int messageId;

	public CancelCommand(int messageId) {
		this.messageId = messageId;
	}
	
	@Override
	public Response<?> execute() {
		String result = "Команда успешно отменена!";

		return new EditMessageResponse(result, messageId);
	}

	
}
