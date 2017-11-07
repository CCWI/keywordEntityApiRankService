package de.hm.ccwi.api.benchmark;

public class Configuration {

	public static final String[] GOLDSTANDARD_LIST = { "GoldstandardEntityKeyword500-2.txt",
			"GoldstandardReddit100.txt" };
	
	/**
	 * Implements a fuzzy search. 
	 */
	public static final Boolean FUZZY_SEARCH = false;

	/**
	 * List all implemented APIs, which should be tested.
	 */
	public static final String[] apiList = new String[] {
			"de.hm.ccwi.api.benchmark.api.MeaningCloudAPI", 
			"de.hm.ccwi.api.benchmark.api.DandelionAPI",
			"de.hm.ccwi.api.benchmark.api.WatsonEntityAPI"};
	
	/**
	 * Only available for Arlchemy-API. Extraction of Entities and Keywords need to
	 * be set to true!
	 */
//	public static final Boolean OUTPUT_ENTITIES_AND_KEYWORDS_FOR_TWEET = true;
	public static final String CSV_FILE_FOR_EXPORT = "./src/main/resources/entityKeywordExport.csv";
	
	public static final String alchemyKeywordApiUri = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
	public static final String alchemyEntityApiUri = "http://access.alchemyapi.com/calls/text/TextGetRankedNamedEntities";
	public static final String dandelionApiUri = "https://api.dandelion.eu/datatxt/nex/v1";
	public static final String meaningcloudApiUri = "https://api.meaningcloud.com/topics-2.0";
	public static final String watsonApiUri = "https://gateway.watsonplatform.net/natural-language-understanding/api";

}
