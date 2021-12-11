package ru.dnoskov.database.sources;

import ru.dnoskov.database.repositories.ItemsRepository;
import ru.dnoskov.database.repositories.UsersRepository;

public interface DataSource {
	
	public ItemsRepository getItemsRepository();
	
	public UsersRepository getUsersRepository();
}
