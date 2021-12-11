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
	 * @return created and saved in databased user
	 */
	public User createUserById(long userId);
	
	/**
	 * Add item by its name or tag to user's list of targets of farm
	 * 
	 * @param user
	 * @param itemNameOrTag 
	 * @return true, if added
	 * 		   false, if already exists
	 */
	public boolean addItem(User user, Item item);
	
	/**
	 * Delete item by its name or tag from user's list of targets of farm
	 * 
	 * @param user
	 * @param itemNameOrTag
	 * @return true, if deleted 
	 * 		   false, if there are no such item
	 */
	public boolean delItem(User user, Item item);
	
	/**
	 * Add or replace personal note 
	 * 
	 * @param user
	 * @param key of the note
	 * @param value text of the note 
	 * @return previous value of such key or null, if new key
	 */
	public String addOrReplaceNote(User user, String key, String value);
	
	/**
	 * Delete personal note by its key
	 * 
	 * @param user
	 * @param key
	 * @return previous value of such key or null, if such note didn't exist
	 */
	public String deleteNote(User user, String key);
	
	/**
	 * Set game server for the user
	 * 
	 * @param user
	 * @param server
	 * @return true if successfully set, 
	 * 		   false otherwise	
	 */
	public boolean setServer(User user, String server);
}
