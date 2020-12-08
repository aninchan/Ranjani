package com.assign.demo.config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assign.demo.dto.Text;

/**
 * @author ranjani
 *
 *         This class is used to handle the xml construction logic.
 *
 */
@Component
public class XmlUtil {

	private static final Logger LOGGER = LogManager.getLogger(XmlUtil.class);

	@Autowired
	private ApplicationConfigDto applicationConfigDto;

	/**
	 * This method is used to construct the xml file from the domain object Text.
	 * 
	 * @param text
	 * @throws FileParserException
	 */
	public void marshal(Text text) throws FileParserException {

		LOGGER.debug("Entering the marshal() method in XmlUtil");
		String xmlOutputFilePath = applicationConfigDto.getXmlOutputFilePath() + "\\output.xml";

		if (xmlOutputFilePath.trim().isEmpty()) {
			throw new FileParserException("Output File Path is empty");
		}

		boolean generateXml = false;

		if (applicationConfigDto.getGenXml() == 1) {
			generateXml = true;
		}

		if (generateXml) {
			try (FileWriter fileWriter = new FileWriter(xmlOutputFilePath); StringWriter sw = new StringWriter()) {

				JAXBContext jaxbContext = JAXBContext.newInstance(Text.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(text, sw);

				fileWriter.append(sw.toString());

				LOGGER.debug("Exiting the marshal() method in XmlUtil");
				LOGGER.debug("Completed the xml file generation in the path--" + xmlOutputFilePath);
			} catch (IOException e) {
				LOGGER.error("Error occurred while generating XML file");
				System.out.println("Error occurred while generating XML file " + e.getMessage());
			} catch (JAXBException e) {
				LOGGER.error("Error occurred while generating XML file");
				System.out.println("Error occurred while generating XML file " + e.getMessage());
			}
		}
	}

}
