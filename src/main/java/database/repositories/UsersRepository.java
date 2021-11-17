package database.repositories;

import database.dao.Item;
import database.dao.User;

public interface UsersRepository {

	/**
	 * Get user by its Telegram id. 
	 * 
	 * @param userId - telegram id of user
	 * @return user
	 */
	public User getUserById(long userId);
	
	/**
	 * Create user by its Telegram id. If such user already exists - do nothing. 
	 * 
	 * @param userId - telegram id of user
	 */
	void createUserById(long userId);
	
	public boolean addItem(User user, Item item);
	
	public boolean delItem(User user, Item item);
	
	public String addOrReplaceNote(User user, String key, String value);
	
	public String deleteNote(User user, String key);
	
}
