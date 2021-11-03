package main;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import lombok.Getter;
import lombok.Setter;

public class Bot extends TelegramLongPollingBot {
	
	@Setter
	private String BOT_NAME;
	
	@Setter 
	private String BOT_TOKEN;
	
	@Setter
	@Getter
	private MongoDatabase database;
	
	
	Bot(String botName, String botToken, String dbUser, String dbPass) {
		this.setBOT_NAME(botName);
		this.setBOT_TOKEN(botToken);
		
		ConnectionString connectionString = new ConnectionString("mongodb+srv://"+dbUser+":"+dbPass+"@dnoskov.emlnn.mongodb.net/DNoskov?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder()
		        .applyConnectionString(connectionString)
		        .build();
		MongoClient mongoClient = MongoClients.create(settings);
		this.setDatabase(mongoClient.getDatabase("genshinNotifier"));
	}

	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		
	}

	public String getBotUsername() {
		return BOT_NAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

}
