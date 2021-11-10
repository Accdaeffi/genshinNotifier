package database;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import util.NoSuchItemException;

public class DbUsersMethods {
	private MongoCollection<Document> users = DbBase.getDatabase().getUsers();
	
	/**
	 * Get or create user by its Telegram id
	 * 
	 * @param userId - telegram id of user
	 * @return document, describing user
	 */
	public Document getOrCreateUserByTelegramId(Long userId) {
		Document user = users.find(Filters.eq("id", userId)).first();
		
		if (user == null) {
			user = createUser(userId);
			users.insertOne(user);
		}
		
		return user;
	}
	
	/* Create new user based on its Telegram id*/
	private Document createUser(Long userId) {
		return new Document().append("id", userId)
							 .append("items", new ArrayList<String>())
							 .append("notes", new Document());
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
	public boolean addItem(Document user, String itemNameOrTag) throws NoSuchItemException {
		DbItemsMethods databaseItems = new DbItemsMethods();
		
		Document item = databaseItems.getItemByNameOrTag(itemNameOrTag);
		String itemName = item.getString("name");
		
		if (user.getList("items", String.class).contains(itemName)) {
			return false;
		} else {
			Bson update = Updates.push("items", itemName);
			users.updateOne(Filters.eq("id", user.get("id")), update);
			return true;
		}
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
	public boolean delItem(Document user, String itemNameOrTag) throws NoSuchItemException {
		boolean result;
		
		DbItemsMethods databaseItems = new DbItemsMethods();
		
		Document item = databaseItems.getItemByNameOrTag(itemNameOrTag);
		String itemName = item.getString("name");
		
		if (user.getList("items", String.class).contains(itemName)) {
			Bson update = Updates.pull("items", itemName);
			users.updateOne(Filters.eq("id", user.get("id")), update);
			result = true;
		} else {
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Add or replace personal note 
	 * 
	 * @param user
	 * @param key of the note
	 * @param value text of the note 
	 * @return previous value of such key or null, if new key
	 */
	public String addOrReplaceNote(Document user, String key, String value) {
		Document userNotes = (Document) user.get("notes");
		
		String previousNote = userNotes.getString(key);

		userNotes.put(key, value);
		users.replaceOne(Filters.eq("id", user.get("id")), user);
	
		return previousNote;
	}
	
	/**
	 * Delete personal note by its key
	 * 
	 * @param user
	 * @param key
	 * @return true, if deleted, false, if such note didn't exist
	 */
	public boolean deleteNote(Document user, String key) {
		boolean result;
		
		Document userNotes = (Document) user.get("notes");
		
		if (userNotes.remove(key) != null) {
			result = true;
			users.replaceOne(Filters.eq("id", user.get("id")), user);
		} else {
			result = false;
		}
		
		return result;
	}
}
