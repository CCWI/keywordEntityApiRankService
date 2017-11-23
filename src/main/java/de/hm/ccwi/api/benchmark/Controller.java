package de.hm.ccwi.api.benchmark;

import java.util.ArrayList;
import java.util.List;

import de.hm.ccwi.api.benchmark.readFile.ReadCsvEntry;
import de.hm.ccwi.api.benchmark.sets.TestEntry;

public class Controller extends Configuration {

	public static void main(String[] args) {
		ProcessExtraction extraction = new ProcessExtraction();
		try {
			List<TestEntry> overallTestEntryList = new ArrayList<>();
			for(String file : GOLDSTANDARD_LIST) {	
				for(TestEntry entry : new ReadCsvEntry().readCsvToEntry(file)) {
					overallTestEntryList.add(entry);
				}
			}
			extraction.process(overallTestEntryList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}