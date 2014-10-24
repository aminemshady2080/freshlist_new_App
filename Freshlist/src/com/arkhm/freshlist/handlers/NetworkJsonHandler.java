package com.arkhm.freshlist.handlers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NetworkJsonHandler {
	private InputStream inputStream = null;
	private String jsonData;
	private JSONObject theDataObject = null;
	public JSONObject userObject = null;

	public NetworkJsonHandler() {
		// TODO Auto-generated constructor stub
	}

	public JSONObject getDataFromUrl(String dataUrl, List<NameValuePair> values) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(dataUrl);
			httpPost.setEntity(new UrlEncodedFormEntity(values));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = null;
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();
				Log.d("suuccess post", "the code is "
						+ httpResponse.getStatusLine().getStatusCode());

			} else {
				Log.d("Not success post ", "the code is "
						+ httpResponse.getStatusLine().getStatusCode()
						+ " \nand reason is "
						+ httpResponse.getStatusLine().getReasonPhrase());
			}
			// return JSON String to business models to use
		} catch (Exception e) {
			Log.e("Error in getin data from url ", e.getMessage());

		}
		try {
			theDataObject = new JSONObject(makeJsonDataFromBuffer(inputStream));
		} catch (JSONException e) {
			Log.e("error in json convertion ", e.getMessage());

			e.printStackTrace();
		}
		return theDataObject;

	}

	private String makeJsonDataFromBuffer(InputStream instream) {
		StringBuilder builder = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");

				System.out.println("data " + line);
			}

			jsonData = builder.toString();
			Log.d("Json Data Returned ", jsonData);
			inputStream.close();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		return builder.toString();
	}
}
