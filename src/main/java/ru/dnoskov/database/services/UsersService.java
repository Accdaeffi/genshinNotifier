package ru.dnoskov.database.services;

import ru.dnoskov.database.dao.Item;
import ru.dnoskov.database.dao.Server;
import ru.dnoskov.database.dao.User;
import ru.dnoskov.database.repositories.UsersRepository;
import ru.dnoskov.main.Properties;
import ru.dnoskov.util.NoSuchItemException;

public class UsersService {
	UsersRepository repository;
	
	public UsersService() {
		repository = Properties.getDataSource().getUsersRepository();
	}
	
	/**
	 * Get or create user by its Telegram id
	 * 
	 * @param userId - telegram id of user
	 * @return document, describing user
	 */
	public User getOrCreateUserByTelegramId(Long userId) {
		User user = repository.getUserById(userId);
		
		if (user == null) {
			user = repository.createUserById(userId);
		}
		
		return user;
	}
	
	/**
	 * Add item by its name or tag to user's list of targets of farm
	 * 
	 * @param user
	 * @param itemNameOrTag 
	 * @return true, if added
	 * 		   false, if already exists
	 * @throws NoSuchItemException if unknown name or tag
	 */
	public boolean addItem(User user, String itemNameOrTag) throws NoSuchItemException {
		ItemsService itemsService = new ItemsService();
		
		Item item = itemsService.getItemByNameOrTag(itemNameOrTag);
		
		return repository.addItem(user, item);
	}
	
	/**
	 * Delete item by its name or tag from user's list of targets of farm
	 * 
	 * @param user
	 * @param itemNameOrTag
	 * @return true, if delete 
	 * 		   false, if there are no such item
	 * @throws NoSuchItemException if unknown name or tag
	 */
	public boolean delItem(User user, String itemNameOrTag) throws NoSuchItemException {
		ItemsService itemsService = new ItemsService();
		
		Item item = itemsService.getItemByNameOrTag(itemNameOrTag);
		
		return repository.delItem(user, item);
	}
	
	/**
	 * Add or replace personal note 
	 * 
	 * @param user
	 * @param key of the note
	 * @param value text of the note 
	 * @return previous value of such key or null, if new key
	 */
	public String addOrReplaceNote(User user, String key, String value) {
		return repository.addOrReplaceNote(user, key, value);
	}
	
	/**
	 * Delete personal note by its key
	 * 
	 * @param user
	 * @param key
	 * @return previous value of such key or null, if such note didn't exist
	 */
	public String deleteNote(User user, String key) {
		return repository.deleteNote(user, key);
	}
	
	/* TODO: add comment */
	public boolean setServer(User user, Server server) {
		return repository.setServer(user, server.toString());
	}

}
