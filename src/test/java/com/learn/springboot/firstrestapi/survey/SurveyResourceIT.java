package com.learn.springboot.firstrestapi.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {
	
	@Autowired
	private TestRestTemplate template;
	
	private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";
	
	private static String ALL_QUESTIONS_URL = "/surveys/Survey1/questions";
	
	@Test
	void retrieveSurveyQuestionById_basicScenario() throws JSONException{
		HttpHeaders headers = setContentTypeAndAuthHeaders();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
		
		ResponseEntity<String> responseEntity  = 
				template.exchange(SPECIFIC_QUESTION_URL, HttpMethod.GET, httpEntity, String.class);
		
//		ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URL, String.class);
		String expectedResponse = """
				{"id":"Question1",
				"description":"Most Popular Cloud Platform Today",
				"correctAnswer":"AWS"}
				""";
		
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}
	
	@Test
	void retrieveAllSurveyQuestions_basicScenario() throws JSONException{
		HttpHeaders headers = setContentTypeAndAuthHeaders();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
		
		ResponseEntity<String> responseEntity  = 
				template.exchange(ALL_QUESTIONS_URL, HttpMethod.GET, httpEntity, String.class);
		
//		ResponseEntity<String> responseEntity = template.getForEntity(ALL_QUESTIONS_URL, String.class);
		String expectedResponse = """
				[
					{
						"id":"Question1"
					},
					{
						"id":"Question2"
					},
					{
						"id":"Question3"
					}
				]
				""";
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}
	
	@Test
	void addNewSurveyQuestion_basicScenario() {
		String requestBody = """
				{
					"description": "Most Popular Platform Automation Tool",
					"options": [
						"Kubernetes",
						"Docker",
						"Terraform",
						"Azure DevOps"
					],
					"correctAnswer": "Terraform"
				}
				""";
		
		HttpHeaders headers = setContentTypeAndAuthHeaders();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
		
		ResponseEntity<String> responseEntity  = 
				template.exchange(ALL_QUESTIONS_URL, HttpMethod.POST, httpEntity, String.class);
		
		String locationHeader = responseEntity.getHeaders().get("Location").get(0);
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));
		
		ResponseEntity<String> responseEntityDelete  = 
				template.exchange(locationHeader, HttpMethod.DELETE, httpEntity, String.class);
		assertTrue(responseEntityDelete.getStatusCode().is2xxSuccessful());
	}

	private HttpHeaders setContentTypeAndAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Basic " + performBasicAuthEncoding("admin", "password"));
		return headers;
	}
	
	String performBasicAuthEncoding(String user, String password) {
		String combined = user + ":" + password;
		byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
		return new String(encodedBytes);
	}
}
