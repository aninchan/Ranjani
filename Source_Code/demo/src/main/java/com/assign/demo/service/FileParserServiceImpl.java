package com.assign.demo.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assign.demo.config.ApplicationConfigDto;
import com.assign.demo.config.FileParserException;
import com.assign.demo.config.XmlUtil;
import com.assign.demo.dto.Sentence;
import com.assign.demo.dto.Text;

/**
 * @author ranjani
 * 
 *         This class is used to parse the text file and convert it to xml/csv.
 *
 */
@Service
public class FileParserServiceImpl implements FileParserService {

	private static final Logger LOGGER = LogManager.getLogger(FileParserServiceImpl.class);

	public final static String REGULAR_EXPRESSION = "(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?)\\s";

	@Autowired
	private ApplicationConfigDto applicationConfigDto;

	@Autowired
	private XmlUtil xmlUtil;

	String header = " ";

	/**
	 *
	 * Method to read the input file and parse it to construct Text object which
	 * contains sentences and words.
	 * 
	 * @return Text object
	 */
	@Override
	public Text parseFileContent() throws FileParserException {

		LOGGER.debug("Entering parseFileContent() method");

		String inputfilePath = applicationConfigDto.getInputFilePath();

		Text text = null;

		try {
			if (inputfilePath.trim().isEmpty()) {
				throw new FileParserException("Input file path is empty");
			}

			List<String> lines = Files.readAllLines(Paths.get(inputfilePath));

			List<String> sentences = getSentences(lines);

			Map<Sentence, List<String>> sentenceMap = getWords(sentences);

			text = buildTextPojo(sentenceMap);

		} catch (IOException e) {
			LOGGER.debug("Error occurred while parsing the file in parseFileContent() " + e.getMessage());
			System.out.println("Error occurred while parsing the file in parseFileContent() " + e.getMessage());
		}

		LOGGER.info("Exiting parseFileContent() method");

		return text;

	}

	/**
	 * Method is used to generate the Output file in CSV/XML based on the
	 * configuration.
	 * 
	 * @param text
	 */
	@Override
	public void generateOutputFile(Text text) {

		LOGGER.debug("Entering generateOutputFile() method");

		try {
			boolean generateXml = false;

			if (applicationConfigDto.getGenXml() == 1) {
				generateXml = true;
			}

			if (generateXml) {
				xmlUtil.marshal(text);
			}

			boolean generateCsv = false;

			if (applicationConfigDto.getGenCsv() == 1) {
				generateCsv = true;
			}

			if (generateCsv) {
				generateOutputInCSV(text.getSentences());
			}

		} catch (FileParserException e) {
			LOGGER.error("Error occurred while generating the file in generateOutputFile() " + e.getMessage());
			System.out.println("Error occurred while generating the file in generateOutputFile() " + e.getMessage());
		}
		LOGGER.debug("Exiting generateOutputFile() method");
	}

	/**
	 * This method is used to create sentences from lines.
	 * 
	 * @param lines
	 * @return collection of sentences.
	 */
	private List<String> getSentences(List<String> lines) {

		LOGGER.debug("Entering getSentences() method");

		List<String> formattedLines = new ArrayList<String>();
		List<String> formattedSentence = new ArrayList<String>();
		String emptyChar = "";

		for (String line : lines) {
			String sentenceArray[] = line.split(REGULAR_EXPRESSION);
			for (String sentence : sentenceArray) {
				formattedLines.add(sentence);
			}
		}

		for (String line : formattedLines) {

			if (!line.trim().isEmpty()) {
				if (!emptyChar.trim().isEmpty()) {
					line = emptyChar + line;
					emptyChar = "";
				}

				if (!(line.endsWith(".") || line.endsWith("!") || line.endsWith("?"))) {
					emptyChar = line;
				} else {
					formattedSentence.add(line);
				}
			}
		}

		LOGGER.debug("Exiting getSentences() method");
		return escapeSpecialCharacters(formattedSentence);
	}

	/**
	 * Method is used to remove the special characters from the sentences.
	 * 
	 * @param sentences
	 * @return sentences with special characters removed.
	 */
	private List<String> escapeSpecialCharacters(List<String> sentences) {

		LOGGER.debug("Entering the escapeSpecialCharacters()");

		sentences = sentences.stream().map(sentence -> {
			sentence = sentence.trim();
			sentence = sentence.replace(",", " ");
			sentence = sentence.replace("  ", " ");
			sentence = sentence.replace(":", "");
			sentence = sentence.replace("(", "");
			sentence = sentence.replace(" - ", " ");
			sentence = sentence.replace(")", "");
			sentence = sentence.replace("\t", " ");
			sentence = sentence.replace("\\s", " ");

			return sentence;

		}).collect(Collectors.toList());

		LOGGER.debug("Exiting the escapeSpecialCharacters()");
		return sentences;
	}

	/**
	 * Method used to get words from the sentence.
	 * 
	 * @param sentences
	 * @return Map with KEY-SentenceName;VALUE-List of words for the particular
	 *         sentence.
	 */
	private Map<Sentence, List<String>> getWords(List<String> sentences) {
		LOGGER.debug("Entering the getWords()");

		Map<Sentence, List<String>> sentenceMap = new LinkedHashMap<>();
		int sentenceNumber = 1;
		for (String sentence : sentences) {
			List<String> wordsList = new ArrayList<String>();
			String[] words = sentence.split(" ");
			for (String wordAr : words) {
				if (wordAr.length() != 0) {
					wordsList.add(wordAr);
				}
			}
			wordsList = wordsList.stream().sorted((word1, word2) -> word1.compareToIgnoreCase(word2))
					.collect(Collectors.toList());

			sentenceMap.put(new Sentence("Sentence " + sentenceNumber), wordsList);

			sentenceNumber++;
		}
		LOGGER.debug("Exiting the getWords()");
		return sentenceMap;
	}

	/**
	 * This method is used to build the Text Domain from the input map.
	 * 
	 * @param sentenceMap
	 * @return Text object
	 */
	private Text buildTextPojo(Map<Sentence, List<String>> sentenceMap) {
		LOGGER.debug("Entering the buildTextPojo()");
		Text text = new Text();
		for (Map.Entry<Sentence, List<String>> entry : sentenceMap.entrySet()) {
			Sentence sentence = new Sentence();
			sentence.setSentenceName(entry.getKey().getSentenceName());
			sentence.setWords(entry.getValue());
			text.addSentences(sentence);
		}
		LOGGER.debug("Exiting the buildTextPojo()");
		return text;
	}

	/**
	 * Method is used to generate the Output in CSV file format.
	 * 
	 * @param sentenceList
	 */
	private void generateOutputInCSV(List<Sentence> sentenceList) {

		LOGGER.debug("Entering the generateOutputInCSV()");
		String csvOutputFilePath = applicationConfigDto.getCsvOutputFilePath() + "\\output.csv";

		try (FileWriter fileWriter = new FileWriter(new File(csvOutputFilePath));
				BufferedWriter writer = new BufferedWriter(fileWriter)) {

			IntStream stream = IntStream.range(1, 50);
			stream.forEach(i -> {
				header = header + "," + "Word " + i;
			});

			writer.write(header);
			writer.newLine();

			sentenceList.forEach(sentence -> {
				String sentenceName = sentence.getSentenceName();
				String words = sentence.getWords().stream().collect(Collectors.joining(","));
				try {
					writer.write(sentenceName + "," + words);
					writer.newLine();
				} catch (IOException e) {
					LOGGER.error("Error generating CSV output files in generateOutputInCSV()" + e.getMessage());
					System.out.println("Error occurred while creating CSV file " + e.getMessage());
				}
			});

		} catch (IOException e) {
			LOGGER.error("Error generating CSV output files in generateOutputInCSV()" + e.getMessage());
			System.out.println("Error occurred while creating CSV file " + e.getMessage());
		}
		LOGGER.debug("Exiting the generateOutputInCSV() -Successfully generated CSV Output file in the path  "
				+ csvOutputFilePath);
		System.out.println("Successfully generated CSV Output file in  " + csvOutputFilePath);
	}

}
