package de.hm.ccwi.api.benchmark.api;

import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.*;
import de.hm.ccwi.api.benchmark.Configuration;
import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.util.SortResponseEntity;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

public class WatsonNLP extends APIBasics implements InterfaceAPI {

    public static final String API_IDENTIFIER = "watsonNlp";

    private static WatsonNLP instance;
    private NaturalLanguageUnderstanding service;
    private AnalyzeOptions parameters;
    private AnalysisResults result;

    public WatsonNLP() {
        IamOptions options = new IamOptions.Builder().apiKey("").build();
        service = new NaturalLanguageUnderstanding("2019-07-07", options);
        service.setEndPoint("https://gateway-fra.watsonplatform.net/natural-language-understanding/api");
    }

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

        this.parameters = new AnalyzeOptions.Builder().text(message).features(features).returnAnalyzedText(true)
                .language(Configuration.languageOfGoldstandard).build();

    }

    @Override
    public void executePOST() throws IOException {
        this.result = service.analyze(parameters).execute().getResult();
    }

    @Override
    public void receiveGET() throws IOException, ParseException {
        if (this.result != null) {

            for (EntitiesResult eR : this.result.getEntities()) {
                ResponseEntry response = new ResponseEntry();
                if (eR != null && eR.getText() != null) {
                    response.setEntry(eR.getText());
                }
                foundEntryList.add(response);
            }
            for (KeywordsResult kwR : this.result.getKeywords()) {
                ResponseEntry response = new ResponseEntry();
                if (kwR != null && kwR.getText() != null) {
                    response.setEntry(kwR.getText());
                }
                foundEntryList.add(response);
            }
        }
        Collections.sort(foundEntryList, new SortResponseEntity());
    }

    @Override
    public String getApiName() {
        return API_IDENTIFIER;
    }
}
