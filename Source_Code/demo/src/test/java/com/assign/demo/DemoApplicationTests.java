package com.assign.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.assign.demo.config.ApplicationConfigDto;
import com.assign.demo.config.XmlUtil;
import com.assign.demo.service.FileParserServiceImpl;

public class DemoApplicationTests {

	@InjectMocks
	private FileParserServiceImpl fileParserServiceImpl;

	@Mock
	private ApplicationConfigDto applicationConfigDto;

	@Mock
	private XmlUtil xmlUtil;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testEmptyInputFilePath() {
		try {
			Mockito.when(applicationConfigDto.getInputFilePath()).thenReturn("");
			fileParserServiceImpl.parseFileContent();
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Input file path is empty");
		}
	}

	@Test
	public void testEmptyOutputXmlFilePath() {
		try {
			Mockito.when(applicationConfigDto.getXmlOutputFilePath()).thenReturn("");
			xmlUtil.marshal(Mockito.any());
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Output File Path is empty");
		}
	}

}
