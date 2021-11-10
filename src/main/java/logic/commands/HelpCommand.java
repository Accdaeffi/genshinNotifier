package logic.commands;

public class HelpCommand extends AbsCommand {
	
	public HelpCommand() {}

	@Override
	public String execute() {
		StringBuilder builder = new StringBuilder();
		builder.append("Список команд бота:");
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		builder.append("https://telegra.ph/Spisok-komand-GenshinNotifierBot-11-06");
		return builder.toString();
	}

}
