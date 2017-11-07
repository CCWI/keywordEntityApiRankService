package de.hm.ccwi.api.benchmark.api;

import org.json.simple.parser.ParseException;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author Marcel
 * @project twitterDataExtraction
 * @email mk@mkarrasch.de
 * @createdOn 26.11.2016
 * @package de.mk.twitterDataExtraction.API
 */

public interface InterfaceAPI {

	/**
	 * Add a result an overall rating of analyzed Messages during a single runtime.
	 */
	public void addSessionRating(Double result);

	/**
	 * Resturns the overall rating of the current session.
	 * 
	 * @return
	 */
	public Double getOverallRating();

	/**
	 * Create a HTTP POST which consits of two or more parameters. Number of
	 * parameters depends on the used API.
	 *
	 * @param message
	 *            Message, which should be posted
	 * @throws UnsupportedEncodingException
	 *             if text is not in Unicode
	 */
	public void createPOST(String message) throws UnsupportedEncodingException;

	/**
	 * Extract all Entities and the confidence Level from the received JSON file.
	 * Save all Enties in a seperate responseEntity. All entities will be saved in
	 * the ArrayList entityList. entityList is ordered from A to Z
	 *
	 * @throws IOException
	 *             IO Error
	 * @throws ParseException
	 *             Parse Error
	 */
	public void receiveGET() throws IOException, ParseException;

	/**
	 * 
	 * @return
	 */
	public Integer getNumberFoundEntries();

	/**
	 * 
	 * @return
	 */
	public List<ResponseEntry> getFoundEntryList();
	
	/**
	 * 
	 * @throws IOException
	 */
	public void executePOST() throws IOException;

	/**
	 * 
	 * @param list
	 * @param list2
	 * @return
	 */
	public double calculateRating(String message, List<String> list, List<String> list2);
	
}
