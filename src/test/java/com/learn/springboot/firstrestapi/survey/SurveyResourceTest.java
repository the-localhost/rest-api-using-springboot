package com.learn.springboot.firstrestapi.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = SurveyResource.class)
public class SurveyResourceTest {
	
	private static String ALL_QUESTION_URL = 
			"http://localhost:8080/surveys/Survey1/questions";

	@MockBean
	private SurveyService surveyService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private static String SPECIFIC_QUESTION_url = 
			"http://localhost:8080/surveys/Survey1/questions/Question1";
	
	@Test
	void retrieveSurveyQuestionById_404Scenario() throws Exception {
		RequestBuilder requestBuilder = 
				MockMvcRequestBuilders.get(SPECIFIC_QUESTION_url).accept(MediaType.APPLICATION_JSON);
		
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		
		// returns 404 as the mock surveyService does not return anything
		// and we will need a stub function
		assertEquals(404, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void retrieveSurveyQuestionById_basicScenario() throws Exception {
		RequestBuilder requestBuilder = 
				MockMvcRequestBuilders.get(SPECIFIC_QUESTION_url).accept(MediaType.APPLICATION_JSON);
		
		Question question = new Question("Question1", "Most Popular Cloud Platform Today",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
		
		String expectedResponse = """
				{
					"id":"Question1",
					"description":"Most Popular Cloud Platform Today",
					"options":["AWS","Azure","Google Cloud","Oracle Cloud"],
					"correctAnswer":"AWS"
				}
				""";
		
		when(surveyService.retrieveSurveyQuestionById("Survey1", "Question1")).thenReturn(question);
		
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = mvcResult.getResponse();
		
		JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	void retrieveSurveyQuestions_basicScenario() throws Exception {
		RequestBuilder requestBuilder = 
				MockMvcRequestBuilders.get(ALL_QUESTION_URL).accept(MediaType.APPLICATION_JSON);
		
		Question question1 = new Question("Question1", "Most Popular Cloud Platform Today",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
		Question question2 = new Question("Question2", "Fastest Growing Cloud Platform",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
		Question question3 = new Question("Question3", "Most Popular DevOps Tool",
				Arrays.asList("Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

		List<Question> questions = new ArrayList<>(Arrays.asList(question1, question2, question3));
		
		String expectedResponse = """
				[
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
					},
					{
						id: "Question2",
						description: "Fastest Growing Cloud Platform",
						options: [
							"AWS",
							"Azure",
							"Google Cloud",
							"Oracle Cloud"
						],
						correctAnswer: "Google Cloud"
					},
					{
						id: "Question3",
						description: "Most Popular DevOps Tool",
						options: [
							"Kubernetes",
							"Docker",
							"Terraform",
							"Azure DevOps"
						],
						correctAnswer: "Kubernetes"
					}
				]
				""";
		
		when(surveyService.retrieveSurveyQuestions("Survey1")).thenReturn(questions);
		
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = mvcResult.getResponse();
		
		JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	void addNewServeyQuestion_basicScenario() throws Exception {
		String requestBody = """
				{
				    "description": "Most Popular PlatEng Tool",
				    "options": [
				        "Kubernetes",
				        "Docker",
				        "Terraform",
				        "Azure DevOps"
				    ],
				    "correctAnswer": "Kubernetes"
				}
				""";
		
		when(surveyService.addNewSurveyQuestion(anyString(), any()))
				.thenReturn("SOME_ID");
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(ALL_QUESTION_URL)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestBody).contentType(MediaType.APPLICATION_JSON);
		
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = mvcResult.getResponse();
		
		assertEquals(201, response.getStatus());
		assertTrue(response.getHeader("Location").contains("/surveys/Survey1/questions/SOME_ID"));
	}
}
