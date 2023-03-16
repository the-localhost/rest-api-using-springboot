package com.learn.springboot.firstrestapi.helloWorld;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {
	
	@RequestMapping("/hello-world")
	String helloWorld() { 
		return "Hello World!";
	}
	
	@RequestMapping("/hello-world-bean")
	HelloWorldBean helloWorldBean() { 
		return new HelloWorldBean("Hello World!");
	}
	
	@RequestMapping("/hello-world-path-variable/{name}/message/{message}")
	HelloWorldBean helloWorldPathVariable(@PathVariable String name,
											@PathVariable String message) { 
		return new HelloWorldBean("Hello, "+name+"! "+message);
	}
}
