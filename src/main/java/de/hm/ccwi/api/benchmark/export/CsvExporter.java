package de.hm.ccwi.api.benchmark.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hm.ccwi.api.benchmark.Configuration;
import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.rating.dto.RatingLog;
import de.hm.ccwi.api.benchmark.util.CSVWriter;

public class CsvExporter {

	private static final Logger LOG = LogManager.getLogger("CsvExporter");

	private String time = LocalDateTime.now().toString();

	private File file;

	private FileWriter writer = null;

	public CsvExporter() {
		this.time = time.replaceAll(":", "-");
		this.time = time.replaceAll("\\.", "-");
		this.file = new File(Configuration.CSV_PATH_FOR_EXPORT,
				Configuration.CSV_FILENAME_FOR_EXPORT + "_" + time + ".csv");
	}

	public void exportRatingLogList(List<RatingLog> processedLogList) {
		try {
			writer = new FileWriter(file);
			List<String> headerList = Arrays.asList("API", "F1", "Precision", "Recall", "FN", "FP", "TN", "TP",
					"Analyzed Text", "Found Entries of API", "Expected Entities", "Expected Keywords");
			CSVWriter.writeLine(writer, headerList);
			writer.flush();

			for (RatingLog log : processedLogList) {
				LOG.info("result of rating: {}", log.toString());
				String foundEntryString = createFoundEntryString(log.getEntityKeywordLog().getFoundEntryList());
				String expectedEntityString = createExpectedEntryString(log.getEntityKeywordLog().getExpectedEntityList());
				String expectedKeywordString = createExpectedEntryString(log.getEntityKeywordLog().getExpectedKeywordList());

				List<String> resultList = Arrays.asList(log.getApi(), convertDoubleToString(log.getF1()),
						convertDoubleToString(log.getPrecision()), convertDoubleToString(log.getRecall()),
						String.valueOf(log.getFn()), String.valueOf(log.getFp()), String.valueOf(log.getTn()),
						String.valueOf(log.getTp()), log.getEntityKeywordLog().getText(), foundEntryString,
						expectedEntityString, expectedKeywordString);

				CSVWriter.writeLine(writer, resultList);
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			LOG.error(e);
			e.printStackTrace();
		}
	}

	private String createExpectedEntryString(List<String> expectedList) {
		String expectedEntityString = "";
		StringBuilder sb2 = new StringBuilder();
		if (expectedList != null) {
			for (String entry : expectedList) {
				sb2.append(entry + " || ");
			}
			expectedEntityString = sb2.toString();
		}
		return expectedEntityString;
	}

	private String createFoundEntryString(List<ResponseEntry> respList) {
		String foundEntryString = "";
		StringBuilder sb1 = new StringBuilder();
		if (respList != null && !respList.isEmpty()) {
			for (ResponseEntry entry : respList) {
				sb1.append(entry.getEntry() + " || ");
			}
			foundEntryString = sb1.toString();
		}
		return foundEntryString;
	}

	private String convertDoubleToString(double d) {
		return ((DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMAN)).format(d);
	}
}
