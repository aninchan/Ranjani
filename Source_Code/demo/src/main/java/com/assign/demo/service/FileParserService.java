package com.assign.demo.service;

import org.springframework.stereotype.Component;

import com.assign.demo.config.FileParserException;
import com.assign.demo.dto.Text;

/**
 * @author ranjani
 *
 *Interface to handle all file parsing functionalities.
 *
 */
@Component
public interface FileParserService {
	
	Text parseFileContent() throws FileParserException;
	
	void generateOutputFile(Text text);

}
