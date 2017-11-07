package de.hm.ccwi.api.benchmark.readFile;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Marcel
 * @project twitterDataExtraction
 * @email mk@mkarrasch.de
 * @createdOn 26.11.2016
 * @package de.mk.twitterDataExtraction.ReadFile
 */

public class ReadTwitterJSON {

	private String urlPattern1 = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
	private String urlPattern2 = "\\(+((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)\\)";
	private String urlPattern3 = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)+\\)";

	private JSONObject twitterJSON;
	private JSONObject twitterObject;
	private JSONArray twitterArray;
	private ArrayList<String> twitterHastags;
	private String tweet;

	public ReadTwitterJSON(String JSONZeile) throws ParseException {
		JSONParser parser = new JSONParser();

		twitterJSON = (JSONObject) parser.parse(JSONZeile);
		twitterObject = new JSONObject();
		twitterArray = new JSONArray();
		twitterHastags = new ArrayList<>();
		tweet = (String) twitterJSON.get("text");

		twitterObject = (JSONObject) twitterJSON.get("entities");
		twitterArray = (JSONArray) twitterObject.get("hashtags");
	}

	/**
	 * Extract the language if the tweet a Twitter JSON Object
	 *
	 * @return ISO Code of the language (e.g. en or de)
	 * @throws ParseException
	 *             Error during Parsing
	 */
	String extractTweetLanguage() throws ParseException {
		return (String) twitterJSON.get("lang");
	}

	/**
	 * Extract all Hastags from a Twitter JSON Object. All Hastags are included
	 * in entities -> hastags
	 *
	 * @return ArrayList of all String Hastags
	 * @throws ParseException
	 *             Error during Parsing
	 */
	public ArrayList<String> extractTweetHastags() throws ParseException {

		for (int i = 0; i < this.numberOfHastags(); i++) {
			twitterObject = (JSONObject) twitterArray.get(i);
			String s = (String) twitterObject.get("text");
			s = s.substring(0, 1).toUpperCase() + s.substring(1);
			twitterHastags.add(i, s);
		}

		Collections.sort(twitterHastags.subList(0, twitterHastags.size()));

		return twitterHastags;
	}

	/**
	 * Extract the not unique Screenname of the creater of a Twitter Tweet
	 *
	 * @return ScreenName
	 * @throws ParseException
	 *             Error during Parsing
	 */
	String extractTweetScreenName() throws ParseException {
		twitterObject = (JSONObject) twitterJSON.get("user");
		return (String) twitterObject.get("screen_name");
	}

	/**
	 * Extract the unique User ID of the creater of a Twitter Tweet
	 *
	 * @return User ID
	 * @throws ParseException
	 *             Error during Parsing
	 */
	Long extractTweetUserID() throws ParseException {
		twitterObject = (JSONObject) twitterJSON.get("user");
		return (Long) twitterObject.get("id");
	}

	/**
	 * Check if the Tweet is a Re-Tweet (Tweet was originally by somebody else)
	 *
	 * @return User ID
	 */
	public Boolean isRT() {
		return twitterJSON.get("retweeted_status") != null;
	}

	/**
	 * Remove URL from a String http://stackoverflow.com/a/12950893 Additional
	 * Fix: URLs with brackets caused exeptions. Therefore a second urlPattern
	 * is implemented, checking for URLs in brackets first!
	 */
	public void removeUrl() {
		Pattern p1 = Pattern.compile(urlPattern1, Pattern.CASE_INSENSITIVE);
		Pattern p2 = Pattern.compile(urlPattern2, Pattern.CASE_INSENSITIVE);
		// Pattern p3 = Pattern.compile(urlPattern3, Pattern.CASE_INSENSITIVE);
		int i = 0;

		i = replaceByPattern(p2, i);
		// i = replaceByPattern(p3, i);
		i = replaceByPattern(p1, i);
	}

	private Integer replaceByPattern(Pattern pattern, Integer i) {
		Matcher matcher = pattern.matcher(tweet);
//		System.out.println("tweet: " + tweet.toString());
		tweet = tweet.replaceAll("\\)", "").trim();

		while (matcher.find()) {
			tweet = tweet.replaceAll(matcher.group(i), "").trim();
			if (pattern.pattern().equals(urlPattern2)) {
				tweet = tweet.replaceAll("[()]", "").trim();
			}
			i++;
		}
		tweet = tweet.replace("  ", " ");

//		System.out.println("tweet replaced: " + tweet.toString());
		return i;
	}

	/**
	 * Remove any Hastrags # from a Tweet
	 */
	public void removeHastags() {
		tweet = tweet.replace("#", "");
	}

	/**
	 * @return Return the current tweet
	 */
	public String getTweet() {
		return tweet;
	}

	/**
	 * Return number of Hastags of a tweet.
	 *
	 * @return Number of Hastags.
	 */
	public int numberOfHastags() {
		return twitterArray.size();
	}

}
