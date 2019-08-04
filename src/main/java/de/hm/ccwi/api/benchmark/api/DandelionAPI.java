package de.hm.ccwi.api.benchmark.api;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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

public class DandelionAPI extends APIBasics implements InterfaceAPI {

    public static final String API_IDENTIFIER = "Dandelion";

    public DandelionAPI () {
        apiKey = properties.getProperty("dandelionAPIKey");
    }

    public void createPOST (String message) throws UnsupportedEncodingException {
        httpclient = HttpClients.createDefault();
        httppost = new HttpPost(Configuration.dandelionApiUri);
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("text", message));
        params.add(new BasicNameValuePair("token", apiKey));

        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
    }

    public void receiveGET () throws IOException, ParseException {
        JSONArray JSONArray = readResponseJSON(API_IDENTIFIER, EntityUtils.toString(httpEntity, "UTF-8"), "annotations");

        if (JSONArray != null) {
            for (Object aJSONArray : JSONArray) {

                JSONObject object = (JSONObject) aJSONArray;
                ResponseEntry entity = new ResponseEntry();

                String s = (String) object.get("spot");
                s = addEntity(s);

                if (s != null) {
                    entity.setEntry(s);
                    entity.setConfidence((Double) object.get("confidence"));
                    foundEntryList.add(entity);
                }
            }

            Collections.sort(foundEntryList, new SortResponseEntity());
        }
    }
    
	@Override
	public String getApiName() {
		return API_IDENTIFIER;
	}
}
