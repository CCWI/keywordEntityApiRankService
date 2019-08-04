package de.hm.ccwi.api.benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.hm.ccwi.api.benchmark.sets.TestEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller extends Configuration {

	private static final Logger LOG = LogManager.getLogger("Controller");

	public static void main(String[] args) {
		ProcessExtraction extraction = new ProcessExtraction();
		try {
			List<TestEntry> overallTestEntryList = new ArrayList<>();
			for(String file : GOLDSTANDARD_LIST) {	
				for(TestEntry entry : readCsvToEntry(file)) {
					overallTestEntryList.add(entry);
				}
			}
			extraction.process(overallTestEntryList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<TestEntry> readCsvToEntry(String file) {
		List<TestEntry> testEntryList = new ArrayList<>();
		InputStream is = ClassLoader.class.getResourceAsStream("/" + file);
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String nextLine;
		try {
			while ((nextLine = in.readLine()) != null) {
				TestEntry tEntry = new TestEntry();
				StringTokenizer st = new StringTokenizer(nextLine, Configuration.LIST_SEPARATOR);
				int counter = 0;
				while (st.hasMoreTokens()) {
					String rowCell = st.nextToken();
					counter++;

					if (rowCell == null) {
						break;
					}

					if (counter == 1) {
						tEntry.setPost(rowCell);
					}

					if (counter == 2) {
						tEntry.setExpectedEntityList(listEntityKeywordsOfString(rowCell));
					}

					if (counter == 3) {
						tEntry.setExpectedKeywordList(listEntityKeywordsOfString(rowCell));
					}
				}

				if(tEntry.getPost() != null) {
					testEntryList.add(tEntry);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.info(">> For file: " + file + "Extracted list-size: " + testEntryList.size());
		return testEntryList;
	}

	private static List<String> listEntityKeywordsOfString(String rowCell) {
		List<String> list = new ArrayList<>();

		StringTokenizer st = new StringTokenizer(rowCell, "|");
		while (st.hasMoreTokens()) {
			String item = st.nextToken();
			list.add(item);
		}
		return list;
	}
}