package database;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import lombok.NonNull;

public class DatabaseBase {
	private static DatabaseBase instance;
	
	private MongoDatabase database; 
	
	public static DatabaseBase getDatabase(@NonNull String dbUser, @NonNull String dbPass) {
		if (instance == null) {
			instance = new DatabaseBase(dbUser, dbPass);
		}
		return instance;
	}
	
	protected static DatabaseBase getDatabase() {
		return instance;
	}

	private DatabaseBase(String dbUser, String dbPass) {
		ConnectionString connectionString = new ConnectionString("mongodb+srv://"+dbUser+":"+dbPass+"@dnoskov.emlnn.mongodb.net/DNoskov?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder()
		        .applyConnectionString(connectionString)
		        .build();
		MongoClient mongoClient = MongoClients.create(settings);
		database = mongoClient.getDatabase("genshinNotifier");
	}
	
	protected MongoCollection<Document> getItems() {
		return database.getCollection("items");
	}
	
	protected MongoCollection<Document> getUsers() {
		return database.getCollection("users");
	}
}
