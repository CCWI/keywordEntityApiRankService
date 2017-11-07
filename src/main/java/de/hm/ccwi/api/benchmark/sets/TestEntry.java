package de.hm.ccwi.api.benchmark.sets;

import java.util.List;

public class TestEntry {

	/**
	 * Message, which needs to be analyzed.
	 */
	private String post;

	/**
	 * Entity List of the manual tagging.
	 */
	private List<String> expectedEntityList;

	/**
	 * Keyword List of the manual tagging.
	 */
	private List<String> expectedKeywordList;

	/**
	 * Default-Constructor
	 */
	public TestEntry() {
		super();
	}

	/**
	 * 
	 * @param post
	 * @param expectedEntityList
	 * @param expectedKeywordList
	 */
	public TestEntry(String post, List<String> expectedEntityList, List<String> expectedKeywordList) {
		super();
		this.post = post;
		this.expectedEntityList = expectedEntityList;
		this.expectedKeywordList = expectedKeywordList;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
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

	@Override
	public String toString() {
		return "TestEntry [post=" + post + ", expectedEntityList=" + expectedEntityList + ", expectedKeywordList="
				+ expectedKeywordList + "]";
	}
}
