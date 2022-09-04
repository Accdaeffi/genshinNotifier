package ru.dnoskov.util.collage.rows;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import ru.dnoskov.database.dao.Item;
import ru.dnoskov.database.dao.Material;

public class CollageRows {
	
	private static final int ROW_HEIGHT = 70;
	
	private BufferedImage separator;
	
	public CollageRows(BufferedImage separator) {
		this.separator = separator;
	} 
	
	public int calcMainPartHeight(Map<Material, List<Item>> itemsSortedByMaterials) {
		int mainPartHeight = 0;
		
		for (Map.Entry<Material, List<Item>> entry : itemsSortedByMaterials.entrySet()) {			
			Material material = entry.getKey();
			List<Item> materialItems = entry.getValue();
				
			int rows = (int) 
					Math.ceil(((double) materialItems.size()) / Util.getMaximumInTheRow(material.getType()));
			
			mainPartHeight = mainPartHeight + ROW_HEIGHT*rows + separator.getHeight();
		}
		
		return mainPartHeight;
	}
	
	public void addItemsAndMaterials(Graphics2D graphics, 
			Map<Material, List<Item>> itemsSortedByMaterials, 
			int baseX, 
			int baseY) throws IOException {
		CollageRowsAdder.addItemsAndMaterials(graphics, itemsSortedByMaterials, separator,  baseX, baseY);
	}
	
	
}
