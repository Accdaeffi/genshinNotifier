package ru.dnoskov.util.filework;

import java.io.File;
import java.io.IOException;
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
	
	public File readFileFromDirectory(String directory, String fileName) {
		
		URI fullFilePath;
		try {	
			//log.info("Reading from " + directory + fileName);
			
			fullFilePath = ClassLoader.getSystemResource(directory+fileName).toURI();
			
			//log.info("Photo " + directory + fileName + " readed!");
			
			return Paths.get(fullFilePath).toFile();	
		} catch (URISyntaxException e) {
			log.error(e);
			return null;
		}
	
	}
}
