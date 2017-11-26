package de.hm.ccwi.api.benchmark;

public class Configuration {

	/**
	 * Language of Messages to analyze. This option is an additional infomation for
	 * APIs, which require this value. For Example: WatsonAPI.
	 */
	public static final String languageOfGoldstandard = "en";

	/**
	 * List of messages, which need to be analyzed. These lists should also contain
	 * the expected entity- and keyword-lists.
	 */
	public static final String[] GOLDSTANDARD_LIST = 
		{ "goldstandardEntityKeyword-sample.txt" };

	/**
	 * Separator in Goldstandard-lists.
	 */
	public static final String LIST_SEPARATOR = ";$;";

	/**
	 * Amount of entries in Goldstandard-List, which should be analyzed.
	 */
	public static final Integer AMOUNT_OF_ENTRIES_TO_ANALYZE = 1;

	/**
	 * Implements a fuzzy search.
	 */
	public static final Boolean FUZZY_SEARCH = true;

	/**
	 * List all implemented APIs, which should be tested. For example:
	 * "de.hm.ccwi.api.benchmark.api.AlchemyEntityAPI"
	 * "de.hm.ccwi.api.benchmark.api.AlchemyKeywordAPI"
	 * "de.hm.ccwi.api.benchmark.api.MeaningCloudAPI"
	 * "de.hm.ccwi.api.benchmark.api.DandelionAPI"
	 * "de.hm.ccwi.api.benchmark.api.WatsonNLP"
	 */
	public static final String[] apiList = new String[] { "de.hm.ccwi.api.benchmark.api.MeaningCloudAPI",
			"de.hm.ccwi.api.benchmark.api.DandelionAPI", "de.hm.ccwi.api.benchmark.api.WatsonNLP"};

	/**
	 * Only available for Arlchemy-API. Extraction of Entities and Keywords need to
	 * be set to true!
	 */
	// public static final Boolean OUTPUT_ENTITIES_AND_KEYWORDS_FOR_TWEET = true;
	public static final String CSV_PATH_FOR_EXPORT = "./src/main/resources/";
	public static final String CSV_FILENAME_FOR_EXPORT = "entityKeywordExport";
	
	public static final String alchemyKeywordApiUri = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
	public static final String alchemyEntityApiUri = "http://access.alchemyapi.com/calls/text/TextGetRankedNamedEntities";
	public static final String dandelionApiUri = "https://api.dandelion.eu/datatxt/nex/v1";
	public static final String meaningcloudApiUri = "https://api.meaningcloud.com/topics-2.0";
	public static final String watsonApiUri = "https://gateway.watsonplatform.net/natural-language-understanding/api";

}
