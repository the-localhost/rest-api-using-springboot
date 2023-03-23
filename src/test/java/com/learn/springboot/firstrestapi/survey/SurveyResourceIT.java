package com.learn.springboot.firstrestapi.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
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
	
	@Test
	void retrieveSurveyQuestionById_basicScenario(){
		ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URL, String.class);
		String expectedResponse = """
				{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
				""";
		assertEquals(expectedResponse.trim(), responseEntity.getBody());
		System.out.println(responseEntity.getBody());
		System.out.println(responseEntity.getHeaders());
	}
}
