package logic.commands;

import util.response.Response;

public abstract class AbsCommand {
	
	public abstract Response<?> execute();
}
