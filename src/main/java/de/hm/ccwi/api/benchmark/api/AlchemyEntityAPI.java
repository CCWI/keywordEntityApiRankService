package de.hm.ccwi.api.benchmark.api;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import de.hm.ccwi.api.benchmark.Configuration;
import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;
import de.hm.ccwi.api.benchmark.util.SortResponseEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlchemyEntityAPI extends APIBasics implements InterfaceAPI {

	private static final Logger LOG = LogManager.getLogger("watsonAPI");
	
    public static final String API_IDENTIFIER = "Alchemy";

    
    /**
     * Constructor.
     */
	public AlchemyEntityAPI() {
		apiKey = properties.getProperty("alchemyAPIKey");
		outputMode = properties.getProperty("outputMode");
	}

	/**
	 * Implemented createPOST from Interface interfaceAPI (see for more details)
	 *
	 * @param tweet
	 *            Twitter Tweet which should be posted
	 * @throws UnsupportedEncodingException
	 *             if text is not in Unicode
	 */
	@Override
	public void createPOST(String tweet) throws UnsupportedEncodingException {
		httpclient = HttpClients.createDefault();
		httppost = new HttpPost(Configuration.alchemyEntityApiUri);

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("text", tweet));
		params.add(new BasicNameValuePair("apikey", apiKey));
		params.add(new BasicNameValuePair("outputMode", outputMode));

		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

	}

	/**
	 * Implemented receiveGET from Interface interfaceAPI (see for more details)
	 *
	 * @throws IOException
	 *             IO Error
	 * @throws ParseException
	 *             Parse Error
	 */
	@Override
	public void receiveGET() throws IOException, ParseException {
		JSONArray JSONArray = readResponseJSON(API_IDENTIFIER, EntityUtils.toString(httpEntity, "UTF-8"), "entities");

		if (JSONArray != null) {
			for (Object aJSONArray : JSONArray) {

				JSONObject object = (JSONObject) aJSONArray;
				ResponseEntry entity = new ResponseEntry();

				String s = (String) object.get("text");
				s = addEntity(s);

				// Add Entity only if it is new and has not been added before
				if (s != null) {
					entity.setEntry(s);
					entity.setConfidence(convertRelevance((String) object.get("relevance")));
					foundEntryList.add(entity);
				}
			}

			// Sort the Array List Entities from A to Z
			Collections.sort(foundEntryList, new SortResponseEntity());
			int i = 1;
			for (ResponseEntry e : foundEntryList) {
				LOG.debug("Entity " + i + " is " + e.getEntry() + "(" + e.getConfidence() + ")");
				i++;
			}
		}
	}
	
	@Override
	public String getApiName() {
		return API_IDENTIFIER;
	}
}
