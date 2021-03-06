package ru.dnoskov.logic.commands;

import ru.dnoskov.util.response.StringResponse;

public class HelpCommand extends AbsCommand {
	
	public HelpCommand() {}

	@Override
	public StringResponse execute() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("Список команд бота:");
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		builder.append("https://telegra.ph/Spisok-komand-GenshinNotifierBot-11-06");
		
		return new StringResponse(builder.toString());
	}

}
