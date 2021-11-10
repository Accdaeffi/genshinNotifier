package logic.commands.personal;

import logic.commands.AbsCommand;

public abstract class AbsPersonalCommand extends AbsCommand {

	protected final long userId;
	
	public AbsPersonalCommand(long userId) {
		this.userId = userId;
	}

	@Override
	public Object execute() {
		return null;
	}

}
