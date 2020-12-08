package com.assign.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author ranjani
 *
 *         Configuration class to load all the properties from
 *         application.properties
 *
 */
@Configuration
public class ApplicationConfigDto {

	@Value("${text.file.path}")
	private String inputFilePath;

	@Value("${xml.file.path}")
	private String xmlOutputFilePath;

	@Value("${csv.file.path}")
	private String csvOutputFilePath;

	@Value("${generateXml}")
	private int genXml;

	@Value("${generateCsv}")
	private int genCsv;

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public String getXmlOutputFilePath() {
		return xmlOutputFilePath;
	}

	public void setXmlOutputFilePath(String xmlOutputFilePath) {
		this.xmlOutputFilePath = xmlOutputFilePath;
	}

	public String getCsvOutputFilePath() {
		return csvOutputFilePath;
	}

	public void setCsvOutputFilePath(String csvOutputFilePath) {
		this.csvOutputFilePath = csvOutputFilePath;
	}

	public int getGenXml() {
		return genXml;
	}

	public void setGenXml(int genXml) {
		this.genXml = genXml;
	}

	public int getGenCsv() {
		return genCsv;
	}

	public void setGenCsv(int genCsv) {
		this.genCsv = genCsv;
	}

}
