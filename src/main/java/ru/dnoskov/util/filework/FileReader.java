package ru.dnoskov.util.filework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileReader {

	public List<File> readAllFilesFromDirectory(String path) {
		List<File> files = new ArrayList<File>();
	
		URI fullFilePath;
		try {	
			fullFilePath = ClassLoader.getSystemResource(path).toURI();
			
		    try (Stream<Path> stream = Files.list(Paths.get(fullFilePath))) {
		    	
		    	files = stream
		    				.filter(filePath -> !Files.isDirectory(filePath))
		    				.filter(filePath -> filePath.getFileName().toString().endsWith(".jpg"))
		    				.map(filePath -> filePath.toFile())
		    				.collect(Collectors.toList());
		    } catch (IOException e) {
		    	log.error(e);
			}
		    catch (Exception e) {
		    	log.error(e);
		    }
		} catch (URISyntaxException e) {
			log.error(e);
		}
	   
		//log.info("Photos readed!");
		
		return files;
	}
	
	public InputStream readFileFromDirectory(String directory, String fileName) {
		
		try {	
			log.info("Reading from " + directory + fileName);
			
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(directory+fileName);
			//File file = new File(classLoader.getResourceAsStream(directory+fileName)).getFile());
			
			//fullFilePath = ClassLoader.getSystemResource(directory+fileName).toURI();
			
			log.info("Photo readed!");
			
			return inputStream;	
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	
	}
}
