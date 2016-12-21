/* D SOFTWARE INCORPORATED
 * Copyright 2007-2011 D Software Incorporated
 * All Rights Reserved.
 *
 * NOTICE: D Software permits you to use, modify, and distribute this 
file
 * in accordance with the terms of the license agreement accompanying 
it.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS? BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
implied.
 */
/*
 * This is a sample of what can be done by using API's with Zephyr through 
the JAVA coding language.
 * By creating the .java files, you can import them 
into your workspace and then call them in your custom program. 
 * 
 * Eclipse Java EE IDE for Web Developers.
Version: Neon Release (4.6.0)
Build id: 20160613-1800
 * Java- Java JDK 1.8.0_101
 * 
 * Author: Swapna Kumar Vemula, Product Support Engineer, D Software Inc.
 */
package com.thed.zapi.cloud.sample;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.atlassian.jira.rest.client.internal.json.JsonArrayParser;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

import net.minidev.json.JSONUtil;

public class CycleExecutionReportByVersion {
	public static void main(String[] args) throws JSONException, URISyntaxException, ParseException, IOException {

		String API_GET_EXECUTIONS = "{SERVER}/public/rest/api/1.0/executions/search/cycle/";
		String API_GET_CYCLES = "{SERVER}/public/rest/api/1.0/cycles/search?";
		// Delimiter used in CSV file
		final String NEW_LINE_SEPARATOR = "\n";
		final String fileName = "F:\\cycleExecutionReport.csv";

		/** Declare JIRA,Zephyr URL,access and secret Keys */

		String jiraBaseURL = "https://demo.atlassian.net";
		// zephyr connect URL got by ZAPI Installation
		String zephyrBaseUrl = "https://api.zephyr4jiracloud.com/connect";
		// zephyr accessKey , we can get from Addons >> zapi section
		String accessKey = "YjE2MjdjMGEtNzExNy0zYjY1LWFkMzQtNjcwMDM3OTljFkbWluIGFkbWlu";
		// zephyr secretKey , we can get from Addons >> zapi section
		String secretKey = "qufnbimi96Ob2hq3ISF08yZ8Qw4c1eHGeGlk";

		/** Declare parameter values here */
		String userName = "admin";
		String versionId = "-1";
		String projectId = "10100";
		String projectName = "Support";
		String versionName = "Unscheduled";

		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
				.build();
		/**
		 * Get List of Cycles by Project and Version
		 */

		final String getCyclesUri = API_GET_CYCLES.replace("{SERVER}", zephyrBaseUrl) + "projectId=" + projectId
				+ "&versionId=" + versionId;

		Map<String, String> cycles = getCyclesByProjectVersion(getCyclesUri, client, accessKey);
		// System.out.println("cycles :"+ cycles.toString());

		/**
		 * Iterating over the Cycles and writing the report to CSV
		 * 
		 */

		FileWriter fileWriter = null;
		System.out.println("Writing CSV file.....");
		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header

			fileWriter.append("Cycle Execution Report By Version and Project");
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("PROJECT:" + "," + projectName);
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("VERSION:" + "," + versionName);
			fileWriter.append(NEW_LINE_SEPARATOR);

			JSONArray executions;
			int totalUnexecutedCount = 0;
			int totalExecutionCount = 0;

			for (String key : cycles.keySet()) {
				int executionCount = 0;
				int unexecutedCount = 0;
				final String getExecutionsUri = API_GET_EXECUTIONS.replace("{SERVER}", zephyrBaseUrl) + key
						+ "?projectId=" + projectId + "&versionId=" + versionId;
				fileWriter.append("Cycle:" + "," + cycles.get(key));
				fileWriter.append(NEW_LINE_SEPARATOR);
				executions = getExecutionsByCycleId(getExecutionsUri, client, accessKey);
				// System.out.println("executions :" + executions.toString());

				HashMap<String, Integer> counter = new HashMap<String, Integer>();

				String[] statusName = new String[executions.length()];
				for (int i = 0; i < executions.length(); i++) {
					JSONObject executionObj = executions.getJSONObject(i).getJSONObject("execution");
					// System.out.println("executionObj
					// "+executionObj.toString());
					JSONObject statusObj = executionObj.getJSONObject("status");
					// System.out.println("statusObj :"+statusObj.toString());
					statusName[i] = statusObj.getString("name");
				}

				if (statusName.length != 0) {
					// System.out.println(statusName.toString());
					for (String a : statusName) {
						if (counter.containsKey(a)) {
							int oldValue = counter.get(a);
							counter.put(a, oldValue + 1);
						} else {
							counter.put(a, 1);
						}
					}
					for (String status : counter.keySet()) {
						fileWriter.append(" " + "," + " " + "," + status + "," + counter.get(status));
						fileWriter.append(NEW_LINE_SEPARATOR);
						if (status.equalsIgnoreCase("UNEXECUTED")) {
							unexecutedCount += counter.get(status);
						} else {
							executionCount += counter.get(status);
						}

					}
				}
				totalExecutionCount += executionCount;
				totalUnexecutedCount += unexecutedCount;

				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("TOTAL CYCLES:" + "," + cycles.size());
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("TOTAL EXECUTIONS:" + "," + totalExecutionCount);
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("TOTAL ASSIGNED:" + "," + (totalUnexecutedCount + totalExecutionCount));

			System.out.println("CSV file was created successfully !!!");
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}

	}

	private static Map<String, String> getCyclesByProjectVersion(String getCyclesUri, ZFJCloudRestClient client,
			String accessKey) throws URISyntaxException, JSONException {
		// TODO Auto-generated method stub

		Map<String, String> cycleMap = new HashMap<String, String>();
		URI uri = new URI(getCyclesUri);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);
		// System.out.println(uri.toString());
		// System.out.println(jwt);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Authorization", jwt);
		httpGet.setHeader("zapiAccessKey", accessKey);

		try {
			response = restClient.execute(httpGet);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		// System.out.println(statusCode);

		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity1 = response.getEntity();
			String string1 = null;
			try {
				string1 = EntityUtils.toString(entity1);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// System.out.println(string1);
			JSONArray cyclesArray = new JSONArray(string1);
			for (int i = 0; i < cyclesArray.length(); i++) {
				JSONObject cycleObj = cyclesArray.getJSONObject(i);
				String cycleID = cycleObj.getString("id");
				String cycleName = cycleObj.getString("name");
				cycleMap.put(cycleID, cycleName);
				// System.out.println(IssuesArray.length());
			}

		}
		return cycleMap;
	}

	private static JSONArray getExecutionsByCycleId(String uriStr, ZFJCloudRestClient client, String accessKey)
			throws URISyntaxException, JSONException {
		JSONArray IssuesArray = null;
		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);
		// System.out.println(uri.toString());
		// System.out.println(jwt);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Authorization", jwt);
		httpGet.setHeader("zapiAccessKey", accessKey);

		try {
			response = restClient.execute(httpGet);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		// System.out.println(statusCode);

		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity1 = response.getEntity();
			String string1 = null;
			try {
				string1 = EntityUtils.toString(entity1);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// System.out.println(string1);
			JSONObject allIssues = new JSONObject(string1);
			IssuesArray = allIssues.getJSONArray("searchObjectList");
			// System.out.println(IssuesArray.length());

		}
		return IssuesArray;
	}

}
