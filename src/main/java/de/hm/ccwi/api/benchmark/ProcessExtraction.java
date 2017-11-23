package de.hm.ccwi.api.benchmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;

import de.hm.ccwi.api.benchmark.api.InterfaceAPI;
import de.hm.ccwi.api.benchmark.export.CsvExporter;
import de.hm.ccwi.api.benchmark.rating.RatingCalculator;
import de.hm.ccwi.api.benchmark.rating.dto.EntityKeywordLog;
import de.hm.ccwi.api.benchmark.rating.dto.RatingLog;
import de.hm.ccwi.api.benchmark.sets.TestEntry;

/**
 * Test-execution class, which iterates the loaded entries and triggers
 * API-calls.
 * 
 * @author Max.Auch
 *
 */
public class ProcessExtraction {

	private static final Logger LOG = LogManager.getLogger("ProcessExtraction");

	private static Integer numberOfAPICalls = 0;

	private List<Class<? extends InterfaceAPI>> apiInterfaceClassList;

	private List<String> apiIdentifier;

	public ProcessExtraction() {
		this.apiInterfaceClassList = new ArrayList<>();
		this.apiIdentifier = new ArrayList<>();
	}

	public void process(List<TestEntry> testEntryList) throws IOException {
		List<EntityKeywordLog> processedLogList = new ArrayList<EntityKeywordLog>();

		List<String> stringlist = Arrays.asList(Configuration.apiList);

		stringlist.forEach(path -> {
			try {
				resolveApiInterface(path);
			} catch (IllegalAccessException | ClassNotFoundException e) {
				LOG.error("An error occured while resolving APIs!", e);
			}
		});

		int counter = 0;
		for (TestEntry entry : testEntryList) {
			if (counter < Configuration.AMOUNT_OF_ENTRIES_TO_ANALYZE) {
				processEntriesByApis(processedLogList, entry);
				counter++;

				numberOfAPICalls += 1;
				if (numberOfAPICalls == 1000) {
					LOG.warn("Daily API Limit (1000 Requests) reached");
					break;
				}
			} else {
				break;
			}
		}

		// Execute Rating
		List<RatingLog> ratingLogList = new RatingCalculator(Configuration.FUZZY_SEARCH).rateFoundEntriesOfAPI(apiIdentifier, processedLogList);

		// Export Result to CSV
		new CsvExporter().exportRatingLogList(ratingLogList);
	}

	private void processEntriesByApis(List<EntityKeywordLog> processedLogList, TestEntry entry) {
		List<InterfaceAPI> newApiInstancedList = IntStream.range(0, apiInterfaceClassList.size()).mapToObj(i -> {
			try {
				return apiInterfaceClassList.get(i).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				LOG.error("Error", e);
			}
			return null;
		}).collect(Collectors.toList());

		for (InterfaceAPI api : newApiInstancedList) {
			processedLogList.add(callProcessingAPI(api, new EntityKeywordLog(api.getApiName(), entry.getPost(),
					entry.getExpectedEntityList(), entry.getExpectedKeywordList())));
		}

	}

	private void resolveApiInterface(String api) throws IllegalAccessException, ClassNotFoundException {
		Class<? extends InterfaceAPI> apiClass = Class.forName(api).asSubclass(InterfaceAPI.class);
		this.apiInterfaceClassList.add(apiClass);
		this.apiIdentifier.add((String) FieldUtils.readDeclaredStaticField(apiClass, "API_IDENTIFIER"));
	}

	private EntityKeywordLog callProcessingAPI(InterfaceAPI api, EntityKeywordLog ratingLog) {
		try {
			api.createPOST(ratingLog.getText());
			api.executePOST();
			api.receiveGET();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ratingLog.setFoundEntryList(api.getFoundEntryList());

		return ratingLog;
	}

}
