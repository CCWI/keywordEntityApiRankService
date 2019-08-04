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

	public void executePOST() throws IOException {
		try {
			HttpResponse response = httpclient.execute(httppost);
			httpEntity = response.getEntity();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

	double convertRelevance(String relevance) {
		return Double.parseDouble(relevance.replace("\"", ""));
	}

	public Integer getNumberFoundEntries() {
		return foundEntryList.size();
	}

	protected String addEntity(String s) {
		s = s.replace("#", "");
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
