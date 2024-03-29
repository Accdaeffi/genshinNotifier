package ru.dnoskov.database.sources;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import lombok.NonNull;
import ru.dnoskov.database.repositories.ItemsRepository;
import ru.dnoskov.database.repositories.MaterialsRepository;
import ru.dnoskov.database.repositories.UsersRepository;
import ru.dnoskov.database.repositories.mongo.MongoItemsRepository;
import ru.dnoskov.database.repositories.mongo.MongoMaterialsRepository;
import ru.dnoskov.database.repositories.mongo.MongoUsersRepository;

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
		return new MongoItemsRepository(this, database.getCollection("items"));
	}

	@Override
	public UsersRepository getUsersRepository() {
		return new MongoUsersRepository(this, database.getCollection("users"));
	}
	
	@Override
	public MaterialsRepository getMaterialsRepository() {
		return new MongoMaterialsRepository(this, database.getCollection("materials"));
	}
}
