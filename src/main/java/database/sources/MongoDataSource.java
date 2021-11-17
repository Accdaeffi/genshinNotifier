package database.sources;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import database.repositories.ItemsRepository;
import database.repositories.UsersRepository;
import database.repositories.mongo.MongoItemsRepository;
import database.repositories.mongo.MongoUsersRepository;
import lombok.NonNull;

public class MongoDataSource implements DataSource {
	private static MongoDataSource instance;
	
	private final MongoDatabase database; 
	
	public static DataSource getDatabase(@NonNull String dbUser, @NonNull String dbPass) {
		if (instance == null) {
			instance = new MongoDataSource(dbUser, dbPass);
		}
		return instance;
	}
	
	public static MongoDataSource getDatabase() {
		return instance;
	}

	private MongoDataSource(String dbUser, String dbPass) {
		ConnectionString connectionString = new ConnectionString("mongodb+srv://"+dbUser+":"+dbPass+"@dnoskov.emlnn.mongodb.net/DNoskov?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder()
		        .applyConnectionString(connectionString)
		        .build();
		MongoClient mongoClient = MongoClients.create(settings);
		database = mongoClient.getDatabase("genshinNotifier");
	}

	@Override
	public ItemsRepository getItemsRepository() {
		return new MongoItemsRepository(database.getCollection("items"));
	}

	@Override
	public UsersRepository getUsersRepository() {
		return new MongoUsersRepository(database.getCollection("users"));
	}
}
