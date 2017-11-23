package de.hm.ccwi.api.benchmark.rating;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.rating.dto.EntityKeywordLog;
import de.hm.ccwi.api.benchmark.rating.dto.RatingLog;

public class RatingAlgorithmTest {

	List<String> apiMockList = new ArrayList<>();
	List<EntityKeywordLog> ekLogList = new ArrayList<>();
	
	@Before
	public void initializeTest() {
		String apiName = "TestAPI";
		
		apiMockList.add(apiName);
		
		List<String> expectedEntityList = new ArrayList<>();
		expectedEntityList.add("Cpp");
		expectedEntityList.add("IoT");
		List<String> expectedKeywordList = new ArrayList<>();
		expectedKeywordList.add("Amazing coding");
		expectedKeywordList.add("Beautiful websites");
		expectedKeywordList.add("Digital agency");
		EntityKeywordLog ekLog = new EntityKeywordLog(
	"Are you drawing beautiful websites at your digital agency? Want to create on top of amazing coding? #IoT #cpp", apiName, expectedEntityList, expectedKeywordList);
		
		List<ResponseEntry> responseEntryList = new ArrayList<>();
		String[] foundEntries = new String[] {"Cpp", "IoT", "websites", "agency", "top"};
				
		for(String entry : foundEntries) {
			ResponseEntry rEntry = new ResponseEntry();
			rEntry.setEntry(entry);
			responseEntryList.add(rEntry);			
		}
		
		ekLog.setFoundEntryList(responseEntryList);
		ekLogList.add(ekLog);
	}
	
	
	/**
	 * Calculation of Rating by analyzing line by line.
	 *
	 * TP: +1 Für jede korrekt gefundene Entität/Keyword im gesamten Goldstandard.
	 * True positiv: Ein Wort das im Goldstandard als Entität (p) gekennzeichnet
	 * ist, wird auch durch die API als Entität (p) identifiziert.
	 * 
	 * FN: -1 Für alle falschen Entitäten/Keywords im gesamten Goldstandard. False
	 * positiv: Ein Wort das im Goldstandard NICHT (n) als Entität gekennzeichnet
	 * ist, wird durch die API als Entität (p) identifiziert. (GS not | RESULT found)
	 * 
	 * FP: +1 Für jedes andere Wort aus dem gesamten Goldstandard, das
	 * korrekterweise nicht als Entität/Keyword klassifiziert wurde. True negativ:
	 * Ein Wort das im Goldstandard NICHT (n) als Entität gekennzeichnet wird auch
	 * NICHT (n) durch die API als Entität identifiziert. [Satz - (TP + )]       FP = (GS not | RESULT found)
	 * 
	 * TN: -1 Für alle nicht gefundenen Entitäten/Keywords im gesamten Goldstandard.
	 * False negativ: Ein Wort das im Goldstandard als Entität (p) gekennzeichnet
	 * ist wird NICHT (n) durch die API als Entität identifiziert. (GS ok | RESULT not)
	 * 
	 * FP <=> FN
	 * TN <=> FN
	 * 
	 * TP: +1 Für jede korrekt gefundene Entität/Keyword im gesamten Goldstandard.
	 * True positiv: Ein Wort das im Goldstandard als Entität (p) gekennzeichnet
	 * ist, wird auch durch die API als Entität (p) identifiziert.
	 * 
	 * FP: -1 Für alle falschen Entitäten/Keywords im gesamten Goldstandard. False
	 * positiv: Ein Wort das im Goldstandard NICHT (n) als Entität gekennzeichnet
	 * ist, wird durch die API als Entität (p) identifiziert. (GS not | RESULT found)
	 * 
	 * TN: +1 Für jedes andere Wort aus dem gesamten Goldstandard, das
	 * korrekterweise nicht als Entität/Keyword klassifiziert wurde. True negativ:
	 * Ein Wort das im Goldstandard NICHT (n) als Entität gekennzeichnet wird auch
	 * NICHT (n) durch die API als Entität identifiziert. [Satz - (TP + )]
	 * 
	 * FN: -1 Für alle nicht gefundenen Entitäten/Keywords im gesamten Goldstandard.
	 * False negativ: Ein Wort das im Goldstandard als Entität (p) gekennzeichnet
	 * ist wird NICHT (n) durch die API als Entität identifiziert. (GS ok | RESULT not)
	 * 
	 * 
	 * Berechnung der Accuracy (Robustheit) = (TP+TN) / (TP+TN+FP+FN).
	 * Berechnung der Genauigkeit (precision P) = TP / (TP+FP).
	 * Berechnung der Trefferquote (recall R) = TP / (TP+FN).
	 * Berechnung des harmonischen Mittels (F1-Wert) = 2* ((P*R)/(P+R)).
	 * 
	 * Beispiel: Anzahl Entitäten im gesamten Goldstandard = 151. 
	 * Anzahl Nicht Entitäten (alle anderen Wörter) = 324. 
	 * Watson NLU = TP = 112; FN = 23; FP = 301; TN = 39. 
	 * P = 112/(112+301) = 0,271. R = 112/(112+23) = 0,829. 
	 * F1 = 2*((0,271*0,829)/(112+23)) = 0,4084.
	 * 
	 * Wörter gesamt: 19
	 * Anzahl Entitäten im gesamten Goldstandard = 6 
	 * Anzahl Nicht Entitäten (alle anderen Wörter) = 13 
	 * TP = 2; 
	 * FN = 3; 
	 * FP = 11; 
	 * TN = 3.
	 * 
	 * not-fuzzy:
	 * P = 2/(2+11) = 0,153846153846153. 
	 * R = 2/(2+3) = 0,4. 
	 * F1 = 2*((0,153846153846153*0,4)/(0,153846153846153+0,4)) = 0.2222222222222222.
	 * 
	 */
	@Test
	public void rateFoundEntriesOfAPITest() {
		RatingCalculator rate = new RatingCalculator(false);
		
		List<RatingLog> resultLogList = rate.rateFoundEntriesOfAPI(apiMockList, ekLogList);
		
		for(RatingLog resultLog : resultLogList) {
			System.out.println(resultLog.toString());
			Assert.assertTrue(resultLog.getF1() == 0.2222222222222222);
		}
	}
	
	/**
	 * Wörter gesamt: 19
	 * Anzahl Entitäten im gesamten Goldstandard = 6 
	 * Anzahl Nicht Entitäten (alle anderen Wörter) = 13 
	 * TP = 4; (1+1+1+1) //KEINE 0,5 
	 * FN = 1; 
	 * FP = 11; 
	 * TN = 1; 
	 * 
	 * fuzzy:
	 * P = 4/(4+11) = 0,2142857
	 * R = 4/(4+1) = 0,75
	 * F1 = 2*((0,2142857*0,75)/(0,2142857+0,75)) = 0,333333316
	 * 
	 */
	@Test
	public void rateFoundEntriesOfAPIFuzzyTest() {
		RatingCalculator rate = new RatingCalculator(true);
		
		List<RatingLog> resultLogList = rate.rateFoundEntriesOfAPI(apiMockList, ekLogList);
		
		for(RatingLog resultLog : resultLogList) {
			System.out.println(resultLog.toString());
			Assert.assertTrue(resultLog.getF1() == 0.4);
		}
	}
}
