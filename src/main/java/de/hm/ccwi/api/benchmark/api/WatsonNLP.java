package de.hm.ccwi.api.benchmark.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

import org.json.simple.parser.ParseException;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

import de.hm.ccwi.api.benchmark.Configuration;
import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.util.SortResponseEntity;

/**
 * Watson NLP Service.
 */
public class WatsonNLP extends APIBasics implements InterfaceAPI {

	private static WatsonNLP instance;
	private NaturalLanguageUnderstanding service;
//	private Map<String, AnalysisResults> tweetList = new HashMap<>();
	private AnalyzeOptions parameters;
	private AnalysisResults result;
	
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

	@Override
	public void createPOST(String message) throws UnsupportedEncodingException {
		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder().limit(50).build();

		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().limit(50).build();

		Features features = new Features.Builder().entities(entitiesOptions).keywords(keywordsOptions).build();

		this.parameters = new AnalyzeOptions.Builder().text(message).features(features)
				.returnAnalyzedText(true).language(Configuration.languageOfGoldstandard).build();

	}
	
	@Override
	public void executePOST() throws IOException {
		this.result = service.analyze(parameters).execute();
	}

	@Override
	public void receiveGET() throws IOException, ParseException {
		for(EntitiesResult eR : this.result.getEntities()) {
			ResponseEntry response = new ResponseEntry();
			response.setEntry(eR.getText());
			foundEntryList.add(response);
		}
		for(KeywordsResult kwR : this.result.getKeywords()) {
			ResponseEntry response = new ResponseEntry();
			response.setEntry(kwR.getText());
			foundEntryList.add(response);
		}
        Collections.sort(foundEntryList, new SortResponseEntity());
	}
}
