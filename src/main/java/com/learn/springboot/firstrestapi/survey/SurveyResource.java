package com.learn.springboot.firstrestapi.survey;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

	@RequestMapping(value = "/surveys/{surveyId}/questions", method = RequestMethod.GET)
	public List<Question> retrieveSurveyQuestions(@PathVariable String surveyId) {
		List<Question> questions = surveyService.retrieveSurveyQuestions(surveyId);
		if (questions == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return questions;
	}

	@RequestMapping(value = "/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.GET)
	public Question retrieveSurveyQuestionById(@PathVariable String surveyId, @PathVariable String questionId) {
		Question questionById = surveyService.retrieveSurveyQuestionById(surveyId, questionId);

		if (questionById == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return questionById;
	}

	@RequestMapping(value = "/surveys/{surveyId}/questions", method = RequestMethod.POST)
	public ResponseEntity<Object> addNewSurveyQuestion(@PathVariable String surveyId, @RequestBody Question question) {

		String questionId = surveyService.addNewSurveyQuestion(surveyId, question);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{questionId}").buildAndExpand(questionId)
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(value = "/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteSurveyQuestionById(@PathVariable String surveyId,
			@PathVariable String questionId) {
		String deletedQuestionId = surveyService.deleteSurveyQuestionById(surveyId, questionId);
		if (deletedQuestionId == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateSurveyQuestionById(@PathVariable String surveyId,
			@PathVariable String questionId, @RequestBody Question question) {
		String updatedQuestionId = surveyService.updateSurveyQuestionById(surveyId, questionId, question);
		if (updatedQuestionId == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{updatedQuestionId}")
				.buildAndExpand(updatedQuestionId).toUri();
		return ResponseEntity.created(location).build();
	}
}
