package ru.dnoskov.logic.commands;

import ru.dnoskov.util.response.Response;

public abstract class AbsCommand {
	
	public abstract Response<?> execute();
}
