package de.hm.ccwi.api.benchmark.rating;

import java.util.List;
import java.util.StringTokenizer;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.rating.dto.RatingLog;

public class TFPNFuzzyCalculator implements TFPNCalculator {

	public String calcTP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, ResponseEntry foundEntry) {
		for (String expectedKE : distinctLoweredExpectedKEList) {
			if (expectedKE.contains(foundEntry.getEntry().toLowerCase())
					&& loweredSearchText.contains(foundEntry.getEntry().toLowerCase())) {
				resultOfRatedAPI.incrementTp(1);
				loweredSearchText = loweredSearchText.replace(expectedKE.toLowerCase(), "");
			}
		}
		return loweredSearchText;
	}

	public void calcFP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			ResponseEntry foundEntry) {
		Boolean matchingEntry;
		matchingEntry = false;
		for (String dLExpectedKE : distinctLoweredExpectedKEList) {
			if (dLExpectedKE.contains(foundEntry.getEntry().toLowerCase())) {
				matchingEntry = true;
				break;
			}
		}
		if (!matchingEntry) {
			resultOfRatedAPI.incrementFp(1);
		}
	}

	public String calcFN(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, List<String> loweredFoundEntryStringList) {
		Boolean matchingEntry;
		for (String dLExpectedKE : distinctLoweredExpectedKEList) {
			matchingEntry = false;
			for (String foundEntry : loweredFoundEntryStringList) {
				if (foundEntry.contains(dLExpectedKE) || dLExpectedKE.contains(foundEntry)) {
					matchingEntry = true;
					break;
				}
			}
			if (!matchingEntry) {
				resultOfRatedAPI.incrementFn(1);
				loweredSearchText = loweredSearchText.replace(dLExpectedKE.toLowerCase(), "");
			}
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
