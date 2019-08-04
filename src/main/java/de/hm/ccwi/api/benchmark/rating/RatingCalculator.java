package de.hm.ccwi.api.benchmark.rating;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.rating.dto.EntityKeywordLog;
import de.hm.ccwi.api.benchmark.rating.dto.RatingLog;

public class RatingCalculator {

	private TFPNCalculator tfpnCalculator;
	
	public RatingCalculator(Boolean searchFuzzy) {
		if(searchFuzzy) {
			tfpnCalculator = new TFPNFuzzyCalculator();
		} else {
			tfpnCalculator = new TFPNStrictCalculator();
		}
	}

	public List<RatingLog> rateFoundEntriesOfAPI(List<String> apiList, List<EntityKeywordLog> entityKeywordLog) {
		List<RatingLog> ratingLogList = new ArrayList<>();

		for (String api : apiList) {
			for (EntityKeywordLog log : entityKeywordLog) {

				RatingLog resultOfRatedAPI = new RatingLog(api, log);
				
				List<String> distinctLoweredExpectedKEList = mapToDistrinctLoweredList(log.getExpectedEntityList(),
						log.getExpectedKeywordList());

				if (log != null && log.getApiName() != null && log.getApiName().equals(api)) {

					String loweredSearchText = log.getText().toLowerCase();

					calculateTFPN(resultOfRatedAPI, log, distinctLoweredExpectedKEList, loweredSearchText);

					resultOfRatedAPI.setAccuracy(calculateAccuracy(resultOfRatedAPI));
					resultOfRatedAPI.setPrecision(calculateP(resultOfRatedAPI));
					resultOfRatedAPI.setRecall(calculateR(resultOfRatedAPI));
					resultOfRatedAPI.setF1(calculateF1(resultOfRatedAPI));

					// fuege Ergebnisse der Iteration der Ergebnisliste hinzu.
					ratingLogList.add(resultOfRatedAPI);

				}
			}
		}
		return ratingLogList;
	}

	public void calculateTFPN(RatingLog resultOfRatedAPI, EntityKeywordLog log,
			List<String> distinctLoweredExpectedKEList, String loweredSearchText) {
		List<String> loweredFoundEntryStringList = new ArrayList<>();

		for (ResponseEntry foundEntry : log.getFoundEntryList()) {
			if(foundEntry.getEntry().contains("@")) {
				foundEntry.setEntry(foundEntry.getEntry().replace("@", ""));
			}
			loweredFoundEntryStringList.add(foundEntry.getEntry().toLowerCase());
			loweredSearchText = tfpnCalculator.calcTP(resultOfRatedAPI, distinctLoweredExpectedKEList, loweredSearchText, foundEntry);
			tfpnCalculator.calcFP(resultOfRatedAPI, distinctLoweredExpectedKEList, foundEntry);
		}

		loweredSearchText = tfpnCalculator.calcFN(resultOfRatedAPI, distinctLoweredExpectedKEList, loweredSearchText,
				loweredFoundEntryStringList);
		tfpnCalculator.calcTN(resultOfRatedAPI, new StringTokenizer(loweredSearchText, ".,! ()[]+#@'"));
	}

	private double calculateF1(RatingLog resultOfRatedAPI) {
		return 2 * ((resultOfRatedAPI.getPrecision() * resultOfRatedAPI.getRecall())
				/ (resultOfRatedAPI.getPrecision() + resultOfRatedAPI.getRecall()));
	}

	private double calculateR(RatingLog resultOfRatedAPI) {
		return resultOfRatedAPI.getTp() / ((double) resultOfRatedAPI.getTp() + resultOfRatedAPI.getFn());
	}

	private double calculateP(RatingLog resultOfRatedAPI) {
		return resultOfRatedAPI.getTp() / ((double) resultOfRatedAPI.getTp() + resultOfRatedAPI.getFp());
	}

	private double calculateAccuracy(RatingLog resultOfRatedAPI) {
		return (resultOfRatedAPI.getTp() + resultOfRatedAPI.getTn()) / ((double) resultOfRatedAPI.getTp()
				+ resultOfRatedAPI.getTn() + resultOfRatedAPI.getFp() + resultOfRatedAPI.getFn());
	}

	private List<String> mapToDistrinctLoweredList(List<String> list1, List<String> list2) {
		List<String> distinctFoundKEList = new ArrayList<>();
		if(list1 != null) {
			distinctFoundKEList = addEntriesToDistinctList(distinctFoundKEList, list1);			
		}
		if(list2 != null) {
			distinctFoundKEList = addEntriesToDistinctList(distinctFoundKEList, list2);
		}
		return distinctFoundKEList;
	}

	private List<String> addEntriesToDistinctList(List<String> distinctKEList, List<String> oldEntryList) {
		for (String entry : oldEntryList) {
			if (!distinctKEList.contains(entry)) {
				distinctKEList.add(entry.toLowerCase());
			}
		}

		return distinctKEList;
	}
}
