package ru.dnoskov.util.collage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import ru.dnoskov.database.dao.Item;
import ru.dnoskov.database.dao.Material;
import ru.dnoskov.util.Util;
import ru.dnoskov.util.collage.rows.CollageRows;
import ru.dnoskov.util.filework.FileReader;

public class CollageMaker {
	
	private static final String SEPARATOR_FILE_NAME = "line.png";
	
	private static final Color BACKGROUND_COLOR = new Color(45, 50, 90);
	
	private static final int PICTURE_WIDTH = 1056;

	public InputStream makeCollage(int weekDay, List<Item> items) throws IOException {
		String headerFileName = Util.GetHeaderFileNameByDay(weekDay);
		String tomorrowFileName = Util.GetTomorrowFileNameByDay(weekDay);
		
		Map<Material, List<Item>> itemsSortedByMaterials = ItemListToMaterialMapMapper.map(items);
		
		FileReader reader = new FileReader();
		File headerFile = reader.readFileFromDirectory(Constants.Paths.PATH_TO_BACKGROUNDS, headerFileName);
		File tomorrowFile = reader.readFileFromDirectory(Constants.Paths.PATH_TO_BACKGROUNDS, tomorrowFileName);
		File separatorFile = reader.readFileFromDirectory(Constants.Paths.PATH_TO_BACKGROUNDS, SEPARATOR_FILE_NAME);
		
		BufferedImage header = ImageIO.read(headerFile);
		BufferedImage tomorrow = ImageIO.read(tomorrowFile);
		BufferedImage separator = ImageIO.read(separatorFile);
		
		CollageRows rowMaker = new CollageRows(separator); 
		
		int mainPartHeight = rowMaker.calcMainPartHeight(itemsSortedByMaterials);
		int pictureHeight = header.getHeight() + mainPartHeight + tomorrow.getHeight();
		
		BufferedImage buffer = new BufferedImage(PICTURE_WIDTH, pictureHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = buffer.createGraphics();
		
		int currentX = 0;
		int currentY = 0;

		graphics.setPaint(BACKGROUND_COLOR);
		graphics.fillRect(currentX, currentY, buffer.getWidth(), buffer.getHeight());
		
		graphics.drawImage(header, currentX, currentY, null);
		
		currentX = 0;
		currentY = header.getHeight();
		
		rowMaker.addItemsAndMaterials(graphics, itemsSortedByMaterials, currentX, currentY);
		
		currentX = 0;
		currentY = header.getHeight() + mainPartHeight;
		
		graphics.drawImage(tomorrow, currentX, currentY, null);
	
		ByteArrayOutputStream os = new ByteArrayOutputStream();		
		ImageIO.write(buffer,"jpeg", os);
		
		return new ByteArrayInputStream(os.toByteArray());

	}

}
