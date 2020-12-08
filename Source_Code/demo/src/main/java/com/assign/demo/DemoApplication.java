package com.assign.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.assign.demo.config.FileParserException;
import com.assign.demo.dto.Text;
import com.assign.demo.service.FileParserServiceImpl;

/**
 * @author ranjani
 * 
 *         This is the main class for File Parsing.
 *
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DemoApplication implements CommandLineRunner {

	private static final Logger LOGGER = LogManager.getLogger(DemoApplication.class);

	@Autowired
	private FileParserServiceImpl fileParserServiceImpl;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			LOGGER.debug("Starting the File Parsing");
			Text text = fileParserServiceImpl.parseFileContent();
			fileParserServiceImpl.generateOutputFile(text);
			LOGGER.debug("Completed the File Parsing");
		} catch (FileParserException e) {
			LOGGER.error("File Parser Exception" + e.getMessage());
			System.out.println("File Parser Exception" + e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Exception in Parsing file " + e.getMessage());
			System.out.println("Exception in Parsing file " + e.getMessage());
		}
	}

}
