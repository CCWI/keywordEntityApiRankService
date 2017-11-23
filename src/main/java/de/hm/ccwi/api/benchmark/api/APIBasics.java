package de.hm.ccwi.api.benchmark.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Marcel
 * @project twitterDataExtraction
 * @email mk@mkarrasch.de
 * @createdOn 10.12.2016
 * @package de.mk.twitterDataExtraction.API
 */
public class APIBasics {

	private static Double sessionRating;

	HttpClient httpclient;
	HttpPost httppost;
	HttpEntity httpEntity;
	List<ResponseEntry> foundEntryList;
	Properties properties;
	
	String apiKey;
	String outputMode;
	String topictypes;
	String lang;

	/**
	 * Read the connection.properties file. This file includes all API Keys
	 */
	public APIBasics() {
		foundEntryList = new ArrayList<>();
		properties = new Properties();
		File f = new File("resources/connection.properties");

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
			properties.load(bis);

			if (properties.getProperty("dandelionAPIKey").isEmpty() || properties.getProperty("alchemyAPIKey").isEmpty()
					|| properties.getProperty("meaningCloudAPIKey").isEmpty() || properties.getProperty("watsonAPIUsername").isEmpty()
					|| properties.getProperty("watsonAPIKey").isEmpty()) {
				System.out.println("One or more API Keys / Usernames are not set. Program stopped!");
				System.exit(0);
			}
		} catch (Exception ex) {
			System.out.println("connection.properties not set");
		}
	}

	/**
	 * Execute the created HTTP Post Element and receive the respone
	 *
	 * @throws IOException
	 *             General Error
	 */
	public void executePOST() throws IOException {
		try {
			HttpResponse response = httpclient.execute(httppost);
			httpEntity = response.getEntity();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Extract the JSON Array annotations which contains all Entites
	 *
	 * @param API
	 *            Name of API. Output is different for each API
	 * @param JSONZeile
	 *            Received response from Dandelion API
	 * @return JSONArray annotations which includes all Entites
	 * @throws ParseException
	 *             Parse Error
	 */
	protected JSONArray readResponseJSON(String API, String JSONZeile, String jsonObject) {
		JSONParser parser = new JSONParser();
		JSONObject responeJSON = null;
		try {
			responeJSON = (JSONObject) parser.parse(JSONZeile);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray returnValue = new JSONArray();

		if (responeJSON != null) {
			returnValue = (JSONArray) responeJSON.get(jsonObject);
		}

		return returnValue;

	}

	/**
	 * In case APi return relevance as a String. Convert into double
	 *
	 * @param relevance
	 *            as String
	 * @return relevance as Double
	 */
	double convertRelevance(String relevance) {
		return Double.parseDouble(relevance.replace("\"", ""));
	}

	/**
	 * Number of entities or keywords saved in the array entryList.
	 *
	 * @return Number of found entities or keywords.
	 */
	public Integer getNumberFoundEntries() {
		return foundEntryList.size();
	}

	/**
	 * Check if entity should be added to the entity list. First check if entity is
	 * already in the entity list. Second check if entity is an URL or has more than
	 * 2 spaces
	 *
	 * @param s
	 *            Entity from API
	 * @return String of entity. If null than it will not be added
	 */
	protected String addEntity(String s) {

		// Replace # with nothing in case Entiy has a # sign
		s = s.replace("#", "");
		// Capitalize first letter
		if (s.length() > 2) {
			s = s.substring(0, 1).toUpperCase() + s.substring(1);
		}
		boolean addEntity = true;

		for (ResponseEntry e : foundEntryList)
			if (s.equals(e.getEntry())) {
				addEntity = false;
				s = null;
				break;
			}

		// Check if Entity is an URL. If so do not add to Entity List.
		// Check if entity has more than 2 spaces. If so do not add to Entity
		// List because the entity is a sentance.
		if (s != null && addEntity) {
			if (s.contains("@") || s.contains("http") || s.contains("Http")
					|| s.length() - s.replace(" ", "").length() > 1) {

				return null;
			}
		}
		return s;
	}

	public List<ResponseEntry> getFoundEntryList() {
		return foundEntryList;
	}

	public synchronized void addSessionRating(Double result) {
		APIBasics.sessionRating = APIBasics.sessionRating + result;
	}

	public Double getOverallRating() {
		return APIBasics.sessionRating;
	}
}
