package logic.commands;

import util.response.Response;

public abstract class AbsCommand {
	
	protected long userId;
	
	public abstract Response<?> execute();
}
