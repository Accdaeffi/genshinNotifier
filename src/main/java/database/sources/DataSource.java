package database.sources;

import database.repositories.ItemsRepository;
import database.repositories.UsersRepository;

public interface DataSource {
	
	public ItemsRepository getItemsRepository();
	
	public UsersRepository getUsersRepository();
}
