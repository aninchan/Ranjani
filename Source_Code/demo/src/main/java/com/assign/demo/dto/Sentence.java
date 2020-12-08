package com.assign.demo.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author ranjani
 * 
 *         Domain class for Sentence.
 *
 */
public class Sentence {

	private String sentenceName;

	private List<String> words;

	public Sentence() {
	}

	public Sentence(String sentenceName) {
		super();
		this.sentenceName = sentenceName;
	}

	@XmlTransient
	public String getSentenceName() {
		return sentenceName;
	}

	public void setSentenceName(String sentenceName) {
		this.sentenceName = sentenceName;
	}

	@XmlElement(name = "word", type = String.class)
	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sentenceName == null) ? 0 : sentenceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sentence other = (Sentence) obj;
		if (sentenceName == null) {
			if (other.sentenceName != null)
				return false;
		} else if (!sentenceName.equals(other.sentenceName))
			return false;
		return true;
	}

}
