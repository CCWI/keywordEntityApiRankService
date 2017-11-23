package de.hm.ccwi.api.benchmark.rating.dto;

import java.util.List;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;

public class EntityKeywordLog {

	private String apiName;

	private String text;

	private List<String> expectedEntityList;

	private List<String> expectedKeywordList;

	/**
	 * List of all found Entities and Keywords - In case of Watson, Entity- and
	 * Keyword-Lists are concatenated.
	 */
	private List<ResponseEntry> foundEntryList;

	public EntityKeywordLog(String apiName, String text, List<String> expectedEntityList, List<String> expectedKeywordList) {
		super();
		this.apiName = apiName;
		this.text = text;
		this.expectedEntityList = expectedEntityList;
		this.expectedKeywordList = expectedKeywordList;
	}

	public synchronized void addToExpectedEntityList(String entity) {
		this.expectedEntityList.add(entity);
	}

	public synchronized void addToExpectedKeywordList(String keyword) {
		this.expectedKeywordList.add(keyword);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getExpectedEntityList() {
		return expectedEntityList;
	}

	public void setExpectedEntityList(List<String> expectedEntityList) {
		this.expectedEntityList = expectedEntityList;
	}

	public List<String> getExpectedKeywordList() {
		return expectedKeywordList;
	}

	public void setExpectedKeywordList(List<String> expectedKeywordList) {
		this.expectedKeywordList = expectedKeywordList;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public List<ResponseEntry> getFoundEntryList() {
		return foundEntryList;
	}

	public void setFoundEntryList(List<ResponseEntry> foundEntryList) {
		this.foundEntryList = foundEntryList;
	}

	@Override
	public String toString() {
		return "EntityKeywordLog [apiName=" + apiName + ", text=" + text + ", expectedEntityList=" + expectedEntityList
				+ ", expectedKeywordList=" + expectedKeywordList + ", foundEntryList=" + foundEntryList + "]";
	}

}
