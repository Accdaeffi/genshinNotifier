package database.repositories.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import database.dao.Item;
import database.dao.User;
import database.repositories.IncorrectDataSourceException;
import database.repositories.UsersRepository;
import database.sources.DataSource;
import database.sources.MongoDataSource;
import main.Properties;

public class MongoUsersRepository implements UsersRepository {

	private MongoCollection<Document> users;
	
	public MongoUsersRepository () throws IncorrectDataSourceException {
		DataSource dataSource = Properties.dataSource;
		
		if (dataSource instanceof MongoDataSource) {
			users = ((MongoDataSource) dataSource).getUsers();
		} else {
			throw new IncorrectDataSourceException();
		}
	}

	@Override
	public User getUserById(long userId) {
		Document user = users.find(Filters.eq("id", userId)).first();
		
		return userFromDocument(user);
	}

	@Override
	public void createUserById(long userId) {
		Document user =  new Document().append("id", userId)
								.append("items", new ArrayList<String>())
								.append("notes", new Document());
		users.insertOne(user);
	}

	@Override
	public boolean addItem(User user, Item item) {
		Bson filter = Filters.eq("id", user.getId());
		Bson update = Updates.push("items", item.getName());
		return users.updateOne(filter, update).getModifiedCount() == 1;
	}

	@Override
	public boolean delItem(User user, Item item) {
		Bson filter = Filters.eq("id", user.getId());
		Bson update = Updates.pull("items", item.getName());
		return users.updateOne(filter, update).getModifiedCount() == 1;
	}

	/* XXX */
	@Override
	public String addOrReplaceNote(User user, String key, String value) {
		Document userFromDb = users.find(Filters.eq("id", user.getId())).first();
		Document userNotesFromDb = (Document) userFromDb.get("notes");
	
		String prevoiusValue = (String) userNotesFromDb.put(key, value);
		
		Bson filter = Filters.eq("id", user.getId());
		
		users.replaceOne(filter, userFromDb);
		
		return prevoiusValue;
	}
	
	/* XXX */
	@Override
	public String deleteNote(User user, String key) {
		Document userFromDb = users.find(Filters.eq("id", user.getId())).first();
		Document userNotesFromDb = (Document) userFromDb.get("notes");
	
		String prevoiusValue = (String) userNotesFromDb.remove(key);
		
		Bson filter = Filters.eq("id", user.getId());
		
		users.replaceOne(filter, userFromDb);
		
		return prevoiusValue;
	}
	
	private User userFromDocument(Document userFromDatabase) {
		User user = null;
		
		if (userFromDatabase != null) {	
			long id = userFromDatabase.getLong("id");
			List<String> items = userFromDatabase.getList("items", String.class);
			Map<String, String> notes = new HashMap<>(); 
			((Document) userFromDatabase.get("notes"))
				.entrySet()
				.parallelStream()
				.forEach(entry -> notes.put(entry.getKey(), (String) entry.getValue()));
				
			user = new User(id, items, notes);
		}
		
		return user;
	}
	
}
