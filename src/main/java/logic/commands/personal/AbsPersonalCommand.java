package logic.commands.personal;

import logic.commands.AbsCommand;
import util.response.Response;

public abstract class AbsPersonalCommand extends AbsCommand {

	protected final long userId;
	
	public AbsPersonalCommand(long userId) {
		this.userId = userId;
	}

	@Override
	public abstract Response<?> execute();

}
