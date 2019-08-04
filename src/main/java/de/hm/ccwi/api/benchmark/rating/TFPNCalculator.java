package de.hm.ccwi.api.benchmark.rating;

import java.util.List;
import java.util.StringTokenizer;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.rating.dto.RatingLog;

public interface TFPNCalculator {

	public String calcTP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, ResponseEntry foundEntry);

	public void calcFP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			ResponseEntry foundEntry);

	public String calcFN(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, List<String> loweredFoundEntryStringList);
	
	public void calcTN(RatingLog resultOfRatedAPI, StringTokenizer tokenizedText);
}
