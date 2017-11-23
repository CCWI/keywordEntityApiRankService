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
	
	/**
	 * Calculation of Rating by analyzing line by line.
	 * 
	 * @param ratingLog
	 * @return
	 */
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
	
	/**
	 * TP: +1 Für jede korrekt gefundene Entität/Keyword im gesamten Goldstandard.
	 * True positiv: Ein Wort das im Goldstandard als Entität (p) gekennzeichnet
	 * ist, wird auch durch die API als Entität (p) identifiziert.
	 * 
	 * FP: -1 Für alle falschen Entitäten/Keywords im gesamten Goldstandard. False
	 * positiv: Ein Wort das im Goldstandard NICHT (n) als Entität gekennzeichnet
	 * ist, wird durch die API als Entität (p) identifiziert. (GS not | RESULT
	 * found)
	 * 
	 * TN: +1 Für jedes andere Wort aus dem gesamten Goldstandard, das
	 * korrekterweise nicht als Entität/Keyword klassifiziert wurde. True negativ:
	 * Ein Wort das im Goldstandard NICHT (n) als Entität gekennzeichnet wird auch
	 * NICHT (n) durch die API als Entität identifiziert. [Satz - (TP + )]
	 * 
	 * FN: -1 Für alle nicht gefundenen Entitäten/Keywords im gesamten Goldstandard.
	 * False negativ: Ein Wort das im Goldstandard als Entität (p) gekennzeichnet
	 * ist wird NICHT (n) durch die API als Entität identifiziert. (GS ok | RESULT
	 * not)
	 * 
	 * 
	 * @param resultOfRatedAPI
	 * @param log
	 * @param distinctLoweredExpectedKEList
	 * @param loweredSearchText
	 */
	public void calculateTFPN(RatingLog resultOfRatedAPI, EntityKeywordLog log,
			List<String> distinctLoweredExpectedKEList, String loweredSearchText) {
		List<String> loweredFoundEntryStringList = new ArrayList<>();

		for (ResponseEntry foundEntry : log.getFoundEntryList()) {
			loweredFoundEntryStringList.add(foundEntry.getEntry().toLowerCase());

			// 1. TP
			loweredSearchText = tfpnCalculator.calcTP(resultOfRatedAPI, distinctLoweredExpectedKEList, loweredSearchText, foundEntry);

			// 2. FP
			tfpnCalculator.calcFP(resultOfRatedAPI, distinctLoweredExpectedKEList, foundEntry);
		}

		// 3. FN
		loweredSearchText = tfpnCalculator.calcFN(resultOfRatedAPI, distinctLoweredExpectedKEList, loweredSearchText,
				loweredFoundEntryStringList);
		// 4. TN
		tfpnCalculator.calcTN(resultOfRatedAPI, new StringTokenizer(loweredSearchText, ".,! ()[]+#@'"));
	}

	/**
	 * Berechnung des harmonischen Mittels (F1-Wert) = 2* ((P*R)/(P+R)).
	 *
	 * @param resultOfRatedAPI
	 * @return
	 */
	private double calculateF1(RatingLog resultOfRatedAPI) {
		return 2 * ((resultOfRatedAPI.getPrecision() * resultOfRatedAPI.getRecall())
				/ (resultOfRatedAPI.getPrecision() + resultOfRatedAPI.getRecall()));
	}

	/**
	 * Berechnung der Trefferquote (recall R) = TP / (TP+FN).
	 * 
	 * @param resultOfRatedAPI
	 * @return
	 */
	private double calculateR(RatingLog resultOfRatedAPI) {
		return resultOfRatedAPI.getTp() / ((double) resultOfRatedAPI.getTp() + resultOfRatedAPI.getFn());
	}

	/**
	 * Berechnung der Genauigkeit (precision P) = TP / (TP+FP).
	 * 
	 * @param resultOfRatedAPI
	 */
	private double calculateP(RatingLog resultOfRatedAPI) {
		return resultOfRatedAPI.getTp() / ((double) resultOfRatedAPI.getTp() + resultOfRatedAPI.getFp());
	}

	/**
	 * Berechnung der Accuracy (Robustheit) = (TP+TN) / (TP+TN+FP+FN).
	 * 
	 * @param resultOfRatedAPI
	 * @return
	 */
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
