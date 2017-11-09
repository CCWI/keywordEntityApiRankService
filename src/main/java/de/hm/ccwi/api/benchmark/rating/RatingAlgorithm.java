package de.hm.ccwi.api.benchmark.rating;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;

public class RatingAlgorithm {

	/**
	 * Calculation of Rating by analyzing line by line.
	 * 
	 * Beispiel: Anzahl Entitäten im gesamten Goldstandard = 151. Anzahl Nicht
	 * Entitäten (alle anderen Wörter) = 324. Watson NLU = TP = 112; FN = 23; FP =
	 * 301; TN = 39. P = 112/(112+301) = 0,271. R = 112/(112+23) = 0,829. F1 =
	 * 2*((0,271*0,829)/(112+23)) = 0,4084.
	 * 
	 * @param ratingLog
	 * @return
	 */
	public List<RatingLog> rateFoundEntriesOfAPI(List<String> apiList, List<EntityKeywordLog> entityKeywordLog,
			Boolean searchFuzzy) {
		List<RatingLog> ratingLogList = new ArrayList<>();

		for (String api : apiList) {

			// new RatingLog for API
			RatingLog resultOfRatedAPI = new RatingLog(api);

			for (EntityKeywordLog log : entityKeywordLog) {
				List<String> distinctLoweredExpectedKEList = mapToDistrinctLoweredList(log.getExpectedEntityList(),
						log.getExpectedKeywordList());

				if (log.getApiName().equals(api)) {

					String loweredSearchText = log.getText().toLowerCase();

					calculateTFPN(resultOfRatedAPI, log, distinctLoweredExpectedKEList, loweredSearchText, searchFuzzy);

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
	 * FN: -1 Für alle falschen Entitäten/Keywords im gesamten Goldstandard. False
	 * positiv: Ein Wort das im Goldstandard NICHT (n) als Entität gekennzeichnet
	 * ist, wird durch die API als Entität (p) identifiziert.
	 * 
	 * FP: +1 Für jedes andere Wort aus dem gesamten Goldstandard, das
	 * korrekterweise nicht als Entität/Keyword klassifiziert wurde. True negativ:
	 * Ein Wort das im Goldstandard NICHT (n) als Entität gekennzeichnet wird auch
	 * NICHT (n) durch die API als Entität identifiziert. [Satz - (TP + )]
	 * 
	 * TN: -1 Für alle nicht gefundenen Entitäten/Keywords im gesamten Goldstandard.
	 * False negativ: Ein Wort das im Goldstandard als Entität (p) gekennzeichnet
	 * ist wird auch NICHT (n) durch die API als Entität identifiziert.
	 * 
	 * @param resultOfRatedAPI
	 * @param log
	 * @param distinctLoweredExpectedKEList
	 * @param loweredSearchText
	 */
	private void calculateTFPN(RatingLog resultOfRatedAPI, EntityKeywordLog log,
			List<String> distinctLoweredExpectedKEList, String loweredSearchText, Boolean fuzzySearch) {
		List<String> loweredFoundEntryStringList = new ArrayList<>();

		for (ResponseEntry foundEntry : log.getFoundEntryList()) {
			loweredFoundEntryStringList.add(foundEntry.getEntry().toLowerCase());

			if (fuzzySearch) {
				loweredSearchText = calcFuzzyTP(resultOfRatedAPI, distinctLoweredExpectedKEList, loweredSearchText,
						foundEntry);
				calcFuzzyFN(resultOfRatedAPI, distinctLoweredExpectedKEList, foundEntry);

			} else {
				loweredSearchText = calcTP(resultOfRatedAPI, distinctLoweredExpectedKEList, loweredSearchText,
						foundEntry);

				// 2. Alle E&K, die nicht vorkamen -> FN++ abgleichen, ob die distinctKEList
				// wörter enthält, die nicht in der distinctExpectedListe vorkommt.
				calcFN(resultOfRatedAPI, distinctLoweredExpectedKEList, foundEntry);
			}
		}

		// 3. Alle gefundenen Einträge aus der Goldstandard-Liste -> TN++ & Lösche diese
		// aus dem Text
		if (fuzzySearch) {
			loweredSearchText = calcFuzzyTN(resultOfRatedAPI, distinctLoweredExpectedKEList, loweredSearchText,
					loweredFoundEntryStringList);
		} else {
			loweredSearchText = calcTN(resultOfRatedAPI, distinctLoweredExpectedKEList, loweredSearchText,
					loweredFoundEntryStringList);

		}
		// 4. Alle Restwörter tokenizen und pro wort das größer als 1 ist FP++
		StringTokenizer tokenizedText = new StringTokenizer(loweredSearchText, ".,! ()[]+#@");

		while (tokenizedText.hasMoreElements()) {
			String s = tokenizedText.nextElement().toString();
			if (s.length() > 1) {
				resultOfRatedAPI.incrementFp(1.0);
			}
		}
	}

	private void calcFuzzyFN(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
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
			resultOfRatedAPI.incrementFn(1.0);
		}
	}

	private void calcFN(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			ResponseEntry foundEntry) {
		if (!distinctLoweredExpectedKEList.contains(foundEntry.getEntry().toLowerCase())) {
			resultOfRatedAPI.incrementFn(1.0);
		}
	}

	private String calcTN(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, List<String> loweredFoundEntryStringList) {
		for (String dLExpectedKE : distinctLoweredExpectedKEList) {
			if (!loweredFoundEntryStringList.contains(dLExpectedKE.toLowerCase())) {
				resultOfRatedAPI.incrementTn(1.0);
			}
			loweredSearchText = loweredSearchText.replace(dLExpectedKE.toLowerCase(), "");
		}
		return loweredSearchText;
	}

	/**
	 * RESULT found, but no Entry in GS
	 * 
	 * @param resultOfRatedAPI
	 * @param distinctLoweredExpectedKEList
	 * @param loweredSearchText
	 * @param loweredFoundEntryStringList
	 * @return
	 */
	private String calcFuzzyTN(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
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
				resultOfRatedAPI.incrementTn(1.0);
				loweredSearchText = loweredSearchText.replace(dLExpectedKE.toLowerCase(), "");
			}
		}
		return loweredSearchText;
	}

	/**
	 * Check for Entities and Keywords on the text -> Delete those if found and
	 * TP++.
	 * 
	 * @param resultOfRatedAPI
	 * @param distinctLoweredExpectedKEList
	 * @param loweredSearchText
	 * @param foundEntry
	 * @return
	 */
	private String calcTP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, ResponseEntry foundEntry) {
		if (loweredSearchText.contains(foundEntry.getEntry().toLowerCase())
				&& distinctLoweredExpectedKEList.contains(foundEntry.getEntry().toLowerCase())) {
			resultOfRatedAPI.incrementTp(1.0);
			loweredSearchText = loweredSearchText.replaceFirst(foundEntry.getEntry().toLowerCase(), "");
		}
		return loweredSearchText;
	}

	/**
	 * Same as calcFuzzyTP, but the search is fuzzy. For example: If "agency"
	 * instead of "digital agency" found, it still counts as TP++.
	 * 
	 * @param resultOfRatedAPI
	 * @param distinctLoweredExpectedKEList
	 * @param loweredSearchText
	 * @param foundEntry
	 * @return
	 */
	private String calcFuzzyTP(RatingLog resultOfRatedAPI, List<String> distinctLoweredExpectedKEList,
			String loweredSearchText, ResponseEntry foundEntry) {
		// for-Schleife um die Suche fuzzy zu machen. (Bspw. wenn "agency" statt
		// "digital agency" gefunden wird, dann +0,5.)
		for (String expectedKE : distinctLoweredExpectedKEList) {
			if (expectedKE.contains(foundEntry.getEntry().toLowerCase())
					&& loweredSearchText.contains(foundEntry.getEntry().toLowerCase())) {
				resultOfRatedAPI.incrementTp(1.0);
				loweredSearchText = loweredSearchText.replace(expectedKE.toLowerCase(), "");
				// break;
			}
		}
		return loweredSearchText;
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
		return ((double) resultOfRatedAPI.getTp() / (resultOfRatedAPI.getTp() + resultOfRatedAPI.getFn()));
	}

	/**
	 * Berechnung der Genauigkeit (precision P) = TP / (TP+FP).
	 * 
	 * @param resultOfRatedAPI
	 */
	private double calculateP(RatingLog resultOfRatedAPI) {
		return ((double) resultOfRatedAPI.getTp() / (resultOfRatedAPI.getTp() + resultOfRatedAPI.getFp()));
	}

	/**
	 * Berechnung der Accuracy (Robustheit) = (TP+TN) / (TP+TN+FP+FN).
	 * 
	 * @param resultOfRatedAPI
	 * @return
	 */
	private double calculateAccuracy(RatingLog resultOfRatedAPI) {
		return (((double) resultOfRatedAPI.getTp() + resultOfRatedAPI.getTn()) / (resultOfRatedAPI.getTp()
				+ resultOfRatedAPI.getTn() + resultOfRatedAPI.getFp() + resultOfRatedAPI.getFn()));
	}

	private List<String> mapToDistrinctLoweredList(List<String> list1, List<String> list2) {
		List<String> distinctFoundKEList = new ArrayList<>();
		distinctFoundKEList = addEntriesToDistinctList(distinctFoundKEList, list1);
		distinctFoundKEList = addEntriesToDistinctList(distinctFoundKEList, list2);
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
