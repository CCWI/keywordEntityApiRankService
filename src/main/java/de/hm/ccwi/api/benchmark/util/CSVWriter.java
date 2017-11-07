package de.hm.ccwi.api.benchmark.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVWriter {

	private static final String DEFAULT_SEPARATOR = "U+0009";

	public static void writeLine(Writer w, List<String> values) throws IOException {
		writeLine(w, values, DEFAULT_SEPARATOR, ' ');
	}

	private static String followCVSformat(String value) {

		String result = value;
		if (result.contains("\"")) {
			result = result.replace("\"", "\"\"");
		}
		return result;

	}

	public static void writeLine(Writer w, List<String> values, String separators, char customQuote)
			throws IOException {

		boolean first = true;

		if (separators == " ") {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuilder sb = new StringBuilder();
		for (String value : values) {
			if (!first) {
				sb.append(separators);
			}
			if (customQuote == ' ') {
				sb.append(followCVSformat(value));
			} else {
				sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
			}

			first = false;
		}
		sb.append("\n");
		w.append(sb.toString());

	}

}