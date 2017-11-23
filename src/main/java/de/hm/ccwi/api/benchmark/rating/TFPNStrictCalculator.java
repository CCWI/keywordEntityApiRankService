package de.hm.ccwi.api.benchmark.rating;

import java.util.List;
import java.util.StringTokenizer;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.rating.dto.RatingLog;

public class TFPNStrictCalculator implements TFPNCalculator {
	
	public void calcFP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			ResponseEntry foundEntry) {
		if (!distinctLoweredExpectedKEList.contains(foundEntry.getEntry().toLowerCase())) {
			resultOfRatedAPI.incrementFp(1);
		}
	}

	public String calcFN(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, List<String> loweredFoundEntryStringList) {
		for (String dLExpectedKE : distinctLoweredExpectedKEList) {
			if (!loweredFoundEntryStringList.contains(dLExpectedKE.toLowerCase())) {
				resultOfRatedAPI.incrementTn(1);
			}
			loweredSearchText = loweredSearchText.replace(dLExpectedKE.toLowerCase(), "");
		}
		return loweredSearchText;
	}

	public String calcTP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, ResponseEntry foundEntry) {
		if (loweredSearchText.contains(foundEntry.getEntry().toLowerCase())
				&& distinctLoweredExpectedKEList.contains(foundEntry.getEntry().toLowerCase())) {
			resultOfRatedAPI.incrementTp(1);
			loweredSearchText = loweredSearchText.replaceFirst(foundEntry.getEntry().toLowerCase(), "");
		}
		return loweredSearchText;
	}

	public void calcTN(RatingLog resultOfRatedAPI, StringTokenizer tokenizedText) {
		while (tokenizedText.hasMoreElements()) {
			String s = tokenizedText.nextElement().toString();
			if (s.length() > 1) {
				resultOfRatedAPI.incrementTn(1);
			}
		}
	}
}
