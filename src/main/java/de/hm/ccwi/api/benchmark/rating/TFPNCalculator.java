package de.hm.ccwi.api.benchmark.rating;

import java.util.List;
import java.util.StringTokenizer;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.rating.dto.RatingLog;

public interface TFPNCalculator {

	/**
	 * @param resultOfRatedAPI
	 * @param distinctLoweredExpectedKEList
	 * @param loweredSearchText
	 * @param foundEntry
	 * @return
	 */
	public String calcTP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, ResponseEntry foundEntry);

	/**
	 * 
	 * @param resultOfRatedAPI
	 * @param distinctLoweredExpectedKEList
	 * @param foundEntry
	 */
	public void calcFP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			ResponseEntry foundEntry);

	/**
	 * 
	 * @param resultOfRatedAPI
	 * @param distinctLoweredExpectedKEList
	 * @param loweredSearchText
	 * @param loweredFoundEntryStringList
	 * @return
	 */
	public String calcFN(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, List<String> loweredFoundEntryStringList);
	
	/**
	 * 
	 * @param resultOfRatedAPI
	 * @param tokenizedText
	 */
	public void calcTN(RatingLog resultOfRatedAPI, StringTokenizer tokenizedText);
}
