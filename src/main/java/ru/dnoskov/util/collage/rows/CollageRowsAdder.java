package ru.dnoskov.util.collage.rows;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import ru.dnoskov.database.dao.Item;
import ru.dnoskov.database.dao.Material;
import ru.dnoskov.util.collage.Constants;
import ru.dnoskov.util.filework.FileReader;

public class CollageRowsAdder {
	
	private static final int ROW_START_X = 30;
	private static final int ROW_HEIGHT = 70;
	
	private static final int ITEM_START_X = 220;
	private static final int ITEM_HEIGHT = 65;
	private static final int ITEM_PADDING = (ROW_HEIGHT-ITEM_HEIGHT)/2;
	private static final int BETWEEN_ITEM_WIDTH = 15;
	
	static void addItemsAndMaterials(Graphics2D graphics, 
			Map<Material, List<Item>> itemsSortedByMaterials, 
			BufferedImage separator,
			int baseX, 
			int baseY) throws IOException {
		
		FileReader reader = new FileReader();
		
		int currentX = baseX+ROW_START_X;
		int currentY = baseY;
		
		//System.out.println("Current X: " + currentX);
		//System.out.println("Current Y: " + currentY);
		
		for (Map.Entry<Material, List<Item>> entry : itemsSortedByMaterials.entrySet()) {
			Material material = entry.getKey();
			List<Item> materialItems = entry.getValue();
			
			InputStream materialFile = reader.readFileFromDirectory(Constants.Paths.PATH_TO_MATERIALS, material.getTag()+Constants.PHOTO_EXTENSION);
			
			//System.out.println("Current X: " + currentX);
			//System.out.println("Current Y: " + currentY);
			
			BufferedImage materialImage = ImageIO.read(materialFile);
			graphics.drawImage(materialImage, currentX, currentY, null);
			
			currentX = ITEM_START_X;
			
			int maximumInTheRow = Util.getMaximumInTheRow(material.getType());
			
			int totalDrawn = 0;
			int currentInTheRow = 0;	
			
			for (Item item : materialItems) {
				InputStream itemFile = reader.readFileFromDirectory(Constants.Paths.PATH_TO_ITEMS, item.getTag()+Constants.PHOTO_EXTENSION);
				
				//System.out.println("Current X: " + currentX);
				//System.out.println("Current Y: " + currentY);
				
				BufferedImage itemImage = ImageIO.read(itemFile);
				graphics.drawImage(itemImage, currentX, currentY+ITEM_PADDING, null);
				
				currentInTheRow++;
				totalDrawn++;
				
				//System.out.println("CurrentInTheRow: " + currentInTheRow);
				
				if (currentInTheRow >= maximumInTheRow && totalDrawn != materialItems.size()) {
					//System.out.println("Changing row!");
					
					currentInTheRow = 0;
					
					currentX = ITEM_START_X;
					currentY = currentY + ROW_HEIGHT;
				} else {
					currentX = currentX + itemImage.getWidth() + BETWEEN_ITEM_WIDTH;
				}
			}
			
			currentX = ROW_START_X;
			currentY = currentY + ROW_HEIGHT;
			
			graphics.drawImage(separator, currentX, currentY, null);
			
			currentY = currentY + separator.getHeight();

		}
		
	}
	
}
