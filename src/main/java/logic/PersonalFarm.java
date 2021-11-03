package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import lombok.Getter;
import lombok.Setter;

public class PersonalFarm {

	@Getter
	@Setter
	private Long userId;
	
	@Setter
	@Getter
	private MongoDatabase database;
	
	protected PersonalFarm(Long id,  MongoDatabase database) {
		this.userId = id;
		this.database = database;
	}
	
	protected String getPersonalFarm(int dayOfWeek) {
		String answer;
		
		MongoCollection<Document> users = database.getCollection("users");
		Document user = users.find(Filters.eq("id", userId)).first();
		
		if (user == null || user.getList("items", String.class).isEmpty()) {
			answer = "Я не знаю, по каким предметам тебе нужна информация.";
		} else {
			List<String> userItems = user.getList("items", String.class);
		
			MongoCollection<Document> items = database.getCollection("items");
			List<String> todayUserItems = new LinkedList<>();
			items.find(Filters.and(
							Filters.in("name", userItems), 
							Filters.eq("day", dayOfWeek)))
					   .forEach(item -> todayUserItems.add(item.getString("name")));
			
			if (todayUserItems.isEmpty()) {
				answer = "Сегодня тебе можно ничего не фармить!";
			} else {
				answer = "Сегодня можно фармить ресурсы для: ";
				answer = answer.concat(String.join(", ", todayUserItems));
				answer = answer.concat(".");
			}
		}
		return answer;
	}
	
	protected String add(String itemName) {
		String answer;
		
		if (itemName == null) {
			answer = "Ты не указал, что тебе нужно. Названия, если что - с английского HHW.";
		} else {
			MongoCollection<Document> items = database.getCollection("items");
			if (items.countDocuments(Filters.eq("name", itemName)) == 0) {
				answer = "Ты неправильно указал, что тебе нужно. Названия бери с английского HHW.";
			} else {
				MongoCollection<Document> users = database.getCollection("users");
				
				Document user = users.find(Filters.eq("id", userId)).first();
				if (user == null) {
					user = new Document().append("id", userId)
				  		     			 .append("items", new ArrayList<String>());
					users.insertOne(user);
				}
				
				if (user.getList("items", String.class).contains(itemName)) {
					answer = "Ты уже добавил этот предмет!";
				} else {
					Bson update = Updates.push("items", itemName);
					users.updateOne(Filters.eq("id", userId), update);
					answer = "Успешно добавлено!";
				}
			}
		}
		
		return answer;
	}
	
	protected String del(String itemName) {
		String answer;
		
		if (itemName == null) {
			answer = "Ты не указал, что тебе нужно. Названия, если что - с английского HHW.";
		} else {
			MongoCollection<Document> items = database.getCollection("items");
			if (items.countDocuments(Filters.eq("name", itemName)) == 0) {
				answer = "Ты неправильно указал, что тебе нужно. Названия бери с английского HHW.";
			} else {
				MongoCollection<Document> users = database.getCollection("users");
				
				Document user = users.find(Filters.eq("id", userId)).first();
				if (user == null) {
					user = new Document().append("id", userId)
							  		     .append("items", new ArrayList<String>());
					users.insertOne(user);
				}
				
				if (!user.getList("items", String.class).contains(itemName)) {
					answer = "Нельзя удалить то, что уже удалено!";
				} else {
					Bson update = Updates.pull("items", itemName);
					users.updateOne(Filters.eq("id", userId), update);
					answer = "Успешно удалено!";
				}
			}
		}
		
		return answer;
	}
}
