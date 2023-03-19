package com.learn.springboot.firstrestapi.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private UserDetailsRepository repository;
	
	public UserDetailsCommandLineRunner(UserDetailsRepository repository) {
		super();
		this.repository = repository;
	}



	@Override
	public void run(String... args) throws Exception {
		repository.save(new UserDetails("Gagan", "Admin"));
		repository.save(new UserDetails("Amit", "Admin"));
		repository.save(new UserDetails("Chirag", "Admin"));
		
	}

}
