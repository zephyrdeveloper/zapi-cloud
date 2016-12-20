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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

public class CreateTestWithTestSteps {

	private static String API_CREATE_TEST = "{SERVER}/rest/api/2/issue";
	private static String API_CREATE_TEST_STEP = "{SERVER}/public/rest/api/1.0/teststep/";

	/** Declare JIRA,Zephyr URL,access and secret Keys */

	private static String jiraBaseURL = "https://demo.atlassian.net";
	// zephyr connect URL got by ZAPI Installation
	private static String zephyrBaseUrl = "https://api.zephyr4jiracloud.com/connect";
	// zephyr accessKey , we can get from Addons >> zapi section
	private static String accessKey = "YjE2MjdjMGEtNzExNy0zYjY1LWFkMzQtNjcwMDM3MxIGFkbWluIGFkbWlu";
	// zephyr secretKey , we can get from Addons >> zapi section
	private static String secretKey = "qufnbimi96Ob2hq3ISF08yZRHmQw4c1eHGeGlk";

	/** Declare parameter values here */

	private static String userName = "admin";
	private static String password = "password";
	private static String projectId = "10100";
	private static String issueTypeId = "10005";

	static ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
			.build();
	JwtGenerator jwtGenerator = client.getJwtGenerator();

	public static void main(String[] args) throws JSONException, URISyntaxException, ParseException, IOException {
		final String createTestUri = API_CREATE_TEST.replace("{SERVER}", jiraBaseURL);
		final String createTestStepUri = API_CREATE_TEST_STEP.replace("{SERVER}", zephyrBaseUrl);

		/**
		 * Create Test Parameters, declare Create Test Issue fields Declare more
		 * field objects if required
		 */

		JSONObject projectObj = new JSONObject();
		projectObj.put("id", projectId); // Project ID where the Test to be
		// Created

		JSONObject issueTypeObj = new JSONObject();
		issueTypeObj.put("id", issueTypeId); // IssueType ID which is Test isse
		// type

		JSONObject assigneeObj = new JSONObject();
		assigneeObj.put("name", userName); // Username of the assignee

		JSONObject reporterObj = new JSONObject();
		reporterObj.put("name", userName); // Username of the Reporter

		String testSummary = "Sample Test case With Steps created through ZAPI Cloud"; // Test
		// Case
		// Summary/Name

		/**
		 * Create JSON payload to POST Add more field objects if required
		 * 
		 * ***DONOT EDIT BELOW ***
		 */

		JSONObject fieldsObj = new JSONObject();
		fieldsObj.put("project", projectObj);
		fieldsObj.put("summary", testSummary);
		fieldsObj.put("issuetype", issueTypeObj);
		fieldsObj.put("assignee", assigneeObj);
		fieldsObj.put("reporter", reporterObj);

		JSONObject createTestObj = new JSONObject();
		createTestObj.put("fields", fieldsObj);
		System.out.println(createTestObj.toString());
		byte[] bytesEncoded = Base64.encodeBase64((userName + ":" + password).getBytes());
		String authorizationHeader = "Basic " + new String(bytesEncoded);
		Header header = new BasicHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);

		StringEntity createTestJSON = null;
		try {
			createTestJSON = new StringEntity(createTestObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();
		try {
			// System.out.println(issueSearchURL);
			HttpPost createTestReq = new HttpPost(createTestUri);
			createTestReq.addHeader(header);
			createTestReq.addHeader("Content-Type", "application/json");
			createTestReq.setEntity(createTestJSON);

			response = restClient.execute(createTestReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String testId = null;
		int statusCode = response.getStatusLine().getStatusCode();
		// System.out.println(statusCode);
		HttpEntity entity1 = response.getEntity();
		if (statusCode >= 200 && statusCode < 300) {

			String string1 = null;
			try {
				string1 = EntityUtils.toString(entity1);
				System.out.println(string1);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			JSONObject createTestResp = new JSONObject(string1);
			testId = createTestResp.getString("id");
			System.out.println("testId :" + testId);
		} else {

			try {
				String string = null;
				string = EntityUtils.toString(entity1);
				JSONObject executionResponseObj = new JSONObject(string);
				System.out.println(executionResponseObj.toString());
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}

		/** Create test Steps ***/

		/**
		 * Create Steps Replace the step,data,result values as required
		 */

		JSONObject testStepJsonObj = new JSONObject();
		testStepJsonObj.put("step", "Sample Test Step");
		testStepJsonObj.put("data", "Sample Test Data");
		testStepJsonObj.put("result", "Sample Expected Result");

		/** DONOT EDIT FROM HERE ***/

		StringEntity createTestStepJSON = null;
		try {
			createTestStepJSON = new StringEntity(testStepJsonObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String finalURL = createTestStepUri + testId + "?projectId=" + projectId;
		URI uri = new URI(finalURL);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		System.out.println(uri.toString());
		System.out.println(jwt);

		HttpResponse responseTestStep = null;

		HttpPost addTestStepReq = new HttpPost(uri);
		addTestStepReq.addHeader("Content-Type", "application/json");
		addTestStepReq.addHeader("Authorization", jwt);
		addTestStepReq.addHeader("zapiAccessKey", accessKey);
		addTestStepReq.setEntity(createTestStepJSON);

		try {
			responseTestStep = restClient.execute(addTestStepReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int testStepStatusCode = responseTestStep.getStatusLine().getStatusCode();
		System.out.println(testStepStatusCode);
		System.out.println(response.toString());

		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = responseTestStep.getEntity();
			String string = null;
			String stepId = null;
			try {
				string = EntityUtils.toString(entity);
				JSONObject testStepObj = new JSONObject(string);
				stepId = testStepObj.getString("id");
				System.out.println("stepId :" + stepId);
				System.out.println(testStepObj.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}

	}
}
