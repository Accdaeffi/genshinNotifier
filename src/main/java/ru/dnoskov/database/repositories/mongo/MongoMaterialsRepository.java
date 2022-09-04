package ru.dnoskov.database.repositories.mongo;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import ru.dnoskov.database.dao.Material;
import ru.dnoskov.database.repositories.MaterialsRepository;
import ru.dnoskov.database.sources.DataSource;
import ru.dnoskov.util.Util;

public class MongoMaterialsRepository implements MaterialsRepository {

	private MongoCollection<Document> materials;
	
	public MongoMaterialsRepository(DataSource dataSource, MongoCollection<Document> materialsCollection) {
		this.materials = materialsCollection;
	}

	@Override
	public Material getMaterialByName(String name) {
		Document materialFromDatabase = materials.find(Filters.eq("name", name)).first();
		return materialFromDocument(materialFromDatabase);
	}

	@Override
	public Material getMaterialByTag(String tag) {
		Document materialFromDatabase = materials.find(Filters.eq("tag", tag)).first();
		return materialFromDocument(materialFromDatabase);
	}
	
	@Override
	public List<Material> getMaterialsByDay(int dayOfWeek) {
		int dayOfFarm = Util.ConvertWeekDayToFarmDay(dayOfWeek);
		LinkedList<Material> allMaterials = new LinkedList<>(); 
		
		materials.find(Filters.eq("day", dayOfFarm))
				.forEach(materialFromDatabase -> allMaterials.add(materialFromDocument(materialFromDatabase)));
		
		return allMaterials;
	}
	
	@Override
	public List<Material> getAllMaterials() {
		LinkedList<Material> allMaterials = new LinkedList<>(); 
		
		materials.find()
				.forEach(materialFromDatabase -> allMaterials.add(materialFromDocument(materialFromDatabase)));
		
		return allMaterials;
	}
	
	private Material materialFromDocument(Document materialFromDatabase) {
		Material material = null;
		
		if (materialFromDatabase != null) {	
			int sortingPosition = materialFromDatabase.getInteger("sortingPosition");
			int day = materialFromDatabase.getInteger("day");
			String name = materialFromDatabase.getString("name");
			String tag = materialFromDatabase.getString("tag");
			String type = materialFromDatabase.getString("type");
			
			material = new Material(sortingPosition, day, name, tag, type);
		}
		
		return material;
	}

	
}
