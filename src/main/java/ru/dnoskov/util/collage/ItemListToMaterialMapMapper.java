package ru.dnoskov.util.collage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.dnoskov.database.dao.Item;
import ru.dnoskov.database.dao.Material;

public class ItemListToMaterialMapMapper {

	static Map<Material, List<Item>> map(List<Item> items) {
		Map<Material, List<Item>> itemsSortedByMaterials = 
				new TreeMap<Material, List<Item>>(
						(d1, d2) -> d1.getSortingPriority()-d2.getSortingPriority());

		for (Item item : items) {
			Material material = item.getMaterial();
			if (itemsSortedByMaterials.containsKey(material)) {
				itemsSortedByMaterials.get(material).add(item);
			} else {
				List<Item> materialItems = new ArrayList<Item>();
				materialItems.add(item);
				itemsSortedByMaterials.put(material, materialItems);
			}
		}
		
		return itemsSortedByMaterials;
	}
	
}
