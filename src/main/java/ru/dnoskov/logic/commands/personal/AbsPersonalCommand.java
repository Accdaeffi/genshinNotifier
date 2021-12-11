package ru.dnoskov.logic.commands.personal;

import ru.dnoskov.logic.commands.AbsCommand;
import ru.dnoskov.util.response.Response;

public abstract class AbsPersonalCommand extends AbsCommand {

	protected final long userId;
	
	public AbsPersonalCommand(long userId) {
		this.userId = userId;
	}

	@Override
	public abstract Response<?> execute();

}
