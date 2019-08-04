package de.hm.ccwi.api.benchmark;

public class Configuration {

	public static final String languageOfGoldstandard = "en";
	public static final String[] GOLDSTANDARD_LIST = { };
	public static final String LIST_SEPARATOR = ";$;";
	public static final Integer AMOUNT_OF_ENTRIES_TO_ANALYZE = 500;
	public static final Boolean FUZZY_SEARCH = true;
	public static final String[] apiList = new String[] { "de.hm.ccwi.api.benchmark.api.MeaningCloudAPI",
			"de.hm.ccwi.api.benchmark.api.DandelionAPI", "de.hm.ccwi.api.benchmark.api.WatsonNLP"};

	// public static final Boolean OUTPUT_ENTITIES_AND_KEYWORDS_FOR_TWEET = true;
	public static final String CSV_PATH_FOR_EXPORT = "./src/main/resources/";
	public static final String CSV_FILENAME_FOR_EXPORT = "entityKeywordExport";
	
	public static final String alchemyKeywordApiUri = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
	public static final String alchemyEntityApiUri = "http://access.alchemyapi.com/calls/text/TextGetRankedNamedEntities";
	public static final String dandelionApiUri = "https://api.dandelion.eu/datatxt/nex/v1";
	public static final String meaningcloudApiUri = "https://api.meaningcloud.com/topics-2.0";
	public static final String watsonApiUri = "https://gateway.watsonplatform.net/natural-language-understanding/api";
}
