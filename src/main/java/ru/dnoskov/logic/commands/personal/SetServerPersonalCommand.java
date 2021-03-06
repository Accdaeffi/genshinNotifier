package ru.dnoskov.logic.commands.personal;

import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import ru.dnoskov.database.dao.Server;
import ru.dnoskov.database.dao.User;
import ru.dnoskov.database.services.UsersService;
import ru.dnoskov.util.response.Response;
import ru.dnoskov.util.response.StringWithKeyboardResponse;

public class SetServerPersonalCommand extends AbsPersonalCommand {

	public SetServerPersonalCommand(long userId) {
		super(userId);
	}

	@Override
	public Response<?> execute() {
		UsersService usersService = new UsersService();
		User user = usersService.getOrCreateUserByTelegramId(userId);
		
		String text = String.format("Твой текущий сервер: %s", user.getServer().getRussianName());
	
		InlineKeyboardMarkup keyboard = createKeyboard(user.getServer());
				
		return new StringWithKeyboardResponse(text, keyboard);
	}
	
	private InlineKeyboardMarkup createKeyboard(Server currentServer) {
		InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
		
		List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
		
		EnumSet.allOf(Server.class).stream()
						.filter(server -> !server.equals(currentServer))
						.forEach(server -> {
			InlineKeyboardButton button = new InlineKeyboardButton();
			button.setText(server.getRussianName());
			button.setCallbackData(String.format("server_change %s", server.toString()));
			
			List<InlineKeyboardButton> row = new ArrayList<>();
			row.add(button);
			
			rowList.add(row);
		}); 
		
		InlineKeyboardButton button = new InlineKeyboardButton();
		button.setText("Отмена команды");
		button.setCallbackData(String.format("cancel"));
		
		List<InlineKeyboardButton> row = new ArrayList<>();
		row.add(button);
		
		rowList.add(row);
		
		markup.setKeyboard(rowList);
		
		return markup;
	}

}
