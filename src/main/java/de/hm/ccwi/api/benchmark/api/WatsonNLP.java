package de.hm.ccwi.api.benchmark.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;

/**
 * Watson NLP Service.
 */
public class WatsonNLP extends APIBasics implements InterfaceAPI {

	private static WatsonNLP instance;
	private NaturalLanguageUnderstanding service;
	private Map<String, AnalysisResults> tweetList = new HashMap<>();

	private WatsonNLP() {
		service = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
				properties.getProperty("watsonAPIUsername"), properties.getProperty("watsonAPIKey"));
	}

	/**
	 * Gets the singleton instance.
	 *
	 * @return the instance
	 */
	public static WatsonNLP getInstance() {
		if (WatsonNLP.instance == null) {
			WatsonNLP.instance = new WatsonNLP();
		}
		return WatsonNLP.instance;
	}

	/**
	 * Gets the analysis result by querying the Watson NLU Service.
	 *
	 * @param message
	 *            the message to analyze.
	 * @return the results
	 */
	public AnalysisResults getResults(String message) {
		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder().limit(50).build();

		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().limit(50).build();

		Features features = new Features.Builder().entities(entitiesOptions).keywords(keywordsOptions).build();

		AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(message).features(features)
				.returnAnalyzedText(true).language("en").build();

		AnalysisResults result = service.analyze(parameters).execute();
		tweetList.put(message, result);
		return result;
	}

	@Override
	public void createPOST(String message) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveGET() throws IOException, ParseException {
		// TODO Auto-generated method stub
		
	}
}
