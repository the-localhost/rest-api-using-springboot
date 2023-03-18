package com.learn.springboot.firstrestapi.survey;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class SurveyResource {
	
	public SurveyResource(SurveyService surveyService) {
		super();
		this.surveyService = surveyService;
	}

	private SurveyService surveyService;
	
	@GetMapping("/surveys")
	public List<Survey> retrieveAllSurveys(){
		return surveyService.retrieveAllSurveys();
	}
}
