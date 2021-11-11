package logic.commands;

import util.Response;

public abstract class AbsCommand {
	
	public abstract Response<?> execute();
}
