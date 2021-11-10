package logic.commands;

import lombok.Getter;

public abstract class AbsCommand {
	
	@Getter
	private long userId;
	
	public abstract Object execute();
}
