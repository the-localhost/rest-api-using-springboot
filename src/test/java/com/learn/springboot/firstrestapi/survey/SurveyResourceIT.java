package com.learn.springboot.firstrestapi.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {
	// http://localhost:8080/surveys/Survey1/questions/Question1
	String str = """
			{
				id: "Question1",
				description: "Most Popular Cloud Platform Today",
				options: [
					"AWS",
					"Azure",
					"Google Cloud",
					"Oracle Cloud"
				],
				correctAnswer: "AWS"
			}
			""";
	
	@Autowired
	private TestRestTemplate template;
	
	private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";
	
	private static String ALL_QUESTIONS_URL = "/surveys/Survey1/questions";
	
	@Test
	void retrieveSurveyQuestionById_basicScenario() throws JSONException{
		ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URL, String.class);
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
		ResponseEntity<String> responseEntity = template.getForEntity(ALL_QUESTIONS_URL, String.class);
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
//		System.out.println(responseEntity.getBody());
	}
}
