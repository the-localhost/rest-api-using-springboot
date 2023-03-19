package com.learn.springboot.firstrestapi.survey;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SurveyResource {

	public SurveyResource(SurveyService surveyService) {
		super();
		this.surveyService = surveyService;
	}

	private SurveyService surveyService;

	@RequestMapping("/surveys")
	public List<Survey> retrieveAllSurveys() {
		return surveyService.retrieveAllSurveys();
	}

	@RequestMapping("/surveys/{surveyId}")
	public Survey retrieveSurveyById(@PathVariable String surveyId) {
		Survey survey = surveyService.retrieveSurveyById(surveyId);
		if (survey == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return survey;
	}

	@RequestMapping(value="/surveys/{surveyId}/questions", method = RequestMethod.GET)
	public List<Question> retrieveSurveyQuestions(@PathVariable String surveyId) {
		List<Question> questions = surveyService.retrieveSurveyQuestions(surveyId);
		if (questions == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return questions;
	}

	@RequestMapping("/surveys/{surveyId}/questions/{questionId}")
	public Question retrieveSurveyQuestionById(@PathVariable String surveyId, @PathVariable String questionId) {
		Question questionById = surveyService.retrieveSurveyQuestionById(surveyId, questionId);

		if (questionById == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return questionById;
	}
	
	@RequestMapping(value="/surveys/{surveyId}/questions", method = RequestMethod.POST)
	public void addNewSurveyQuestion(@PathVariable String surveyId,
											@RequestBody Question question) {
		
		surveyService.addNewSurveyQuestion(surveyId, question);
	}
}
